package me.pixeldots.pixelscharactermodels.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NodeButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.OffsetFlatButtonWidget;
import me.pixeldots.pixelscharactermodels.network.ClientNetwork;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.Node;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.script.PostfixOperation;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class EditorGui extends GuiHandler {

    public TextFieldWidget PehkuiScale, SkinSuffix;

    public static int selectedPart = -1;
    public static ModelPart selectedPartModel = null;
    public static int selectedNode = -1;
    public static List<Node> nodes = new ArrayList<>();
    public static int yscroll = 0;
    public static boolean isDragging = false;

    public List<ButtonWidget> scrollable_widgets = new ArrayList<>();
    public UUID uuid;
    public float stored_pehkuiscale = 1;
    public float entityViewScale = 75;

    public LivingEntity entity;
    public EntityModel<?> model;
    
    public EditorGui(LivingEntity _entity) {
        super("Editor");
        entity = _entity;
        model = PlatformUtils.getModel(_entity);
        uuid = _entity.getUuid();
        if (!(model instanceof AnimalModel<?>)) PCMClient.minecraft.setScreen(null);
    }

    public EditorGui(LivingEntity _entity, float _entityViewScale) {
        this(_entity);
        entityViewScale = _entityViewScale;
    }

    public void setScreen(GuiHandler gui) {
        selectedPart = -1;
        selectedPartModel = null;
        selectedNode = -1;
        nodes.clear();
        yscroll = 0;
        isDragging = false;
        this.client.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();
        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text.of("Presets"), (btn) -> {
            setScreen(new PresetsGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text.of("Editor"), (btn) -> {})).active = false;
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text.of("Animation"), (btn) -> {
            setScreen(new AnimationGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text.of("Settings"), (btn) -> {
            setScreen(new SettingsGui(entity, this.entityViewScale));
        }));

        // Left Panel
        addButton(new FlatButtonWidget(5, 15, 100, 10, Text.of("compile"), (btn) -> {
            compile_nodes(uuid, selectedPartModel, true);
        }));

        if (selectedNode == -1) {
            // Pehkui Scale
            PehkuiScale = addTextField(new TextFieldWidget(textRenderer, 5, 30, 100, 10, Text.of("")));
            SkinSuffix = addTextField(new TextFieldWidget(textRenderer, 5, 45, 100, 10, Text.of("")));

            stored_pehkuiscale = PCMUtils.getPehkuiScale(entity);
            PehkuiScale.setText(String.valueOf(stored_pehkuiscale));

            PehkuiScale.setChangedListener((v) -> {
                if (PostfixOperation.isNumeric(v)) {
                    stored_pehkuiscale = PCMUtils.getFloat(v);
                    ClientNetwork.send_pehkui_scale(entity, stored_pehkuiscale);
                }
            });

            if (PCMClient.PlayerSkinList.containsKey(uuid))
                SkinSuffix.setText(PCMClient.PlayerSkinList.get(uuid));
            
            SkinSuffix.setChangedListener((v) -> {
                SkinHelper.setSkinSuffix(uuid, v);
                SkinHelper.reloadSkins();
            });
        }

        // Right Panel
        listModelParts(this.width-115, 15+yscroll, entity);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= this.width-120 && mouseX < this.width) {
            yscroll += amount*10;
            if (yscroll > 0) yscroll = 0;
            else {
                for (ButtonWidget widget : scrollable_widgets) {
                    widget.y += amount*10;
                }
            }
        } else if (mouseX >= 120 && mouseX < this.width-120) {
            entityViewScale += amount*10;
            if (entityViewScale < 1) entityViewScale = 1;
        }
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float entityMouseX = 0;
        float entityMouseY = 0;

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX = (float)(this.width/2) - mouseX;
            entityMouseY = (float)(this.height/2+37-125) - mouseY;
        }

        if (entity != null)
            drawEntity(this.width/2, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, entity, PCMMain.settings.show_block_under_player_ui);

        drawColor(matrices, 0, 0, 120, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 120, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 119, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, this.width-120, 0, 120, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, this.width-120, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, this.width-121, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, 0, 0, this.width, 10, 0, 0, 0, 255);
        super.render(matrices, mouseX, mouseY, delta);
        drawColor(matrices, this.width-120, 0, this.width, 10, 0, 0, 0, 255);
    }
    @Override 
    public void close() {
        compile_nodes(uuid, selectedPartModel, false);

        yscroll = 0;
        selectedPart = -1;
        selectedNode = -1;
        selectedPartModel = null;
        nodes.clear();
        EditorGui.isDragging = false;

        super.close(); 
    }

    public void listModelParts(int x, int y, LivingEntity entity) {
        addScrollable(new OffsetFlatButtonWidget(x, y, 110, 10, Text.of((selectedPart == -2 ? "- " : "+ ") + "Root"), (btn) -> {
            selectedNode = -1;
            if (-2 == selectedPart) { 
                selectedPart = -1;
                selectedPartModel = null;
                compile_nodes(uuid, null, false);
                nodes.clear();
            } else {
                selectedPart = -2;
                selectedPartModel = null;
                decompile_script(null);
            }

            this.client.setScreen(new EditorGui(entity, entityViewScale));
        }));

        int row = 1 + showNodes(-2, 0, x, y);
        int index = 100;
        for (ModelPart part : PlatformUtils.getHeadParts(model)) {
            boolean isSelected = selectedPart == index;
            Text name = Text.of((isSelected ? "- " : "+ ") + ModelPartNames.getHeadName(entity, index-100));

            createSelectableModelPart(part, x, y, row, index, name);
            row += showNodes(index, row, x, y);

            index++;
            row++;
        }

        index = 0;
        for (ModelPart part : PlatformUtils.getBodyParts(model)) {
            boolean isSelected = selectedPart == index;
            Text name = Text.of((isSelected ? "- " : "+ ") + ModelPartNames.getBodyName(entity, index));

            createSelectableModelPart(part, x, y, row, index, name);
            row += showNodes(index, row, x, y);

            index++;
            row++;
        }
    }

    public int showNodes(int index, int row, int x, int y) {
        if (index != selectedPart) return 0;
        row += 1;

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (i == selectedNode) node.init(this, 5, 30);

            final int num = i;

            addScrollable(new FlatButtonWidget(x+10, y+((row+i)*11), 10, 10, Text.of("-"), (btn) -> {
                nodes.remove(num);

                if (nodes.size() == 0) compile_nodes(uuid, selectedPartModel, true);
                else nodes.get(0).changed = true;
                this.client.setScreen(new EditorGui(entity, entityViewScale));
            }));

            addScrollable(new NodeButtonWidget(x+20, y+((row+i)*11), 90, 10, Text.of(node.type.name().toLowerCase()), (btn) -> {
                if (num == selectedNode) { selectedNode = -1; }
                else selectedNode = num;
                
                this.client.setScreen(new EditorGui(entity, entityViewScale));
            }, (dragged, scroll) -> {
                int d = -(int)Math.round(scroll/15d);
                int move = dragged + d;
                move = (move <= -1 ? move = 0 : (move >= nodes.size() ? move = nodes.size()-1 : move));

                nodes.add(move, nodes.remove(num));
                nodes.get(move).changed = true;
                
                this.client.setScreen(new EditorGui(entity, entityViewScale));
            }));
        }

        addScrollable(new FlatButtonWidget(x+20, y+((row+nodes.size())*11), 90, 10, Text.of("+"), (btn) -> {
            this.client.setScreen(new NodeSelectGui(entity, entityViewScale, false));
        }));

        return nodes.size()+1;
    }

    public void createSelectableModelPart(final ModelPart part, int x, int y, int row, final int index, Text name) {
        addScrollable(new OffsetFlatButtonWidget(x, y+(row*11), 110, 10, name, (btn) -> {
            selectedNode = -1;
            if (index == selectedPart) {
                selectedPart = -1;
                selectedPartModel = null;
                compile_nodes(uuid, part, false);
                nodes.clear();
            } else {
                selectedPart = index;
                selectedPartModel = part;
                decompile_script(part);
            }

            this.client.setScreen(new EditorGui(entity, entityViewScale));
        }));
    }

    public void decompile_script(ModelPart part) {
        nodes.clear();

        String[] s = ClientHelper.decompile_script(uuid, part).split("\n");
        int ignore_lines = 0;
        for (String line : s) {
            if (line.trim().equals("")) continue;

            if (ignore_lines > 0) {
                ignore_lines--;
                continue;
            }
            if (line.toLowerCase().startsWith("define")) {
                String[] args = line.split(" ");
                ignore_lines = Math.round(PCMUtils.getFloat(args[1]));

                String new_line = args[2];
                if (new_line.equalsIgnoreCase("animation")) continue;
                
                for (int i = 3; i < args.length; i++) {
                    new_line += " " + args[i];
                }

                line = new_line;
            }
            nodes.add(toNode(line));
        }
    }

    public void compile_nodes(UUID uuid, ModelPart part, boolean force_change) {
        String script = "";
        boolean changed = force_change;
        for (Node node : nodes) {
            script += node.toScript() + "\n";
            if (node.changed) changed = true;
        }

        if (changed == false) return;
        ClientHelper.change_script(uuid, part, selectedPart, script.trim());
    }

    public ButtonWidget addScrollable(ButtonWidget widget) {
        scrollable_widgets.add(widget);
        return addButton(widget);
    }

    public Node toNode(String s) {
        String[] args = s.split(" ");
        return new Node(args[0], args, 1);
    }
    
}
