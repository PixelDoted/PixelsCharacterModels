package me.pixeldots.pixelscharactermodels.Handlers;

import java.util.List;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.accessors.PlayerModelAccessor;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
  
public class RenderingHandler {
	
	public GameProfile currentPlayerRendering = null;
	
	public void playerRenderHead(PlayerEntityModel<?> model, PlayerEntity entity) {
		if (!PixelsCharacterModels.EntityModelList.containsKey(entity)) PixelsCharacterModels.EntityModelList.put(entity, model);
		if (PixelsCharacterModels.dataPackets != null) {
			setPlayerModelPartsData(model, entity);
		}
	}
	
	public void renderPartHead(MatrixStack matrices, VertexConsumer vertices, ModelPart part, CallbackInfo info) {
		if (part == null) return;
		if (PixelsCharacterModels.dataPackets.containsKey(part)) {
			ModelPartData data = PixelsCharacterModels.dataPackets.get(part);
			if (data.copyFromPart != null && PixelsCharacterModels.dataPackets.containsKey(data.copyFromPart)) data = PixelsCharacterModels.dataPackets.get(data.copyFromPart);
			if (!isPartFromPlayer(part, data)) return;
			
			if (PixelsCharacterModels.animations.containsKey(PixelsCharacterModels.playingAnimation)) {
				PCMAnimation anim = PixelsCharacterModels.animations.get(PixelsCharacterModels.playingAnimation);
				if (anim.LimbRotations.containsKey(part)) {
					data.rot = anim.LimbRotations.get(part);
					part.roll = (float)Math.toRadians(data.rot.X);
					part.pitch = (float)Math.toRadians(data.rot.Y);
					part.yaw = (float)Math.toRadians(data.rot.Z);
					data.useRotation = true;
				} else if (data.useRotation) {
					part.roll = 0;
					part.pitch = 0;
					part.yaw = 0;
					data.useRotation = false;
				}
			} else if (data.useRotation) {
				part.roll = 0;
				part.pitch = 0;
				part.yaw = 0;
				data.useRotation = false;
			}
			//Scale
			if (data.scale.X <= 0) data.scale.X = 0.01f;
			if (data.scale.Y <= 0) data.scale.Y = 0.01f;
			if (data.scale.Z <= 0) data.scale.Z = 0.01f;
			
			matrices.scale(data.scale.X, data.scale.Y, data.scale.Z);
			//Scale
			MapVec3 animOffset = new MapVec3(0,0,0);
			if (PixelsCharacterModels.animations.containsKey(PixelsCharacterModels.playingAnimation)) {
				PCMAnimation anim = PixelsCharacterModels.animations.get(PixelsCharacterModels.playingAnimation);
				animOffset = anim.playerTransform;
			}
			matrices.translate(data.pos.X + animOffset.X, -data.pos.Y - animOffset.Y, -data.pos.Z - animOffset.Z);
		} else {
			PixelsCharacterModels.dataPackets.put(part, new ModelPartData());
		}
	}
	
	public boolean isPartFromPlayer(ModelPart part, ModelPartData data) {
		if (data.model == null || currentPlayerRendering == null) return false;
		return currentPlayerRendering.getId() == data.entity.getGameProfile().getId() && currentPlayerRendering.getName() == data.entity.getGameProfile().getName();
	}
	
	public void renderPartTail(MatrixStack matrices, VertexConsumer vertices, ModelPart part, CallbackInfo info) {
		if (part == null) return;
		if (PixelsCharacterModels.dataPackets.containsKey(part)) {
			ModelPartData data = PixelsCharacterModels.dataPackets.get(part);
			if (data.copyFromPart != null && PixelsCharacterModels.dataPackets.containsKey(data.copyFromPart)) data = PixelsCharacterModels.dataPackets.get(data.copyFromPart);
			if (!isPartFromPlayer(part, data)) return;
			//Scale
			if (data.scale.X <= 0) data.scale.X = 0.01f;
			if (data.scale.Y <= 0) data.scale.Y = 0.01f;
			if (data.scale.Z <= 0) data.scale.Z = 0.01f;
			
			matrices.scale(1/data.scale.X, 1/data.scale.Y, 1/data.scale.Z);
			//Scale
			MapVec3 animOffset = new MapVec3(0,0,0);
			if (PixelsCharacterModels.animations.containsKey(PixelsCharacterModels.playingAnimation)) {
				PCMAnimation anim = PixelsCharacterModels.animations.get(PixelsCharacterModels.playingAnimation);
				animOffset = anim.playerTransform;
			}
			matrices.translate(-data.pos.X - animOffset.X, data.pos.Y + animOffset.Y, data.pos.Z + animOffset.Z);
		} else {
			PixelsCharacterModels.dataPackets.put(part, new ModelPartData());
		}
	}
	
	public void renderItemHead(LivingEntity entity, ItemStack stack, MatrixStack matrices) {
	}
	
	public void renderItemTail(LivingEntity entity, ItemStack stack, MatrixStack matrices) {
	}
	
	public void setPlayerModelPartsData(PlayerEntityModel<?> model, PlayerEntity entity) {
		List<ModelPart> parts = ((PlayerModelAccessor)model).getParts();
		for (int i = 0; i < parts.size(); i++) {
			if (!PixelsCharacterModels.dataPackets.containsKey(parts.get(i))) continue;
			if (PixelsCharacterModels.dataPackets.get(parts.get(i)).model != null) continue;
			PixelsCharacterModels.dataPackets.get(parts.get(i)).model = model;
			PixelsCharacterModels.dataPackets.get(parts.get(i)).entity = entity;
		}
		if (PixelsCharacterModels.dataPackets.containsKey(model.hat)) PixelsCharacterModels.dataPackets.get(model.hat).setCopyFromPart(model.head);
		if (PixelsCharacterModels.dataPackets.containsKey(model.jacket)) PixelsCharacterModels.dataPackets.get(model.jacket).setCopyFromPart(model.body);
		if (PixelsCharacterModels.dataPackets.containsKey(model.leftPants)) PixelsCharacterModels.dataPackets.get(model.leftPants).setCopyFromPart(model.leftLeg);
		if (PixelsCharacterModels.dataPackets.containsKey(model.rightPants)) PixelsCharacterModels.dataPackets.get(model.rightPants).setCopyFromPart(model.rightLeg);
		if (PixelsCharacterModels.dataPackets.containsKey(model.leftSleeve)) PixelsCharacterModels.dataPackets.get(model.leftSleeve).setCopyFromPart(model.leftArm);
		if (PixelsCharacterModels.dataPackets.containsKey(model.rightSleeve)) PixelsCharacterModels.dataPackets.get(model.rightSleeve).setCopyFromPart(model.rightArm);
	}

}
