package me.pixeldots.pixelscharactermodels.utils.data;

import java.util.ArrayList;
import java.util.List;

import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class AnimationData {

	public String name = "";
	
	public MapVec3 playerTransform = new MapVec3(0,0,0);
	public List<MapVec3> LimbRotations = new ArrayList<MapVec3>();
	public List<MapVec3> LimbPivots = new ArrayList<MapVec3>();
	public List<String> LimbIDs = new ArrayList<String>();
	
	public PCMAnimation convertToAnimation(PlayerEntity entity, PlayerEntityModel<?> model) {
		PCMAnimation anim = new PCMAnimation();
		anim.playerTransform = this.playerTransform;
		anim.name = this.name;
		for (int i = 0; i < this.LimbIDs.size(); i++) {
			ModelPart part = getPlayerLimb(this.LimbIDs.get(i), model);
			if (part == null) continue;
			anim.LimbRotations.put(part, LimbRotations.get(i));
			anim.LimbParts.add(part);
		}
		return anim;
	}
	
	public ModelPart getPlayerLimb(String name, PlayerEntityModel<?> model) {
		if (name.toLowerCase().startsWith("head")) return model.head;
		else if (name.toLowerCase().startsWith("body")) return model.body;
		else if (name.toLowerCase().startsWith("leftleg")) return model.leftLeg;
		else if (name.toLowerCase().startsWith("rightleg")) return model.rightLeg;
		else if (name.toLowerCase().startsWith("leftarm")) return model.leftArm;
		else if (name.toLowerCase().startsWith("rightarm")) return model.rightArm;
		return null;
	}
}
