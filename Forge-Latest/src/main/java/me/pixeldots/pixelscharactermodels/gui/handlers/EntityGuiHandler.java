package me.pixeldots.pixelscharactermodels.gui.handlers;

import java.util.UUID;

import com.mojang.math.Vector3f;

import me.pixeldots.pixelscharactermodels.PCMMain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;


public class EntityGuiHandler extends GuiHandler {

    public LivingEntity entity;
    public EntityModel<?> model;
    public UUID uuid;
    
    public float entityViewScale = 75;
    public Vector3f entityRotation = Vector3f.ZERO;

    public EntityGuiHandler(String title) {
        super(title);
    }

    public void drawEntity(int x, int y, float entityMouseX, float entityMouseY) {
        if (entity != null) {
            drawEntity(x, y, Math.round(entityViewScale), entityMouseX, entityMouseY, this.entityRotation, entity, PCMMain.settings.show_block_under_player_ui);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        entityRotation.add((float)deltaY*(.005f*PCMMain.settings.player_rotation_sensitivity), (float)deltaX*(-.01f*PCMMain.settings.player_rotation_sensitivity), 0F);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
}
