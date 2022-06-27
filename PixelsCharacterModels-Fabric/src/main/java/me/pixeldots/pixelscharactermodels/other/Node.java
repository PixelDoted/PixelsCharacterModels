package me.pixeldots.pixelscharactermodels.other;

import me.pixeldots.pixelscharactermodels.gui.handlers.EntityGuiHandler;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FloatFieldWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.IntFieldWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.TextWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.TextureWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class Node {

    public NodeType type; // the nodes Type
    public String[] args; // the node arguments
    public boolean changed = false; // has the node been modified

    public Node(NodeType t) {
        type = t;
        args = t.arguments;
    }
        
    public Node(String _type, String[] _args, int args_index) {
        type = NodeType.valueOf(_type.toUpperCase());
        args = new String[_args.length-args_index];

        for (int i = args_index; i < _args.length; i++) {
            args[i-1] = _args[i];
        }
    }

    // Node to Script
    public String toScript() {
        return type.tofunc.run(this);
    }

    // Arguments to String
    public String argsToString() {
        String out = "";
        for (String arg : args) {
            out += arg + " ";
        }
        return out.trim();
    }

    // Initialize the Nodes GUI
    public void init(EntityGuiHandler gui, int x, int y) {
        this.type.func.init(gui, x, y, this);
    }

    // create 3 GUI Buttons
    public static void threeButton(String name, GuiHandler gui, int x, int y, Node node) {
        gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y-2, name, 0xFFFFFFFF));
        FloatFieldWidget bX = new FloatFieldWidget(gui.textRendererGUI, x, y+8, 110, 10, 0);
        FloatFieldWidget bY = new FloatFieldWidget(gui.textRendererGUI, x, y+24, 110, 10, 0);
        FloatFieldWidget bZ = new FloatFieldWidget(gui.textRendererGUI, x, y+40, 110, 10, 0);
        bX.only_positive = false; bY.only_positive = false; bZ.only_positive = false;

        bX.setChangedListener((s) -> { node.args[0] = s; node.changed = true; });
        bY.setChangedListener((s) -> { node.args[1] = s; node.changed = true; });
        bZ.setChangedListener((s) -> { node.args[2] = s; node.changed = true; });

        bX.setText(node.args[0]); bY.setText(node.args[1]); bZ.setText(node.args[2]);
        gui.addTextField(bX); gui.addTextField(bY); gui.addTextField(bZ);
    }

    // create 3 GUI Buttons
    public static void threeButtonRotation(String name, GuiHandler gui, int x, int y, Node node) {
        gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y-2, name, 0xFFFFFFFF));
        FloatFieldWidget bX = new FloatFieldWidget(gui.textRendererGUI, x, y+8, 110, 10, 0);
        FloatFieldWidget bY = new FloatFieldWidget(gui.textRendererGUI, x, y+24, 110, 10, 0);
        FloatFieldWidget bZ = new FloatFieldWidget(gui.textRendererGUI, x, y+40, 110, 10, 0);
        bX.only_positive = false; bY.only_positive = false; bZ.only_positive = false;

        bX.setChangedListener((s) -> { node.args[0] = PCMUtils.RadiansOrDegressToString(bX.getNumber()); node.changed = true; });
        bY.setChangedListener((s) -> { node.args[1] = PCMUtils.RadiansOrDegressToString(bY.getNumber()); node.changed = true; });
        bZ.setChangedListener((s) -> { node.args[2] = PCMUtils.RadiansOrDegressToString(bZ.getNumber()); node.changed = true; });

        bX.setNumber(PCMUtils.RadiansOrDegress(PCMUtils.getFloat(node.args[0])));
        bY.setNumber(PCMUtils.RadiansOrDegress(PCMUtils.getFloat(node.args[1])));
        bZ.setNumber(PCMUtils.RadiansOrDegress(PCMUtils.getFloat(node.args[2])));
        gui.addTextField(bX); gui.addTextField(bY); gui.addTextField(bZ);
    }

    public enum NodeType {
        PARTICLE((gui, x, y, node) -> {
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y-2, "Type", 0xFFFFFFFF));
            TextFieldWidget type = new TextFieldWidget(gui.textRendererGUI, x, y+8, 100, 10, Text.of(""));
            
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+21, "Position", 0xFFFFFFFF));
            TextFieldWidget bx = new TextFieldWidget(gui.textRendererGUI, x, y+31, 30, 10, Text.of(""));
            TextFieldWidget by = new TextFieldWidget(gui.textRendererGUI, x+35, y+31, 30, 10, Text.of(""));
            TextFieldWidget bz = new TextFieldWidget(gui.textRendererGUI, x+69, y+31, 30, 10, Text.of(""));
           
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+45, "Velocity", 0xFFFFFFFF));
            TextFieldWidget vx = new TextFieldWidget(gui.textRendererGUI, x, y+54, 30, 10, Text.of(""));
            TextFieldWidget vy = new TextFieldWidget(gui.textRendererGUI, x+35, y+54, 30, 10, Text.of(""));
            TextFieldWidget vz = new TextFieldWidget(gui.textRendererGUI, x+69, y+54, 30, 10, Text.of(""));

            type.setChangedListener((s) -> { node.args[0] = s; node.changed = true; });
            bx.setChangedListener((s) -> { node.args[1] = s; node.changed = true; });
            by.setChangedListener((s) -> { node.args[2] = s; node.changed = true; });
            bz.setChangedListener((s) -> { node.args[3] = s; node.changed = true; });
            vx.setChangedListener((s) -> { node.args[4] = s; node.changed = true; });
            vy.setChangedListener((s) -> { node.args[5] = s; node.changed = true; });
            vz.setChangedListener((s) -> { node.args[6] = s; node.changed = true; });

            type.setText(node.args[0]); bx.setText(node.args[1]); by.setText(node.args[2]); bz.setText(node.args[3]); 
            vx.setText(node.args[4]); vy.setText(node.args[5]); vz.setText(node.args[6]);
            gui.addTextField(type); gui.addTextField(bx); gui.addTextField(by); gui.addTextField(bz); 
            gui.addTextField(vx); gui.addTextField(vy); gui.addTextField(vz);
        }, (node) -> { return "particle " + node.argsToString(); }, new String[] { "minecraft:flame", "0", "0", "0", "0", "0", "0" }),
        TRANSLATE((gui, x, y, node) -> { threeButton("Position", gui, x, y, node); }, (node) -> { return "translate " + node.argsToString(); }, new String[] { "0", "0", "0" }),
        SCALE((gui, x, y, node) -> { threeButton("Scale", gui, x, y, node); }, (node) -> { return "scale " + node.argsToString(); }, new String[] { "1", "1", "1" }),
        ROTATE((gui, x, y, node) -> { threeButtonRotation("Rotate", gui, x, y, node); }, (node) -> { return "rotate " + node.argsToString(); }, new String[] { "0", "0", "0" }),
        ANGLE((gui, x, y, node) -> { threeButtonRotation("Angle", gui, x, y, node); }, (node) -> { return "angle " + node.argsToString(); }, new String[] { "0", "0", "0" }),
        VERTEX((gui, x, y, node) -> {
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y-2, "Position", 0xFFFFFFFF));
            FloatFieldWidget fX = new FloatFieldWidget(gui.textRendererGUI, x, y+8, 32, 10, 0); fX.only_positive = false;
            FloatFieldWidget fY = new FloatFieldWidget(gui.textRendererGUI, x+37, y+8, 32, 10, 0); fY.only_positive = false;
            FloatFieldWidget fZ = new FloatFieldWidget(gui.textRendererGUI, x+75, y+8, 32, 10, 0); fZ.only_positive = false;
            
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+21, "Normal", 0xFFFFFFFF));
            FloatFieldWidget fNX = new FloatFieldWidget(gui.textRendererGUI, x, y+31, 32, 10, 0); fNX.only_positive = false;
            FloatFieldWidget fNY = new FloatFieldWidget(gui.textRendererGUI, x+37, y+31, 32, 10, 0); fNY.only_positive = false;
            FloatFieldWidget fNZ = new FloatFieldWidget(gui.textRendererGUI, x+75, y+31, 32, 10, 0); fNZ.only_positive = false;
            
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+45, "UV", 0xFFFFFFFF));
            FloatFieldWidget fU = new FloatFieldWidget(gui.textRendererGUI, x, y+54, 51, 10, 0);
            FloatFieldWidget fV = new FloatFieldWidget(gui.textRendererGUI, x+56, y+54, 51, 10, 0);
            
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+67, "Color", 0xFFFFFFFF));
            IntFieldWidget fR = new IntFieldWidget(gui.textRendererGUI, x, y+77, 27, 10, 255, 255);
            IntFieldWidget fG = new IntFieldWidget(gui.textRendererGUI, x+27, y+77, 27, 10, 255, 255);
            IntFieldWidget fB = new IntFieldWidget(gui.textRendererGUI, x+54, y+77, 27, 10, 255, 255);
            IntFieldWidget fA = new IntFieldWidget(gui.textRendererGUI, x+81, y+77, 27, 10, 255, 255);

            fX.setChangedListener((s) -> { node.args[0] = s; });
            fY.setChangedListener((s) -> { node.args[1] = s; });
            fZ.setChangedListener((s) -> { node.args[2] = s; });
            fNX.setChangedListener((s) -> { node.args[3] = s; });
            fNY.setChangedListener((s) -> { node.args[4] = s; });
            fNZ.setChangedListener((s) -> { node.args[5] = s; });
            fU.setChangedListener((s) -> { node.args[6] = s; });
            fV.setChangedListener((s) -> { node.args[7] = s; });
            fR.setChangedListener((s) -> { node.args[8] = s; });
            fG.setChangedListener((s) -> { node.args[9] = s; });
            fB.setChangedListener((s) -> { node.args[10] = s; });
            fA.setChangedListener((s) -> { node.args[11] = s; });
            
            gui.addTextField(fX); gui.addTextField(fY); gui.addTextField(fZ);
            gui.addTextField(fNX); gui.addTextField(fNY); gui.addTextField(fNZ);
            gui.addTextField(fU); gui.addTextField(fV);
            gui.addTextField(fR); gui.addTextField(fG); gui.addTextField(fB); gui.addTextField(fA);
        }, (node) -> { return "vertex " + node.argsToString(); }, new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "255", "255", "255", "255" }),
        CUBE((gui, x, y, node) -> {
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y-2, "Face", 0xFFFFFFFF));
            IntFieldWidget face = new IntFieldWidget(gui.textRendererGUI, x, y+8, 109, 10, 0, 5);
            
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+21, "UVs", 0xFFFFFFFF));
            FloatFieldWidget bU = new FloatFieldWidget(gui.textRendererGUI, x, y+31, 52, 10, 0, 64);
            FloatFieldWidget bV = new FloatFieldWidget(gui.textRendererGUI, x+57, y+31, 52, 10, 0, 64);

            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+45, "Width, Height", 0xFFFFFFFF));
            FloatFieldWidget bW = new FloatFieldWidget(gui.textRendererGUI, x, y+54, 52, 10, 64, 64);
            FloatFieldWidget bH = new FloatFieldWidget(gui.textRendererGUI, x+57, y+54, 52, 10, 64, 64);
            TextureWidget texture = new TextureWidget(x, y+67, 110, 110, gui.entity);

            bU.setChangedListener((s) -> { node.args[0+4*face.getNumber()] = String.valueOf(PCMUtils.divide(bU.getNumber(), 64f)); node.changed = true; texture.setSelectX(bU); });
            bV.setChangedListener((s) -> { node.args[1+4*face.getNumber()] = String.valueOf(PCMUtils.divide(bV.getNumber(), 64f)); node.changed = true; texture.setSelectY(bV); });
            bW.setChangedListener((s) -> { node.args[2+4*face.getNumber()] = String.valueOf(PCMUtils.divide(bW.getNumber(), 64f)); node.changed = true; texture.setSelectWidth(bW); });
            bH.setChangedListener((s) -> { node.args[3+4*face.getNumber()] = String.valueOf(PCMUtils.divide(bH.getNumber(), 64f)); node.changed = true; texture.setSelectHeight(bH); });
            
            face.setChangedListener((s) -> {
                int i = 4*face.getNumber();
                bU.setNumber(PCMUtils.getFloat(node.args[0+i])*64f); bV.setNumber(PCMUtils.getFloat(node.args[1+i])*64f);
                bW.setNumber(PCMUtils.getFloat(node.args[2+i])*64f); bH.setNumber(PCMUtils.getFloat(node.args[3+i])*64f);
            });

            bU.setNumber(PCMUtils.getFloat(node.args[0])*64f); bV.setNumber(PCMUtils.getFloat(node.args[1])*64f);
            bW.setNumber(PCMUtils.getFloat(node.args[2])*64f); bH.setNumber(PCMUtils.getFloat(node.args[3])*64f);
            gui.gui_drawables.add(texture);
            gui.addTextField(bU); gui.addTextField(bV); gui.addTextField(bW); gui.addTextField(bH); gui.addTextField(face);
        }, (node) -> { return NodeHelper.cubeToString(node); }, new String[] { "0", "0", "1", "1", "0", "0", "1", "1", "0", "0", "1", "1", "0", "0", "1", "1", "0", "0", "1", "1", "0", "0", "1", "1" }),
        MESH((gui, x, y, node) -> {
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y-2, "Mesh File Name", 0xFFFFFFFF));
            TextFieldWidget meshID = new TextFieldWidget(gui.textRendererGUI, x, y+8, 109, 10, Text.of(""));
            
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+21, "UVs", 0xFFFFFFFF));
            FloatFieldWidget bU = new FloatFieldWidget(gui.textRendererGUI, x, y+31, 30, 10, 0, 64);
            FloatFieldWidget bV = new FloatFieldWidget(gui.textRendererGUI, x+35, y+31, 30, 10, 0, 64);
            
            gui.gui_drawables.add(new TextWidget(gui.textRendererGUI, x, y+45, "Width, Height", 0xFFFFFFFF));
            FloatFieldWidget bW = new FloatFieldWidget(gui.textRendererGUI, x, y+54, 30, 10, 64, 64);
            FloatFieldWidget bH = new FloatFieldWidget(gui.textRendererGUI, x+35, y+54, 30, 10, 64, 64);
            TextureWidget texture = new TextureWidget(x, y+67, 110, 110, gui.entity);

            meshID.setChangedListener((s) -> { node.args[0] = s; node.changed = true; });
            bU.setChangedListener((s) -> { node.args[1] = String.valueOf(PCMUtils.divide(bU.getNumber(), 64f)); node.changed = true; texture.setSelectX(bU); });
            bV.setChangedListener((s) -> { node.args[2] = String.valueOf(PCMUtils.divide(bV.getNumber(), 64f)); node.changed = true; texture.setSelectY(bV); });
            bW.setChangedListener((s) -> { node.args[3] = String.valueOf(PCMUtils.divide(bW.getNumber(), 64f)); node.changed = true; texture.setSelectWidth(bW); });
            bH.setChangedListener((s) -> { node.args[4] = String.valueOf(PCMUtils.divide(bH.getNumber(), 64f)); node.changed = true; texture.setSelectHeight(bH); });
            
            meshID.setText(node.args[0]);
            bU.setNumber(PCMUtils.getFloat(node.args[1])*64f); bV.setNumber(PCMUtils.getFloat(node.args[2])*64f);
            bW.setNumber(PCMUtils.getFloat(node.args[3])*64f); bH.setNumber(PCMUtils.getFloat(node.args[4])*64f);
            gui.gui_drawables.add(texture);
            gui.addTextField(meshID); gui.addTextField(bU); gui.addTextField(bV); gui.addTextField(bW); gui.addTextField(bH);
        }, (node) -> { return NodeHelper.meshToString(node); }, new String[] { "", "0", "0", "1", "1" }),
        CANCEL((gui, x, y, node) -> {}, (node) -> { return "cancel"; }, new String[] {});

        NodeFunc func;
        ScriptFunc tofunc;
        public String[] arguments;

        private NodeType(NodeFunc _func, ScriptFunc _tofunc, String[] _arguments) {
            this.func = _func;
            this.tofunc = _tofunc;
            this.arguments = _arguments;
        }
    }

    public interface NodeFunc {
        void init(EntityGuiHandler gui, int x, int y, Node node);
    }

    public interface ScriptFunc {
        String run(Node node);
    }
}
