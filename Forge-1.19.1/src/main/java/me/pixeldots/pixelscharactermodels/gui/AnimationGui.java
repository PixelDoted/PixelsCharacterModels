package me.pixeldots.pixelscharactermodels.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;

import me.pixeldots.pixelscharactermodels.PCMFileSystem;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.AnimationFile;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.gui.handlers.EntityGuiHandler;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NodeButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.IntFieldWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.OffsetFlatButtonWidget;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.Node;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class AnimationGui extends EntityGuiHandler {

    public static AnimationFile animation;
    public static File animation_file;
    public static int frame_index_value;

    public static int selectedPart = -1;
    public static String selectedPartName = "";
    public static int selectedNode = -1;
    public static List<Node> nodes = new ArrayList<>();
    public static int yscroll = 0;
    public static int animation_yscroll = 0;
    public static boolean isDragging = false;
    public static String path_offset = "";

    public List<Button> scrollable_widgets = new ArrayList<>();
    public List<Button> animation_widgets = new ArrayList<>();
    public float stored_pehkuiscale = 1;
    public float entityViewScale = 75;
    
    public AnimationGui(LivingEntity _entity) {
        super("Animation");
        entity = _entity;
        model = PlatformUtils.getModel(_entity);
        uuid = _entity.getUUID();
    }

    public AnimationGui(LivingEntity _entity, float _entityViewScale) {
        this(_entity);
        entityViewScale = _entityViewScale;
    }

    public void setScreen(GuiHandler gui) {
        compile_nodes(uuid, false);

        yscroll = 0;
        selectedPart = -1;
        selectedNode = -1;
        selectedPartName = "";
        nodes.clear();
        path_offset = "";
        AnimationGui.isDragging = false;

        animation = null;
        animation_file = null;
        frame_index_value = 0;
        this.minecraft.setScreen(gui);
    }

    @Override
    public void init() {
        if (animation == null) animation = new AnimationFile();
        super.init();
        
        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text("pcm.menu.Presets"), (btn) -> {
            setScreen(new PresetsGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text("pcm.menu.Editor"), (btn) -> {
            setScreen(new EditorGui(entity, entityViewScale));
        }));
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text("pcm.menu.Animation"), (btn) -> {})).active = false;
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text("pcm.menu.Settings"), (btn) -> {
            setScreen(new SettingsGui(entity, this.entityViewScale));
        }));

        // Left Panel
        if (selectedNode == -1) {
            File[] files = new File(this.minecraft.gameDirectory.getAbsolutePath() + File.separator + PCMFileSystem.Animations_Path + path_offset).listFiles();
            for (int i = 0; i < files.length; i++) {
                final File file = files[i];

                Button widget = addButton(new FlatButtonWidget(15, 15+(i*10)+animation_yscroll, 100, 10, Component.literal((file.isDirectory() ? "~" : "") + file.getName().replace(".json", "")), (btn) -> {
                    if (file.isDirectory()) path_offset += "/" + file.getName();
                    else selectAnimation(file);

                    this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
                }));
                Button save_widget = addButton(new FlatButtonWidget(5, 15+(i*10)+animation_yscroll, 10, 10, Component.literal("S"), (btn) -> {
                    boolean result = AnimationHelper.write(file, animation);
                    if (result == false)
                        this.minecraft.player.displayClientMessage(Component.literal("File \"" + file.getAbsolutePath() + "\" could not be saved"), false);
                }, this, Arrays.asList(Text("pcm.gui.Save")) ));

                widget.visible = !(widget.y < 10);
                save_widget.visible = !(save_widget.y < 10);
                animation_widgets.add(widget); animation_widgets.add(save_widget);
            }

            EditBox createname = addTextField(new EditBox(textRenderer, 125, 60, 55, 10, Component.literal("")));
            addButton(new FlatButtonWidget(125, 45, 55, 10, Text("pcm.gui.Create"), (btn) -> {
                File file = new File(this.minecraft.gameDirectory.getAbsolutePath() + File.separator + PCMFileSystem.Animations_Path + path_offset + File.separator + createname.getValue() + ".json");
                boolean result = AnimationHelper.write(file, animation);
                if (result == false)
                    this.minecraft.player.displayClientMessage(Component.literal("File \"" + file.getAbsolutePath() + "\" could not be created"), false);

                animation_file = file;
                this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
            }));
            addButton(new FlatButtonWidget(125, 35, 55, 10, Text("pcm.gui.Rename"), (btn) -> {
                if (animation_file != null && !animation_file.exists()) return;
                String new_name = createname.getValue();
                String new_path = animation_file.getAbsolutePath().replace(animation_file.getName(), new_name + (!new_name.endsWith(".json") ? ".json" : ""));
    
                File new_file = new File(new_path);
                animation_file.renameTo(new_file);
                animation_file = new_file;
                this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
            }));
    
            addButton(new FlatButtonWidget(125, 15, 55, 10, Text("pcm.gui.Delete"), (btn) -> {
                if (animation_file != null && !animation_file.exists()) return;
                animation_file.delete();
                
                animation_file = null;
                this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
            }));
        } else {
            addButton(new FlatButtonWidget(5, 15, 110, 10, Text("pcm.gui.Save"), (btn) -> {
                compile_nodes(uuid, true);
                boolean result = AnimationHelper.write(animation_file, animation);
                if (result == false)
                    this.minecraft.player.displayClientMessage(Component.literal("File \"" + animation_file.getAbsolutePath() + "\" could not be saved"), false);
            }));
        }

        // Right Panel
        listModelParts(this.width-115, 15+yscroll, entity);

        // Bottom Panel
        IntFieldWidget framerate = new IntFieldWidget(textRenderer, 190, this.height-65, 40, 10, frame_index_value); addTextField(framerate);
        framerate.setResponder((s) -> {
            animation.framerate = framerate.getNumber();
        });
        framerate.setNumber(animation.framerate);
        
        IntFieldWidget frame_index = new IntFieldWidget(textRenderer, 125+125, this.height-65, 25, 10, frame_index_value); addTextField(frame_index);
        addButton(new FlatButtonWidget(155+125, this.height-65, 30, 10, Text("pcm.gui.Add"), (btn) -> {
            animation.frames.add(animation.frames.get(animation.frames.size()-1));
            frame_index_value = animation.frames.size()-1;
            this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
        }));
        addButton(new FlatButtonWidget(190+125, this.height-65, 50, 10, Text("pcm.gui.Remove"), (btn) -> {
            animation.frames.remove(frame_index_value);
            frame_index_value--;
            this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
        }));

        IntFieldWidget frame_count = new IntFieldWidget(textRenderer, 125+125, this.height-40, 40, 10, animation.frames.get(frame_index_value).run_frame); addTextField(frame_count);

        frame_index.setResponder((s) -> {
            frame_index_value = Math.round(frame_index.value);
            if (animation.frames.size() <= frame_index_value)
                frame_index_value = animation.frames.size()-1;

            this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
        });
        frame_count.setResponder((s) -> {
            animation.frames.get(frame_index_value).run_frame = Math.round(frame_count.value);
        });
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
        } else if (mouseX <= 120) {
            animation_yscroll += amount*10;
            if (animation_yscroll > 0) animation_yscroll = 0;
            else {
                for (Button widget : animation_widgets) {
                    widget.y += amount*10;
                    widget.visible = !(widget.y < 10);
                }
            }
        } else if (mouseX >= 185 && mouseX < this.width-120 && mouseY < this.height-80) {
            entityViewScale += amount*10;
            if (entityViewScale < 1) entityViewScale = 1;
        }
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        float entityMouseX = 0;
        float entityMouseY = 0;

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX = (float)(this.width/2+32) - mouseX;
            entityMouseY = (float)(this.height/2+37-128) - mouseY;
        }

        if (entity != null)
            drawEntity(this.width/2+32, this.height/2-3, Math.round(entityViewScale)-10, entityMouseX, entityMouseY, entity, PCMMain.settings.show_block_under_player_ui);

        drawColor(matrices, 185, this.height-80, this.width-240, 80, 0, 4, 17, 222);
        drawHorizontalLine(matrices, 185, this.width-120, this.height-80, 0, 0, 0, 255);
        drawHorizontalLine(matrices, 185, this.width-120, this.height-79, 0, 0, 0, 255);

        drawColor(matrices, 0, 0, 185, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 185, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 184, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 119, 15, this.height-6, 0, 0, 0, 188);
        drawVerticalLine(matrices, 120, 15, this.height-6, 0, 0, 0, 188);

        drawColor(matrices, this.width-120, 0, 120, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, this.width-120, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, this.width-121, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, 0, 0, this.width, 10, 0, 0, 0, 255);

        drawString(matrices, Text("pcm.gui.Framerate"), 190, this.height-80);
        drawString(matrices, Text("Frame Index"), 250, this.height-80);
        drawString(matrices, Text("Delay Frames"), 250, this.height-55);

        super.render(matrices, mouseX, mouseY, delta);
        drawColor(matrices, this.width-120, 0, this.width, 10, 0, 0, 0, 255);
    }
    @Override 
    public void onClose() {
        compile_nodes(uuid, false);

        yscroll = 0;
        selectedPart = -1;
        selectedNode = -1;
        selectedPartName = "";
        nodes.clear();
        AnimationGui.isDragging = false;

        animation = null;
        animation_file = null;
        frame_index_value = 0;

        super.onClose(); 
    }

    public void listModelParts(int x, int y, LivingEntity entity) {
        addScrollable(new OffsetFlatButtonWidget(x, y, 110, 10, Component.literal((selectedPart == -2 ? "- " : "+ ") + String("pcm.entity.Root")), (btn) -> {
            selectedNode = -1;
            if (-2 == selectedPart) { 
                compile_nodes(uuid, false);
                selectedPart = -1;
                selectedPartName = "";
                nodes.clear();
            } else {
                selectedPart = -2;
                selectedPartName = "";
                decompile_script();
            }

            this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
        }));

        int row = 1 + showNodes(-2, 0, x, y);
        int index = 100;
        for (ModelPart part : PlatformUtils.getHeadParts(model)) {
            boolean isSelected = selectedPart == index;
            String partName = ModelPartNames.getHeadName(entity, index-100);
            Component name = Component.literal((isSelected ? "- " : "+ ") + String("pcm.entity." + partName));

            createSelectableModelPart(partName, x, y, row, index, name);
            row += showNodes(index, row, x, y);

            index++;
            row++;
        }

        index = 0;
        for (ModelPart part : PlatformUtils.getBodyParts(model)) {
            boolean isSelected = selectedPart == index;
            String partName = ModelPartNames.getBodyName(entity, index);
            Component name = Component.literal((isSelected ? "- " : "+ ") + String("pcm.entity." + partName));

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
            Button B = addScrollable(new NodeButtonWidget(x+20, y+((row+i)*11), 90, 10, Component.literal(node.type.name().toLowerCase()), (btn) -> {
                if (num == selectedNode) { selectedNode = -1; }
                else selectedNode = num;
                
                this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
            }, (dragged, scroll) -> {
                int d = -(int)Math.round(scroll/15d);
                int move = (dragged + d)+num;
                move = (move <= -1 ? move = 0 : (move >= nodes.size() ? move = nodes.size()-1 : move));

                nodes.add(move, nodes.remove(num));
                nodes.get(move).changed = true;
                
                this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
            }));
            B.visible = !(B.y < 0);

            Button A = addScrollable(new FlatButtonWidget(x+10, y+((row+i)*11), 10, 10, Component.literal("-"), (btn) -> {
                nodes.remove(num);

                if (nodes.size() == 0) compile_nodes(uuid, true);
                else nodes.get(0).changed = true;
                this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
            }, this, TextArray("pcm.gui.Remove")));
            A.visible = !(A.y < 0);
        }

        addScrollable(new FlatButtonWidget(x+20, y+((row+nodes.size())*11), 90, 10, Component.literal("+"), (btn) -> {
            this.minecraft.setScreen(new NodeSelectGui(entity, entityViewScale, true));
        }));

        return nodes.size()+1;
    }

    public void createSelectableModelPart(final String partName, int x, int y, int row, final int index, Component name) {
        addScrollable(new OffsetFlatButtonWidget(x, y+(row*11), 110, 10, name, (btn) -> {
            selectedNode = -1;
            if (index == selectedPart) {
                compile_nodes(uuid, false);
                selectedPart = -1;
                selectedPartName = "";
                nodes.clear();
            } else {
                selectedPart = index;
                selectedPartName = partName.toLowerCase();
                decompile_script();
            }

            this.minecraft.setScreen(new AnimationGui(entity, entityViewScale));
        }));
    }

    public void decompile_script() {
        nodes.clear();

        String[] s;
        AnimationFile.Frame frame = animation.frames.get(frame_index_value);
        if (selectedPartName.equals("")) s = frame.script.split("\n");
        else if (frame.parts.containsKey(selectedPartName)) 
            s = frame.parts.get(selectedPartName).split("\n");
        else return;

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

        AnimationFile.Frame frame = animation.frames.get(frame_index_value);
        if (!selectedPartName.equals("")) frame.parts.put(selectedPartName, script);
        else frame.script = script;
    }

    public Button addScrollable(Button widget) {
        scrollable_widgets.add(widget);
        return addButton(widget);
    }

    public Node toNode(String s) {
        String[] args = s.split(" ");
        return new Node(args[0], args, 1);
    }

    public void selectAnimation(File file) {
        AnimationFile anim = AnimationHelper.read(file, false);
        if (anim == null)
            this.minecraft.player.displayClientMessage(Component.literal("File \"" + file.getAbsolutePath() + "\" could not be loaded"), false);

        animation = anim;
        animation_file = file;
    }
    
}
