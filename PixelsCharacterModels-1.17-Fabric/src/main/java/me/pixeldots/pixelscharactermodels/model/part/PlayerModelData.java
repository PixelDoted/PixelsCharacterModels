package me.pixeldots.pixelscharactermodels.model.part;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class PlayerModelData {
	
	public PlayerEntityModel<?> Model;
	public List<ModelPartData> parts = new ArrayList<ModelPartData>();
	
	@SuppressWarnings("unlikely-arg-type")
	public ModelPartData getDataFromPart(ModelPart part) {
		if (!parts.contains(part)) return null;
		return parts.get(parts.indexOf(part));
	}
}
