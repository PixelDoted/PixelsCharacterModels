package me.pixeldots.pixelscharactermodels.other;

import me.pixeldots.pixelscharactermodels.gui.handlers.EntityGuiHandler;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FloatFieldWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.IntFieldWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.TextureWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class Node {

    public NodeType type; // the nodes Type
    public String[] args; // the node arguments
    public boolean changed = false; // has the node been modified

    public Node(NodeType t) {
        type = t;
        args = new String[t.argument_count];
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
    public static void threeButton(GuiHandler gui, int x, int y, Node node) {
        FloatFieldWidget bX = new FloatFieldWidget(gui.textRendererGUI, x, y, 110, 10, 0);
        FloatFieldWidget bY = new FloatFieldWidget(gui.textRendererGUI, x, y+15, 110, 10, 0);
        FloatFieldWidget bZ = new FloatFieldWidget(gui.textRendererGUI, x, y+30, 110, 10, 0);
        bX.only_positive = false; bY.only_positive = false; bZ.only_positive = false;

        bX.setChangedListener((s) -> { node.args[0] = s; node.changed = true; });
        bY.setChangedListener((s) -> { node.args[1] = s; node.changed = true; });
        bZ.setChangedListener((s) -> { node.args[2] = s; node.changed = true; });

        bX.setText(node.args[0]); bY.setText(node.args[1]); bZ.setText(node.args[2]);
        gui.addTextField(bX); gui.addTextField(bY); gui.addTextField(bZ);
    }

    // create 3 GUI Buttons
    public static void threeButtonRotation(GuiHandler gui, int x, int y, Node node) {
        FloatFieldWidget bX = new FloatFieldWidget(gui.textRendererGUI, x, y, 110, 10, 0);
        FloatFieldWidget bY = new FloatFieldWidget(gui.textRendererGUI, x, y+15, 110, 10, 0);
        FloatFieldWidget bZ = new FloatFieldWidget(gui.textRendererGUI, x, y+30, 110, 10, 0);
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
            TextFieldWidget type = new TextFieldWidget(gui.textRendererGUI, x, y, 100, 10, Text.of(""));
            TextFieldWidget bx = new TextFieldWidget(gui.textRendererGUI, x, y+15, 30, 10, Text.of(""));
            TextFieldWidget by = new TextFieldWidget(gui.textRendererGUI, x+35, y+15, 30, 10, Text.of(""));
            TextFieldWidget bz = new TextFieldWidget(gui.textRendererGUI, x+69, y+15, 30, 10, Text.of(""));
            TextFieldWidget vx = new TextFieldWidget(gui.textRendererGUI, x, y+30, 30, 10, Text.of(""));
            TextFieldWidget vy = new TextFieldWidget(gui.textRendererGUI, x+35, y+30, 30, 10, Text.of(""));
            TextFieldWidget vz = new TextFieldWidget(gui.textRendererGUI, x+69, y+30, 30, 10, Text.of(""));

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
        }, (node) -> { return "particle " + node.argsToString(); }, 7),
        TRANSLATE((gui, x, y, node) -> { threeButton(gui, x, y, node); }, (node) -> { return "translate " + node.argsToString(); }, 3),
        SCALE((gui, x, y, node) -> { threeButton(gui, x, y, node); }, (node) -> { return "scale " + node.argsToString(); }, 3),
        ROTATE((gui, x, y, node) -> { threeButtonRotation(gui, x, y, node); }, (node) -> { return "rotate " + node.argsToString(); }, 3),
        ANGLE((gui, x, y, node) -> { threeButtonRotation(gui, x, y, node); }, (node) -> { return "angle " + node.argsToString(); }, 3),
        VERTEX((gui, x, y, node) -> {}, (node) -> { return "vertex " + node.argsToString(); }, 12),
        CUBE((gui, x, y, node) -> {
            FloatFieldWidget bU = new FloatFieldWidget(gui.textRendererGUI, x, y, 30, 10, 0, 64);
            FloatFieldWidget bV = new FloatFieldWidget(gui.textRendererGUI, x+35, y, 30, 10, 0, 64);
            FloatFieldWidget bW = new FloatFieldWidget(gui.textRendererGUI, x, y+15, 30, 10, 64, 64);
            FloatFieldWidget bH = new FloatFieldWidget(gui.textRendererGUI, x+35, y+15, 30, 10, 64, 64);
            TextureWidget texture = new TextureWidget(x, y+45, 110, 110, gui.entity);
            IntFieldWidget face = new IntFieldWidget(gui.textRendererGUI, x, y+160, 110, 10, 0, 5);

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
        }, (node) -> { return NodeHelper.cubeToString(node); }, 4*6),
        MESH((gui, x, y, node) -> {
            TextFieldWidget meshID = new TextFieldWidget(gui.textRendererGUI, x, y, 100, 10, Text.of(""));
            FloatFieldWidget bU = new FloatFieldWidget(gui.textRendererGUI, x, y+15, 30, 10, 0, 64);
            FloatFieldWidget bV = new FloatFieldWidget(gui.textRendererGUI, x+35, y+15, 30, 10, 0, 64);
            FloatFieldWidget bW = new FloatFieldWidget(gui.textRendererGUI, x, y+30, 30, 10, 64, 64);
            FloatFieldWidget bH = new FloatFieldWidget(gui.textRendererGUI, x+35, y+30, 30, 10, 64, 64);
            TextureWidget texture = new TextureWidget(x, y+45, 110, 110, gui.entity);

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
        }, (node) -> { return NodeHelper.meshToString(node); }, 5),
        CANCEL((gui, x, y, node) -> {}, (node) -> { return "cancel"; }, 0);

        NodeFunc func;
        ScriptFunc tofunc;
        public int argument_count;

        private NodeType(NodeFunc _func, ScriptFunc _tofunc, int _argumentcount) {
            this.func = _func;
            this.tofunc = _tofunc;
            this.argument_count = _argumentcount;
        }
    }

    public interface NodeFunc {
        void init(EntityGuiHandler gui, int x, int y, Node node);
    }

    public interface ScriptFunc {
        String run(Node node);
    }
}
