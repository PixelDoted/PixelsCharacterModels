package me.pixeldots.pixelscharactermodels.utils;

import me.pixeldots.pixelscharactermodels.model.CreatePartData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class GuiData {
	
	public PlayerEntity entity = null;
	public PlayerEntityModel<?> model = null;
	
	public String SelectedPresetPath = "";
	public String SelectedPresetName = "";
	
	public String SelectedPart = "";
	public int SelectedPartID = -1;
	public ModelPart SelectedPartModel = null;
	
	public String SelectedAnimation = "";
	
	public String SelectedFrames = "";
	public int SelectedFramesID = -1;
	
	public CreatePartData createPartData = new CreatePartData();
	
}
