package me.pixeldots.pixelscharactermodels.Handlers;

import java.util.List;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.accessors.PlayerModelAccessor;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
  
public class RenderingHandler {
	
	public GameProfile currentPlayerRendering = null;

	public void playerRenderHead(PlayerEntityModel<?> model, PlayerEntity entity, LivingEntityRenderer<?,?> renderer) {
		if (!PixelsCharacterModels.PlayerDataList.containsKey(entity.getUuid())) PixelsCharacterModels.PlayerDataList.put(entity.getUuid(), new PlayerData(entity, model));
		FramesHandler.UpdateFrames(model, entity);
	}
	
	public void playerRenderTail(PlayerEntityModel<?> model, PlayerEntity entity, LivingEntityRenderer<?,?> renderer) {
		if (PixelsCharacterModels.dataPackets != null) setPlayerModelPartsData(model, entity);
	}
	
	public void renderPartHead(MatrixStack matrices, VertexConsumer vertices, ModelPart part, int light, int overlay, CallbackInfo info) {
		if (part == null) return;
		if (PixelsCharacterModels.dataPackets.containsKey(part)) {
			boolean ignoreRotation = false;
			ModelPartData data = PixelsCharacterModels.dataPackets.get(part);
			if (data.copyFromPart != null && PixelsCharacterModels.dataPackets.containsKey(data.copyFromPart)) {
				part.pitch = data.copyFromPart.pitch;
				part.yaw = data.copyFromPart.yaw;
				part.roll = data.copyFromPart.roll;
				data = PixelsCharacterModels.dataPackets.get(data.copyFromPart);
				ignoreRotation = true;
			}
			
			if (!isPartFromPlayer(part, data)) return;
			if (data.Show == false) { info.cancel(); return; }
			
			if (!ignoreRotation) {
				if (!data.useRotation && data.activeRotation) { data.useRotation = true; data.activeRotation = false; }
				if (PixelsCharacterModels.playingAnimationData != null) {
					PCMAnimation anim = PixelsCharacterModels.playingAnimationData;
					if (anim.LimbRotations.containsKey(part)) {
						data.useRotation = true;
						data.activeRotation = true;
						data.rot = anim.LimbRotations.get(part);
						if (data.rot != null) {
							MapVec3 vector = FramesHandler.getLerpIfFrames(data.rot, part);
							part.pitch = (float)Math.toRadians(vector.X);
							part.yaw = (float)Math.toRadians(vector.Y);
							part.roll = (float)Math.toRadians(vector.Z);
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
			}
			
			MapVec3 animOffset = new MapVec3();
			if (PixelsCharacterModels.playingAnimationData != null) {
				PCMAnimation anim = PixelsCharacterModels.playingAnimationData;
				animOffset = anim.playerTransform;
			}
			matrices.translate(data.pos.X + animOffset.X, -data.pos.Y - animOffset.Y, -data.pos.Z - animOffset.Z);
		} else {
			PixelsCharacterModels.dataPackets.put(part, new ModelPartData());
		}
	}
	
	public void renderPartCubioudsHead(MatrixStack.Entry matrices, ModelPart part, VertexConsumer vertex, int light, int overlay, CallbackInfo info) {
		if (part == null) return;
		if (PixelsCharacterModels.dataPackets.containsKey(part)) {
			ModelPartData data = PixelsCharacterModels.dataPackets.get(part);
			if (data.copyFromPart != null && PixelsCharacterModels.dataPackets.containsKey(data.copyFromPart)) data = PixelsCharacterModels.dataPackets.get(data.copyFromPart);
			if (!isPartFromPlayer(part, data)) return;
			
			//Scale
			if (data.scale.X <= 0) data.scale.X = 0.01f;
			if (data.scale.Y <= 0) data.scale.Y = 0.01f;
			if (data.scale.Z <= 0) data.scale.Z = 0.01f;
			ScaleMatrixEntry(matrices, data.scale);
			//matrices.scale(data.scale.X, data.scale.Y, data.scale.Z);
			//Scale
			
		}
	}
	
	public void renderPartCubioudsTail(MatrixStack.Entry matrices, ModelPart part, VertexConsumer vertex, int light, int overlay, CallbackInfo info) {
		if (part == null || !PixelsCharacterModels.dataPackets.containsKey(part)) return;

		ModelPartData data = PixelsCharacterModels.dataPackets.get(part);
		if (data.copyFromPart != null && PixelsCharacterModels.dataPackets.containsKey(data.copyFromPart)) return;
		if (!isPartFromPlayer(part, data)) return;

		boolean hasPreview = false;
		if (PixelsCharacterModels.previewModelPart != null && PixelsCharacterModels.previewModelPart.owner == data) hasPreview = true;

		if (data.cubes.size()+data.meshes.size() >= 1 || hasPreview) {
			/*RenderSystem.setShaderTexture(0, ((AbstractClientPlayerEntity)data.entity).getSkinTexture()); // sets the shaders texture to the players skin
			RenderSystem.setShaderColor(1, 1, 1, 1); // sets the shader color to white
			RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader); // sets the Shader
			RenderSystem.enableDepthTest(); // enable Depth Test*/
			TextureManager textureManager = PixelsCharacterModels.client.minecraft.getTextureManager();
			Identifier playerSkin = ((AbstractClientPlayerEntity)data.entity).getSkinTexture();
			//textureManager.bindTexture(playerSkin);

			for (int i = 0; i < data.cubes.size(); i++) { // renders all cubes on limb
				data.cubes.get(i).render(textureManager, matrices, vertex, light, overlay, 1, 1, 1, 1, data.entity);
			}
			if (hasPreview && PixelsCharacterModels.previewModelPart.cube != null) { // renders the preview if it's a cube
				PixelsCharacterModels.previewModelPart.cube.render(textureManager, matrices, vertex, light, overlay, 1, 1, 1, 1, data.entity);
				//textureManager.bindTexture(playerSkin);
			}

			if (data.meshes.size() >= 1 || hasPreview) { // flips the rotation and disables culling
				RenderSystem.disableCull();
				matrices.getModel().multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float) Math.toRadians(90)));
				matrices.getNormal().multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float) Math.toRadians(90)));
				matrices.getModel().multiply(Vec3f.POSITIVE_Z.getRadialQuaternion((float) Math.toRadians(180)));
				matrices.getNormal().multiply(Vec3f.POSITIVE_Z.getRadialQuaternion((float) Math.toRadians(180)));
			}
			for (int i = 0; i < data.meshes.size(); i++) { // renders all meshes on limb
				data.meshes.get(i).render(textureManager, matrices, vertex, light, overlay, 1, 1, 1, 1, data.entity);
			}
			if (hasPreview && PixelsCharacterModels.previewModelPart.mesh != null) { // renders the preview if it's a mesh
				PixelsCharacterModels.previewModelPart.mesh.render(textureManager, matrices, vertex, light, overlay, 1, 1, 1, 1, data.entity);
			}
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
			
			MapVec3 animOffset = new MapVec3();
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
	
	public void ScaleMatrixEntry(MatrixStack.Entry entry, MapVec3 value) {
		float x = value.X;
		float y = value.Y;
		float z = value.Z;
	    entry.getModel().multiply(Matrix4f.scale(x, y, z));
	    if (x == y && y == z) {
		    if (x > 0.0F) {
		    	return;
		    }
	
		    entry.getNormal().multiply(-1.0F);
	    }

	    float f = 1.0F / x;
	    float g = 1.0F / y;
	    float h = 1.0F / z;
	    float i = MathHelper.fastInverseCbrt(f * g * h);
	    entry.getNormal().multiply(Matrix3f.scale(i * f, i * g, i * h));
	}

}
