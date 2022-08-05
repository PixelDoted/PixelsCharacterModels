package me.pixeldots.pixelscharactermodels.gui.handlers;

import java.util.UUID;

import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;


public class EntityGuiHandler extends GuiHandler {

    public LivingEntity entity;
    public EntityModel<?> model;
    public UUID uuid;

    public EntityGuiHandler(String title) {
        super(title);
    }
    
}
