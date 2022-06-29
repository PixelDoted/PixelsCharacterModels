package me.pixeldots.pixelscharactermodels.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import me.pixeldots.pixelscharactermodels.utils.data.AnimationData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class PCMAnimation {
	
	public String name = "";
	
	public MapVec3 playerTransform = new MapVec3();
	public Map<ModelPart, MapVec3> LimbRotations = new HashMap<ModelPart, MapVec3>();
	public Map<ModelPart, MapVec3> LimbPivots = new HashMap<ModelPart, MapVec3>();
	public List<ModelPart> LimbParts = new ArrayList<ModelPart>();
	
	public PCMAnimation() {}
	public PCMAnimation(String name) {
		this.name = name;
	}
	
	public AnimationData convertToData(PlayerEntityModel<?> model) {
		AnimationData data = new AnimationData();
		data.name = this.name;
		data.playerTransform = this.playerTransform;
		for (int i = 0; i < LimbParts.size(); i++) {
			String name = getModelPartName(LimbParts.get(i), model);
			data.LimbIDs.add(name);
			data.LimbRotations.add(LimbRotations.get(LimbParts.get(i)));
		}
		return data;
	}
	
	public String getModelPartName(ModelPart part, PlayerEntityModel<?> model) {
		if (part == model.head) return "Head";
		else if (part == model.body) return "Body";
		else if (part == model.leftLeg) return "leftLeg";
		else if (part == model.rightLeg) return "rightLeg";
		else if (part == model.leftArm) return "leftArm";
		else if (part == model.rightArm) return "rightArm";
		return null;
	}
	
}
