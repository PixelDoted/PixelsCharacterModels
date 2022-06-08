package me.pixeldots.pixelscharactermodels.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.gui.widgets.AButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NodeButtonWidget;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.Node;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.FabricUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import me.pixeldots.scriptedmodels.script.PostfixOperation;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class EditorGui extends GuiHandler {

    public TextFieldWidget PehkuiScale, SkinSuffix;

    public static int selectedPart = -1;
    public static ModelPart selectedPartModel = null;
    public static int selectedNode = -1;
    public static List<Node> nodes = new ArrayList<>();
    public static int yscroll = 0;

    public List<ButtonWidget> scrollable_widgets = new ArrayList<>();
    public UUID uuid;

    public EditorGui() {
        super("Editor");
    }

    @Override
    public void init() {
        super.init();
        LivingEntity entity = PixelsCharacterModels.minecraft.player;
        IAnimalModelMixin model = (IAnimalModelMixin)FabricUtils.getModel(entity);
        uuid = entity.getUuid();

        addButton(new ButtonWidget(5, 5, 100, 10, Text.of("compile"), (btn) -> {
            compile_nodes(uuid, selectedPartModel, true);
        }));

        if (selectedNode == -1) {
            // Pehkui Scale
            PehkuiScale = addTextField(new TextFieldWidget(textRenderer, 5, 20, 100, 10, Text.of("")));
            SkinSuffix = addTextField(new TextFieldWidget(textRenderer, 5, 35, 100, 10, Text.of("")));

            ScaleData basedata = ScaleTypes.BASE.getScaleData(this.client.player);
            PehkuiScale.setText(String.valueOf(basedata.getBaseScale()));

            PehkuiScale.setChangedListener((v) -> {
                if (PostfixOperation.isNumeric(v))
                    ScaleTypes.BASE.getScaleData(this.client.player).setScale(Float.parseFloat(v));
            });

            if (PixelsCharacterModels.PlayerSkinList.containsKey(uuid))
                SkinSuffix.setText(PixelsCharacterModels.PlayerSkinList.get(uuid));
            
            SkinSuffix.setChangedListener((v) -> {
                PixelsCharacterModels.PlayerSkinList.put(uuid, v);
                SkinHelper.reloadSkins();
            });
        }

        listModelParts(this.width-115, 5+yscroll, entity, model);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        yscroll += amount*10;
        if (yscroll > 0) yscroll = 0;
        else {
            for (ButtonWidget widget : scrollable_widgets) {
                widget.y += amount*10;
            }
        }
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int player_height = 37;
        drawEntity(this.width/2, this.height/2+player_height, 75, (float)(this.width/2) - mouseX, (float)(this.height/2+player_height-125) - mouseY, this.client.player);
        
        drawColor(matrices, 0, 0, 120, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 120, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 119, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, this.width-120, 0, 120, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, this.width-120, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, this.width-121, -1, this.height, 0, 0, 0, 255);

        super.render(matrices, mouseX, mouseY, delta);
    }
    @Override 
    public void close() {
        compile_nodes(uuid, selectedPartModel, false);

        yscroll = 0;
        selectedPart = -1;
        selectedNode = -1;
        selectedPartModel = null;
        nodes.clear();

        super.close(); 
    }

    public void listModelParts(int x, int y, LivingEntity entity, IAnimalModelMixin model) {
        addScrollable(new AButtonWidget(x, y, 110, 10, Text.of((selectedPart == -2 ? "- " : "+ ") + "Root"), (btn) -> {
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

            this.client.setScreen(new EditorGui());
        }));

        int row = 1 + showNodes(-2, 0, x, y);
        int index = 100;
        for (ModelPart part : model.getHeadParts()) {
            boolean isSelected = selectedPart == index;
            Text name = Text.of((isSelected ? "- " : "+ ") + ModelPartNames.getHeadName(entity, index-100));

            createSelectableModelPart(part, x, y, row, index, name);
            row += showNodes(index, row, x, y);

            index++;
            row++;
        }

        index = 0;
        for (ModelPart part : model.getBodyParts()) {
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
            if (i == selectedNode) node.init(this, 5, 20);

            final int num = i;

            addScrollable(new ButtonWidget(x+10, y+((row+i)*11), 10, 10, Text.of("-"), (btn) -> {
                nodes.remove(num);

                if (nodes.size() == 0) compile_nodes(uuid, selectedPartModel, true);
                else nodes.get(0).changed = true;
                this.client.setScreen(new EditorGui());
            }));

            addScrollable(new NodeButtonWidget(x+20, y+((row+i)*11), 90, 10, Text.of(node.type.name().toLowerCase()), (btn) -> {
                if (num == selectedNode) { selectedNode = -1; }
                else selectedNode = num;
                
                this.client.setScreen(new EditorGui());
            }, (dragged, scroll) -> {
                int d = -(int)Math.round(scroll/15d);
                int move = dragged + d;
                move = (move <= -1 ? move = 0 : (move >= nodes.size() ? move = nodes.size()-1 : move));

                nodes.add(move, nodes.remove(num));
                nodes.get(move).changed = true;
                
                this.client.setScreen(new EditorGui());
            }));
        }

        addScrollable(new ButtonWidget(x+20, y+((row+nodes.size())*11), 90, 10, Text.of("+"), (btn) -> {
            this.client.setScreen(new NodeSelectGui());
        }));

        return nodes.size()+1;
    }

    public void createSelectableModelPart(final ModelPart part, int x, int y, int row, final int index, Text name) {
        addScrollable(new AButtonWidget(x, y+(row*11), 110, 10, name, (btn) -> {
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

            this.client.setScreen(new EditorGui());
        }));
    }

    public void decompile_script(ModelPart part) {
        nodes.clear();

        String[] s = ClientHelper.decompile_script(uuid, part).split("\n");
        for (String line : s) {
            if (line.trim().equals("")) continue;
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