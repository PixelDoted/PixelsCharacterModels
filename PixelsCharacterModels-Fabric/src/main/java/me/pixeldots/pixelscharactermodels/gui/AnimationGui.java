package me.pixeldots.pixelscharactermodels.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.AnimationFile;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.gui.widgets.AButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NodeButtonWidget;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.Node;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class AnimationGui extends GuiHandler {

    public static AnimationFile animation = new AnimationFile();
    public static int selectedPart = -1;
    public static String selectedPartName = "";
    public static int selectedNode = -1;
    public static List<Node> nodes = new ArrayList<>();
    public static int yscroll = 0;
    public static boolean isDragging = false;

    public List<ButtonWidget> scrollable_widgets = new ArrayList<>();
    public UUID uuid;
    public float stored_pehkuiscale = 1;
    public float entityViewScale = 75;
    public String path_offset = "";

    public LivingEntity entity;
    public IAnimalModelMixin model;
    
    public AnimationGui(LivingEntity _entity) {
        super("Animation");
        entity = _entity;
        model = (IAnimalModelMixin)PlatformUtils.getModel(_entity);
        uuid = _entity.getUuid();
    }

    public AnimationGui(LivingEntity _entity, float _entityViewScale) {
        this(_entity);
        entityViewScale = _entityViewScale;
    }

    public void setScreen(GuiHandler gui) {
        this.client.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();
        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text.of("Presets"), (btn) -> {
            setScreen(new PresetsGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text.of("Editor"), (btn) -> {
            setScreen(new EditorGui(entity, entityViewScale));
        }));
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text.of("Animation"), (btn) -> {}));
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text.of("Settings"), (btn) -> {
            setScreen(new SettingsGui(entity, this.entityViewScale));
        }));

        // Left Panel
        if (selectedNode == -1) {
            File[] files = new File(this.client.runDirectory.getAbsolutePath() + "/PCM/Animations" + path_offset).listFiles();
            for (int i = 0; i < files.length; i++) {
                final File file = files[i];

                ButtonWidget widget = addButton(new ButtonWidget(15, 15+(i*10)+yscroll, 100, 10, Text.of((file.isDirectory() ? "~" : "") + file.getName().replace(".json", "")), (btn) -> {
                    if (file.isDirectory()) path_offset += "/" + file.getName();
                    else selectAnimation(file);
                }));
                ButtonWidget save_widget = addButton(new ButtonWidget(5, 15+(i*10)+yscroll, 10, 10, Text.of("S"), (btn) -> {
                    boolean result = AnimationHelper.write(file, animation);
                    if (result == false)
                        this.client.player.sendMessage(Text.of("File \"" + file.getAbsolutePath() + "\" could not be saved"), false);
                }));

                widget.visible = !(widget.y < 10);
                addButton(widget); addButton(save_widget);
                //presetButtons.add(widget); presetButtons.add(save_widget);
            }

            TextFieldWidget createname = addTextField(new TextFieldWidget(textRenderer, 5, this.height-30, 110, 10, Text.of("")));
            addButton(new ButtonWidget(5, this.height-15, 110, 10, Text.of("create"), (btn) -> {
                File file = new File(this.client.runDirectory.getAbsolutePath() + "/PCM/Animations" + path_offset + "/" + createname.getText() + ".json");
                boolean result = AnimationHelper.write(file, animation);
                if (result == false)
                    this.client.player.sendMessage(Text.of("File \"" + file.getAbsolutePath() + "\" could not be created"), false);

                this.client.setScreen(new AnimationGui(entity, entityViewScale));
            }));
        }

        // Right Panel
        listModelParts(this.width-115, 15+yscroll, entity, model);

        // TODO: Bottom Panel
        
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
        float entityMouseX = (float)(this.width/2);
        float entityMouseY = (float)(this.height/2-128);

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX -= mouseX;
            entityMouseY -= mouseY;
        } else {
            entityMouseX -= this.width/2-13.5f;
            entityMouseY -= this.height/2+80;
        }

        if (entity != null) {
            if (PCMMain.settings.show_block_under_player_ui) {
                drawEntityOnBlock(this.width/2, this.height/2-3, Math.round(entityViewScale)-10, entityMouseX, entityMouseY, entity);
            } else {
                drawEntity(this.width/2, this.height/2-3, Math.round(entityViewScale)-10, entityMouseX, entityMouseY, entity);
            }
        }

        drawColor(matrices, 120, this.height-80, this.width-240, 80, 0, 4, 17, 222);
        drawHorizontalLine(matrices, 120, this.width-120, this.height-80, 0, 0, 0, 255);
        drawHorizontalLine(matrices, 120, this.width-120, this.height-79, 0, 0, 0, 255);

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
        compile_nodes(uuid, false);

        yscroll = 0;
        selectedPart = -1;
        selectedNode = -1;
        selectedPartName = null;
        nodes.clear();
        AnimationGui.isDragging = false;

        super.close(); 
    }

    public void listModelParts(int x, int y, LivingEntity entity, IAnimalModelMixin model) {
        addScrollable(new AButtonWidget(x, y, 110, 10, Text.of((selectedPart == -2 ? "- " : "+ ") + "Root"), (btn) -> {
            selectedNode = -1;
            if (-2 == selectedPart) { 
                selectedPart = -1;
                selectedPartName = null;
                compile_nodes(uuid, false);
                nodes.clear();
            } else {
                selectedPart = -2;
                selectedPartName = null;
                decompile_script(null);
            }

            this.client.setScreen(new AnimationGui(entity, entityViewScale));
        }));

        int row = 1 + showNodes(-2, 0, x, y);
        int index = 100;
        for (ModelPart part : model.getHeadParts()) {
            boolean isSelected = selectedPart == index;
            String partName = ModelPartNames.getHeadName(entity, index-100);
            Text name = Text.of((isSelected ? "- " : "+ ") + partName);

            createSelectableModelPart(partName, x, y, row, index, name);
            row += showNodes(index, row, x, y);

            index++;
            row++;
        }

        index = 0;
        for (ModelPart part : model.getBodyParts()) {
            boolean isSelected = selectedPart == index;
            String partName = ModelPartNames.getBodyName(entity, index);
            Text name = Text.of((isSelected ? "- " : "+ ") + partName);

            createSelectableModelPart(partName, x, y, row, index, name);
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

            addScrollable(new ButtonWidget(x+10, y+((row+i)*11), 10, 10, Text.of("-"), (btn) -> {
                nodes.remove(num);

                if (nodes.size() == 0) compile_nodes(uuid, true);
                else nodes.get(0).changed = true;
                this.client.setScreen(new AnimationGui(entity, entityViewScale));
            }));

            addScrollable(new NodeButtonWidget(x+20, y+((row+i)*11), 90, 10, Text.of(node.type.name().toLowerCase()), (btn) -> {
                if (num == selectedNode) { selectedNode = -1; }
                else selectedNode = num;
                
                this.client.setScreen(new AnimationGui(entity, entityViewScale));
            }, (dragged, scroll) -> {
                int d = -(int)Math.round(scroll/15d);
                int move = dragged + d;
                move = (move <= -1 ? move = 0 : (move >= nodes.size() ? move = nodes.size()-1 : move));

                nodes.add(move, nodes.remove(num));
                nodes.get(move).changed = true;
                
                this.client.setScreen(new AnimationGui(entity, entityViewScale));
            }));
        }

        addScrollable(new ButtonWidget(x+20, y+((row+nodes.size())*11), 90, 10, Text.of("+"), (btn) -> {
            this.client.setScreen(new NodeSelectGui(entity, entityViewScale, true));
        }));

        return nodes.size()+1;
    }

    public void createSelectableModelPart(final String partName, int x, int y, int row, final int index, Text name) {
        addScrollable(new AButtonWidget(x, y+(row*11), 110, 10, name, (btn) -> {
            selectedNode = -1;
            if (index == selectedPart) {
                selectedPart = -1;
                selectedPartName = null;
                compile_nodes(uuid, false);
                nodes.clear();
            } else {
                selectedPart = index;
                selectedPartName = partName.toLowerCase();
                decompile_script(selectedPartName);
            }

            this.client.setScreen(new AnimationGui(entity, entityViewScale));
        }));
    }

    public void decompile_script(String part_name) {
        nodes.clear();

        String[] s;
        AnimationFile.Frame frame = animation.frames.get(0);
        if (frame.parts.containsKey(part_name))
            s = frame.parts.get(part_name).split("\n");
        else s = frame.script.split("\n");

        int ignore_lines = 0;
        for (String line : s) {
            if (line.trim().equals("")) continue;

            if (ignore_lines > 0) {
                ignore_lines--;
                continue;
            }
            if (line.toLowerCase().startsWith("define")) {
                String[] args = line.split(" ");
                ignore_lines = Math.round(Float.parseFloat(args[1]));

                String new_line = args[2];
                for (int i = 3; i < args.length; i++) {
                    new_line += " " + args[i];
                }

                line = new_line;
            }
            nodes.add(toNode(line));
        }
    }

    public void compile_nodes(UUID uuid, boolean force_change) {
        String script = "";
        boolean changed = force_change;
        for (Node node : nodes) {
            script += node.toScript() + "\n";
            if (node.changed) changed = true;
        }

        if (changed == false) return;

        String part_name = selectedPartName;
        AnimationFile.Frame frame = animation.frames.get(0);
        if (frame.parts.containsKey(part_name))
            frame.parts.put(part_name, script);
        else frame.script = script;
    }

    public ButtonWidget addScrollable(ButtonWidget widget) {
        scrollable_widgets.add(widget);
        return addButton(widget);
    }

    public Node toNode(String s) {
        String[] args = s.split(" ");
        return new Node(args[0], args, 1);
    }

    public void selectAnimation(File file) {
        AnimationFile anim = AnimationHelper.read(file);
        if (anim == null)
            this.client.player.sendMessage(Text.of("File \"" + file.getAbsolutePath() + "\" could not be loaded"), false);

        animation = anim;
    }
    
}
