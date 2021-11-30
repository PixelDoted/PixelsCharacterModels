package me.PixelDots.PixelsCharacterModels.client.gui;

import net.minecraft.entity.player.PlayerEntity;

public class GuiSettings 
{
	
	public PlayerEntity player;
	
	public int SelectedPartID = -1;
	public String SelectedPart = "";
	public boolean SelectedPartIsLimb = true;
	
	public int SelectedMeshID = -1;
	public String SelectedMesh = "";
	public boolean SelectedMeshIsCube = true;
	
	public int SelectedPresetID = -1;
	public int SelectedAnimationID = -1;
	
	public int SelectedFrameID = -1;
	
	public GuiSettings() {
		
	}
	
	public GuiSettings(PlayerEntity player) {
		this.player = player;
	}
	
}
