package me.pixeldots.pixelscharactermodels.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.gui.handlers.EntityGuiHandler;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.FloatFieldWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NodeButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.OffsetFlatButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.TextWidget;
import me.pixeldots.pixelscharactermodels.network.ClientNetwork;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.Node;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class EditorGui extends EntityGuiHandler {

    public FloatFieldWidget PehkuiScale;
    public EditBox SkinSuffix;

    public static int selectedPart = -1;
    public static ModelPart selectedPartModel = null;
    public static int selectedNode = -1;
    public static List<Node> nodes = new ArrayList<>();
    public static int yscroll = 0;
    public static boolean isDragging = false;

    public List<Button> scrollable_widgets = new ArrayList<>();
    public float stored_pehkuiscale = 1;
    public float entityViewScale = 75;
    
    public EditorGui(LivingEntity _entity) {
        super("Editor");
        entity = _entity;
        model = PlatformUtils.getModel(_entity);
        uuid = _entity.getUUID();
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
        this.minecraft.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();
        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text("pcm.menu.Presets"), (btn) -> {
            setScreen(new PresetsGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text("pcm.menu.Editor"), (btn) -> {})).active = false;
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text("pcm.menu.Animation"), (btn) -> {
            setScreen(new AnimationGui(entity, this.entityViewScale, this.entityRotation));
        }));
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text("pcm.menu.Settings"), (btn) -> {
            setScreen(new SettingsGui(entity, this.entityViewScale));
        }));

        // Left Panel
        addButton(new FlatButtonWidget(5, 15, 110, 10, Text("pcm.gui.Compile"), (btn) -> {
            compile_nodes(uuid, selectedPartModel, true);
        }));

        if (selectedNode == -1) {
            // Pehkui Scale
            this.gui_drawables.add(new TextWidget(textRenderer, 5, 29, Text("pcm.gui.EntityScale"), 0xFFFFFFFF));
            PehkuiScale = (FloatFieldWidget)addTextField(new FloatFieldWidget(textRenderer, 5, 39, 110, 10, 1));
            
            this.gui_drawables.add(new TextWidget(textRenderer, 5, 54, Text("pcm.gui.SkinSuffix"), 0xFFFFFFFF));
            SkinSuffix = addTextField(new EditBox(textRenderer, 5, 63, 110, 10, Component.literal("")));

            stored_pehkuiscale = PCMUtils.getPehkuiScale(entity);
            PehkuiScale.setNumber(stored_pehkuiscale);

            PehkuiScale.setResponder((v) -> {
                stored_pehkuiscale = PehkuiScale.getNumber();
                ClientNetwork.send_pehkui_scale(entity, stored_pehkuiscale);
            });

            if (PCMClient.PlayerSkinList.containsKey(uuid)) {
                SkinSuffix.setValue(PCMClient.PlayerSkinList.get(uuid));
                System.out.println(PCMClient.PlayerSkinList.get(uuid));
            }
            
            SkinSuffix.setResponder((v) -> {
                ClientNetwork.send_skin_suffix(uuid, v);
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
                for (Button widget : scrollable_widgets) {
                    widget.y += amount*10;
                    widget.visible = !(widget.y < 0);
                }
            }
        } else if (mouseX >= 120 && mouseX < this.width-120) {
            entityViewScale += amount*10;
            if (entityViewScale < 1) entityViewScale = 1;
        }
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        /*float entityMouseX = 0;
        float entityMouseY = 0;

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX = (float)(this.width/2) - mouseX;
            entityMouseY = (float)(this.height/2+37-125) - mouseY;
        }*/

        drawEntity(this.width/2, this.height/2+37, 0, 0);
        
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
    public void onClose() {
        compile_nodes(uuid, selectedPartModel, false);

        yscroll = 0;
        selectedPart = -1;
        selectedNode = -1;
        selectedPartModel = null;
        nodes.clear();
        EditorGui.isDragging = false;

        super.onClose(); 
    }

    public void listModelParts(int x, int y, LivingEntity entity) {
        Button btn_widget = addScrollable(new OffsetFlatButtonWidget(x, y, 110, 10, Component.literal((selectedPart == -2 ? "- " : "+ ") + String("pcm.entity.Root")), (btn) -> {
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

            this.minecraft.setScreen(new EditorGui(entity, entityViewScale));
        }));
        btn_widget.visible = !(btn_widget.y < 0);

        int row = 1 + showNodes(-2, 0, x, y);
        int index = 100;
        for (ModelPart part : PlatformUtils.getHeadParts(model)) {
            boolean isSelected = selectedPart == index;
            Component name = Component.literal((isSelected ? "- " : "+ ") + String("pcm.entity." + ModelPartNames.getHeadName(entity, index-100)));

            Button widget = createSelectableModelPart(part, x, y, row, index, name);
            row += showNodes(index, row, x, y);
            widget.visible = !(widget.y < 0);

            index++;
            row++;
        }

        index = 0;
        for (ModelPart part : PlatformUtils.getBodyParts(model)) {
            boolean isSelected = selectedPart == index;
            Component name = Component.literal((isSelected ? "- " : "+ ") + String("pcm.entity." + ModelPartNames.getBodyName(entity, index)));

            Button widget = createSelectableModelPart(part, x, y, row, index, name);
            row += showNodes(index, row, x, y);
            widget.visible = !(widget.y < 0);

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
            Button B = addScrollable(new NodeButtonWidget(x+20, y+((row+i)*11), 90, 10, Component.literal(node.type.name().toLowerCase()), (btn) -> {
                if (num == selectedNode) { selectedNode = -1; }
                else selectedNode = num;
                
                this.minecraft.setScreen(new EditorGui(entity, entityViewScale));
            }, (dragged, scroll) -> {
                int d = -(int)Math.round(scroll/15d);
                int move = (dragged + d)+num;
                move = (move <= -1 ? move = 0 : (move >= nodes.size() ? move = nodes.size()-1 : move));

                nodes.add(move, nodes.remove(num));
                nodes.get(move).changed = true;
                
                this.minecraft.setScreen(new EditorGui(entity, entityViewScale));
            }));
            B.visible = !(B.y < 0);

            Button A = addScrollable(new FlatButtonWidget(x+10, y+((row+i)*11), 10, 10, Component.literal("-"), (btn) -> {
                nodes.remove(num);

                if (nodes.size() == 0) compile_nodes(uuid, selectedPartModel, true);
                else nodes.get(0).changed = true;
                this.minecraft.setScreen(new EditorGui(entity, entityViewScale));
            }, this, TextArray("pcm.gui.Remove")));
            A.visible = !(A.y < 0);
        }

        Button widget = addScrollable(new FlatButtonWidget(x+20, y+((row+nodes.size())*11), 90, 10, Component.literal("+"), (btn) -> {
            this.minecraft.setScreen(new NodeSelectGui(entity, entityViewScale, this.entityRotation, false));
        }));
        widget.visible = !(widget.y < 0);

        return nodes.size()+1;
    }

    public Button createSelectableModelPart(final ModelPart part, int x, int y, int row, final int index, Component name) {
        return addScrollable(new OffsetFlatButtonWidget(x, y+(row*11), 110, 10, name, (btn) -> {
            selectedNode = -1;
            if (index == selectedPart) {
                compile_nodes(uuid, part, false);
                selectedPart = -1;
                selectedPartModel = null;
                nodes.clear();
            } else {
                selectedPart = index;
                selectedPartModel = part;
                decompile_script(part);
            }

            this.minecraft.setScreen(new EditorGui(entity, entityViewScale));
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

    public Button addScrollable(Button widget) {
        scrollable_widgets.add(widget);
        return addButton(widget);
    }

    public Node toNode(String s) {
        String[] args = s.split(" ");
        return new Node(args[0], args, 1);
    }
    
}
