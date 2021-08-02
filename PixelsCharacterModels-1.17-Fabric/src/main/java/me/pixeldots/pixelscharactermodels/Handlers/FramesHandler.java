package me.pixeldots.pixelscharactermodels.Handlers;

import java.util.HashMap;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.accessors.MinecraftClientAccessor;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class FramesHandler {
	
	public static float CurrentTick = 1;
	public static Map<ModelPart, MapVec3> lastLimbRotations = new HashMap<>(); 
	
	public static void UpdateFrames(PlayerEntityModel<?> model, PlayerEntity entity) {
		if (PixelsCharacterModels.isPlayingFrames) {
			if (PixelsCharacterModels.playingAnimationData == null) {
				PixelsCharacterModels.PCMClient.framesAnimationID = 0;
				loadAnimationFrames(model, entity);
			}
			else {
				for (int i = 0; i < PixelsCharacterModels.playingAnimationData.LimbParts.size(); i++) {
					ModelPart part = PixelsCharacterModels.playingAnimationData.LimbParts.get(i);
					MapVec3 rot = PixelsCharacterModels.playingAnimationData.LimbRotations.get(part);
					if (sameInRange((float)Math.toDegrees(part.pitch), rot.X, 0.02f) 
							&& sameInRange((float)Math.toDegrees(part.yaw), rot.Y, 0.02f) 
							&& sameInRange((float)Math.toDegrees(part.roll), rot.Z, 0.02f)) {
						lastLimbRotations = PixelsCharacterModels.playingAnimationData.LimbRotations;
						if (PixelsCharacterModels.PCMClient.framesAnimationID+1 >= PixelsCharacterModels.playingFramesData.frames.size()) {
							if (PixelsCharacterModels.playingFramesData.Loop) {
								PixelsCharacterModels.PCMClient.framesAnimationID = 0;
								loadAnimationFrames(model, entity);
							} else {
								PixelsCharacterModels.PCMClient.framesAnimationID = -1;
								PixelsCharacterModels.isPlayingFrames = false;
								PixelsCharacterModels.playingAnimation = "";
								PixelsCharacterModels.playingFramesData = null;
								PixelsCharacterModels.playingAnimationData = null;
							}
						} else {
							PixelsCharacterModels.PCMClient.framesAnimationID++;
							loadAnimationFrames(model, entity);
						}
						CurrentTick = 1;
					}
				}
			}
			CurrentTick -= (1-PixelsCharacterModels.playingFramesData.TimePerFrame);
			if (CurrentTick < 0) CurrentTick = 0;
		} else { CurrentTick = 1; lastLimbRotations.clear(); }
	}
	
	public static void loadAnimationFrames(PlayerEntityModel<?> model, PlayerEntity entity) {
		String s = PixelsCharacterModels.playingFramesData.frames.get(PixelsCharacterModels.PCMClient.framesAnimationID);
		PixelsCharacterModels.AnimationsData.loadAnimation(s, entity, model);
		PixelsCharacterModels.playingAnimation = s;
		PixelsCharacterModels.playingAnimationData = PixelsCharacterModels.PCMClient.currentStoredAnimation;
	}
	
	public static MapVec3 getLerpIfFrames(MapVec3 vector, ModelPart part) {
		if (PixelsCharacterModels.isPlayingFrames) {
			MapVec3 value = new MapVec3();
			MapVec3 partRotation = new MapVec3((float)Math.toDegrees(part.pitch), (float)Math.toDegrees(part.yaw), (float)Math.toDegrees(part.roll));
			if (lastLimbRotations.containsKey(part)) partRotation = lastLimbRotations.get(part);
			
			value.X = Lerp(vector.X, partRotation.X, PixelsCharacterModels.playingFramesData.TimePerFrame);
			value.Y = Lerp(vector.Y, partRotation.Y, PixelsCharacterModels.playingFramesData.TimePerFrame);
			value.Z = Lerp(vector.Z, partRotation.Z, PixelsCharacterModels.playingFramesData.TimePerFrame);
			return value;
		}
		return vector;
	}
	
	/*Lerp*/
	public static float Lerp(float a, float b, float t) {
		float end = b;
		float delta = CurrentTick;
		float v = (float) MathHelper.lerp(delta, a, end);
		//float v = (1-t) * a + t * b;
		
		if (v > b && v > a) { v = b; }
		else if (v < b && v < a) { v = b; }
		if (a == b) v = b;
		if (sameInRange(v, end, 0.02f)) v = end;
		return v;
	}
	
	public static boolean sameInRange(float value, float end, float range) {
		if (value >= end-range && value  <= end+range) {
			return true;
		}
		return false;
	}
	
}
