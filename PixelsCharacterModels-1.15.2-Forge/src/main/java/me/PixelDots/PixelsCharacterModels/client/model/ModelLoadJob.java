package me.PixelDots.PixelsCharacterModels.client.model;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;

public class ModelLoadJob 
{
	
	public final PlayerEntity player;
	public final UUID uuid;
    public final String data;
    
    public ModelLoadJob(PlayerEntity player, String data) {
    	this.uuid = player.getUniqueID();
    	this.player = player;
    	this.data = data;
    }
	
}
