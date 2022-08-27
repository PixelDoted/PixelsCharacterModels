package me.pixeldots.pixelscharactermodels.gui;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget;
import me.pixeldots.pixelscharactermodels.other.Node;
import me.pixeldots.pixelscharactermodels.other.Node.NodeType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3f;

public class NodeSelectGui extends GuiHandler {

    public LivingEntity entity;
    public float entityViewScale = 75;
    public Vec3f entityRotation = Vec3f.ZERO;

    public boolean is_animation = false;

    public NodeSelectGui(LivingEntity _entity, float _entityViewScale, Vec3f _entityRotation, boolean _isanimation) {
        super("Node Selector");
        entity = _entity;
        entityViewScale = _entityViewScale;
        this.is_animation = _isanimation;
        this.entityRotation = _entityRotation;
    }

    public void setScreen(GuiHandler gui) {
        this.client.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();

        addButton(new FlatButtonWidget(5, 5, 110, 10, Text("pcm.gui.Cancel"), (btn) -> {
            if (is_animation) this.client.setScreen(new AnimationGui(entity, entityViewScale, this.entityRotation));
            else this.client.setScreen(new EditorGui(entity, entityViewScale, this.entityRotation));
        }));

        int row = 1, col = 0;
        for (NodeType t : NodeType.values()) {
            if (10+(row*15) > this.height) {
                col++;
                row = 1;
            }

            addButton(new FlatButtonWidget(5+(col*105), 10+(row*15), 110, 10, Text.of(t.name().toLowerCase()), (btn) -> {
                Node node = new Node(t);
                node.changed = true;
                if (is_animation) {
                    AnimationGui.nodes.add(node);
                    this.client.setScreen(new AnimationGui(entity, entityViewScale, this.entityRotation));
                } else {
                    EditorGui.nodes.add(node);
                    this.client.setScreen(new EditorGui(entity, entityViewScale, this.entityRotation));
                }
            }));

            row++;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        /*float entityMouseX = 0;
        float entityMouseY = 0;

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX = (float)(this.width/2) - mouseX;
            entityMouseY = (float)(this.height/2+37-125) - mouseY;
        }*/

        if (entity != null)
            drawEntity(this.width/2, this.height/2+37, Math.round(entityViewScale), 0, 0, this.entityRotation, entity, PCMMain.settings.show_block_under_player_ui);
        
        drawColor(matrices, 0, 0, 120, this.height, 0, 4, 17, 222);

        drawHorizontalLine(matrices, 5, 113, 19, 0, 0, 0, 188);
        drawHorizontalLine(matrices, 5, 113, 20, 0, 0, 0, 188);

        drawVerticalLine(matrices, 119, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 120, -1, this.height, 0, 0, 0, 255);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        entityRotation.add((float)deltaY*(.005f*PCMMain.settings.player_rotation_sensitivity), (float)deltaX*(-.01f*PCMMain.settings.player_rotation_sensitivity), 0F);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

}
