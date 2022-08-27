package me.pixeldots.pixelscharactermodels.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget;
import me.pixeldots.pixelscharactermodels.other.Node;
import me.pixeldots.pixelscharactermodels.other.Node.NodeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class NodeSelectGui extends GuiHandler {

    public LivingEntity entity;
    public float entityViewScale = 75;
    public Vector3f entityRotation = Vector3f.ZERO;

    public boolean is_animation = false;

    public NodeSelectGui(LivingEntity _entity, float _entityViewScale, Vector3f _entityRotation, boolean _isanimation) {
        super("Node Selector");
        entity = _entity;
        entityViewScale = _entityViewScale;
        this.is_animation = _isanimation;
    }

    public void setScreen(GuiHandler gui) {
        this.minecraft.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();

        addButton(new FlatButtonWidget(5, 5, 110, 10, Text("pcm.gui.Cancel"), (btn) -> {
            if (is_animation) this.minecraft.setScreen(new AnimationGui(entity, entityViewScale, this.entityRotation));
            else this.minecraft.setScreen(new EditorGui(entity, entityViewScale));
        }));

        int row = 1, col = 0;
        for (NodeType t : NodeType.values()) {
            if (10+(row*15) > this.height) {
                col++;
                row = 1;
            }

            addButton(new FlatButtonWidget(5+(col*105), 10+(row*15), 110, 10, Component.literal(t.name().toLowerCase()), (btn) -> {
                Node node = new Node(t);
                node.changed = true;
                if (is_animation) {
                    AnimationGui.nodes.add(node);
                    this.minecraft.setScreen(new AnimationGui(entity, entityViewScale, this.entityRotation));
                } else {
                    EditorGui.nodes.add(node);
                    this.minecraft.setScreen(new EditorGui(entity, entityViewScale));
                }
            }));

            row++;
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
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

}
