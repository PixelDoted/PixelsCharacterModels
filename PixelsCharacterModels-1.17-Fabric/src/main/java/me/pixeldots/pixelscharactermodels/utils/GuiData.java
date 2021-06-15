package me.pixeldots.pixelscharactermodels.utils;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class GuiData {
	
	public PlayerEntity entity = null;
	public PlayerEntityModel<?> model = null;
	
	public int SelectedPresetID = -1;
	public String SelectedPresetName = "";
	
	public String SelectedPart = "";
	public int SelectedPartID = -1;
	public ModelPart SelectedPartModel = null;
	
	public String SelectedAnimation = "";
	
	public CreatePartData createPartData = new CreatePartData();
	
}
