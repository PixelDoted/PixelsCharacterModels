package me.pixeldots.pixelscharactermodels.Handlers;

import java.util.List;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.accessors.PlayerModelAccessor;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;
  
public class RenderingHandler {
	
	public GameProfile currentPlayerRendering = null;
	
	public void playerRenderHead(PlayerEntityModel<?> model, PlayerEntity entity, LivingEntityRenderer<?,?> renderer) {
		if (!PixelsCharacterModels.EntityModelList.containsKey(entity)) PixelsCharacterModels.EntityModelList.put(entity, model);
	}
	
	public void playerRenderTail(PlayerEntityModel<?> model, PlayerEntity entity, LivingEntityRenderer<?,?> renderer) {
		if (PixelsCharacterModels.dataPackets != null) {
			setPlayerModelPartsData(model, entity);
		}
	}
	
	public void renderPartHead(MatrixStack matrices, VertexConsumer vertices, ModelPart part, int light, int overlay, CallbackInfo info) {
		if (part == null) return;
		if (PixelsCharacterModels.dataPackets.containsKey(part)) {
			ModelPartData data = PixelsCharacterModels.dataPackets.get(part);
			if (data.copyFromPart != null && PixelsCharacterModels.dataPackets.containsKey(data.copyFromPart)) {
				part.pitch = data.copyFromPart.pitch;
				part.yaw = data.copyFromPart.yaw;
				part.roll = data.copyFromPart.roll;
				data = PixelsCharacterModels.dataPackets.get(data.copyFromPart);
			}
			
			if (!isPartFromPlayer(part, data)) return;
			if (data.Show == false) { info.cancel(); return; }
			
			if (!data.useRotation && data.activeRotation) { data.useRotation = true; data.activeRotation = false; }
			if (PixelsCharacterModels.playingAnimationData != null) {
				PCMAnimation anim = PixelsCharacterModels.playingAnimationData;
				if (anim.LimbRotations.containsKey(part)) {
					data.useRotation = true;
					data.activeRotation = true;
					data.rot = anim.LimbRotations.get(part);
					if (data.rot != null) {
						part.pitch = (float)Math.toRadians(data.rot.X);
						part.yaw = (float)Math.toRadians(data.rot.Y);
						part.roll = (float)Math.toRadians(data.rot.Z);
					} else {
						data.useRotation = false;
						data.activeRotation = false;
					}
				} else if (data.useRotation) {
					part.pitch = 0;
					part.yaw = 0;
					part.roll = 0;
					data.useRotation = false;
				}
			} else if (data.useRotation) {
				part.pitch = 0;
				part.yaw = 0;
				part.roll = 0;
				data.useRotation = false;
			}
			//Scale
			if (data.scale.X <= 0) data.scale.X = 0.01f;
			if (data.scale.Y <= 0) data.scale.Y = 0.01f;
			if (data.scale.Z <= 0) data.scale.Z = 0.01f;
			
			matrices.scale(data.scale.X, data.scale.Y, data.scale.Z);
			//Scale
			MapVec3 animOffset = new MapVec3(0,0,0);
			if (PixelsCharacterModels.playingAnimationData != null) {
				PCMAnimation anim = PixelsCharacterModels.playingAnimationData;
				animOffset = anim.playerTransform;
			}
			matrices.translate(data.pos.X + animOffset.X, -data.pos.Y - animOffset.Y, -data.pos.Z - animOffset.Z);
		} else {
			PixelsCharacterModels.dataPackets.put(part, new ModelPartData());
		}
	}
	
	public void renderPartCubioudsTail(MatrixStack.Entry matrices, ModelPart part, VertexConsumer vertex, int light, int overlay, CallbackInfo info) {
		if (part == null) return;
		if (PixelsCharacterModels.dataPackets.containsKey(part)) {
			ModelPartData data = PixelsCharacterModels.dataPackets.get(part);
			if (data.copyFromPart != null && PixelsCharacterModels.dataPackets.containsKey(data.copyFromPart)) data = PixelsCharacterModels.dataPackets.get(data.copyFromPart);
			if (!isPartFromPlayer(part, data)) return;
		}
	}
	
	public boolean isPartFromPlayer(ModelPart part, ModelPartData data) {
		if (data.model == null || currentPlayerRendering == null) return false;
		return currentPlayerRendering.getId() == data.entity.getGameProfile().getId() && currentPlayerRendering.getName() == data.entity.getGameProfile().getName();
	}
	
	public void renderPartTail(MatrixStack matrices, VertexConsumer vertices, ModelPart part, int light, int overlay, CallbackInfo info) {
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
			if (PixelsCharacterModels.playingAnimationData != null) {
				PCMAnimation anim = PixelsCharacterModels.playingAnimationData;
				animOffset = anim.playerTransform;
			}
			matrices.translate((-data.pos.X - animOffset.X)*data.scale.X, (data.pos.Y + animOffset.Y)*data.scale.Y, (data.pos.Z + animOffset.Z)*data.scale.Z);
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
