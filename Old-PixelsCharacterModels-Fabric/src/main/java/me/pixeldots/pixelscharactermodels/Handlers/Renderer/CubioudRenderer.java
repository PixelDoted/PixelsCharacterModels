package me.pixeldots.pixelscharactermodels.Handlers.Renderer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.Handlers.FramesHandler;
import me.pixeldots.pixelscharactermodels.Handlers.RenderingHandler;
import me.pixeldots.pixelscharactermodels.model.ModelPartData;
import me.pixeldots.pixelscharactermodels.model.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.model.mesh.ModelPartMesh;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class CubioudRenderer {

    private RenderingHandler m_handler;

    public int currentPlayerParts = 0;
	public int currentPlayerTris = 0;

    public CubioudRenderer(RenderingHandler _handler) {
        this.m_handler = _handler;
    }

    public void renderPartHead(MatrixStack matrices, VertexConsumer vertices, ModelPart part, int light, int overlay, CallbackInfo info) {
		if (m_handler.currentPlayerRendering == null) return;
		
		PlayerData playerdata = PixelsCharacterModels.PlayerDataList.get(m_handler.currentPlayerRendering.getId());
		if (part == null) return;
		if (playerdata.limbs.containsKey(part)) {
			boolean ignoreRotation = false;
			ModelPartData data = playerdata.limbs.get(part);
			if (data.copyFromPart != null && playerdata.limbs.containsKey(data.copyFromPart)) {
				part.pitch = data.copyFromPart.pitch;
				part.yaw = data.copyFromPart.yaw;
				part.roll = data.copyFromPart.roll;
				data = playerdata.limbs.get(data.copyFromPart);
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
			playerdata.limbs.put(part, new ModelPartData());
		}
	}
	
	public void renderPartCubioudsHead(MatrixStack.Entry matrices, ModelPart part, VertexConsumer vertex, int light, int overlay, CallbackInfo info) {
		if (m_handler.currentPlayerRendering == null) return;
		
		PlayerData playerdata = PixelsCharacterModels.PlayerDataList.get(m_handler.currentPlayerRendering.getId());
		if (part == null) return;
		if (playerdata.limbs.containsKey(part)) {
			ModelPartData data = playerdata.limbs.get(part);
			if (data.copyFromPart != null && playerdata.limbs.containsKey(data.copyFromPart)) data = playerdata.limbs.get(data.copyFromPart);
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

	public boolean canRenderPart(ModelPartMesh mesh) {
		boolean output = true;
		if (output != false && PixelsCharacterModels.localData.MaximumPartsPerPlayer > 0) {
			output = currentPlayerParts < PixelsCharacterModels.localData.MaximumPartsPerPlayer;
			currentPlayerParts++;
		}
		if (output != false && PixelsCharacterModels.localData.MaximumTrisPerPlayer > 0) {
			output = PixelsCharacterModels.localData.MaximumTrisPerPlayer <= 0 ? true : currentPlayerTris+mesh.sides.length < PixelsCharacterModels.localData.MaximumTrisPerPlayer;
			currentPlayerTris += mesh.sides.length;
		}
		return output;
	}
	public boolean canRenderPart(ModelPartCube cube) {
		boolean output = true;
		if (output != false && PixelsCharacterModels.localData.MaximumPartsPerPlayer > 0) {
			output = currentPlayerParts < PixelsCharacterModels.localData.MaximumPartsPerPlayer;
			currentPlayerParts++;
		}
		if (output != false && PixelsCharacterModels.localData.MaximumTrisPerPlayer > 0) {
			output = PixelsCharacterModels.localData.MaximumTrisPerPlayer <= 0 ? true : currentPlayerTris+cube.sides.length < PixelsCharacterModels.localData.MaximumTrisPerPlayer;
			currentPlayerTris += cube.sides.length;
		}
		return output;
	}
	
	public void renderPartCubioudsTail(MatrixStack.Entry matrices, ModelPart part, VertexConsumer vertex, int light, int overlay, CallbackInfo info) {
		if (m_handler.currentPlayerRendering == null) return;
		
		PlayerData playerdata = PixelsCharacterModels.PlayerDataList.get(m_handler.currentPlayerRendering.getId());
		if (part == null || !playerdata.limbs.containsKey(part)) return;

		ModelPartData data = playerdata.limbs.get(part);
		if (data.copyFromPart != null && playerdata.limbs.containsKey(data.copyFromPart)) return;
		if (!isPartFromPlayer(part, data)) return;

		boolean hasPreview = false;
		if (PixelsCharacterModels.previewModelPart != null && PixelsCharacterModels.previewModelPart.owner == data) hasPreview = true;

		if (data.cubes.size()+data.meshes.size() >= 1 || hasPreview) {
			PlayerEntity entity = data.getEntity();

			for (ModelPartCube cube : data.cubes) { // renders all cubes on limb
				if (!canRenderPart(cube)) continue;
				cube.render(matrices, vertex, light, overlay, 1, 1, 1, 1, entity);
			}
			if (hasPreview && PixelsCharacterModels.previewModelPart.cube != null) { // renders the preview if it's a cube
				PixelsCharacterModels.previewModelPart.cube.render(matrices, vertex, light, overlay, 1, 1, 1, 1, entity);
			}

			if (data.meshes.size() >= 1 || hasPreview) { // flips the rotation and disables culling
				RenderSystem.disableCull();
				matrices.getModel().multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float) Math.toRadians(90)));
				matrices.getNormal().multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float) Math.toRadians(90)));
				matrices.getModel().multiply(Vec3f.POSITIVE_Z.getRadialQuaternion((float) Math.toRadians(180)));
				matrices.getNormal().multiply(Vec3f.POSITIVE_Z.getRadialQuaternion((float) Math.toRadians(180)));
			}
			for (ModelPartMesh mesh : data.meshes) { // renders all meshes on limb
				if (!canRenderPart(mesh)) continue;
				mesh.render(matrices, vertex, light, overlay, 1, 1, 1, 1, entity);
			}
			if (hasPreview && PixelsCharacterModels.previewModelPart.mesh != null) { // renders the preview if it's a mesh
				PixelsCharacterModels.previewModelPart.mesh.render(matrices, vertex, light, overlay, 1, 1, 1, 1, entity);
			}
		}
	}
	
	public boolean isPartFromPlayer(ModelPart part, ModelPartData data) {
        GameProfile rendering = m_handler.currentPlayerRendering;
		if (data.player == null || rendering == null) return false;

		PlayerEntity entity = data.getEntity();
		return rendering.getId() == entity.getGameProfile().getId() && rendering.getName() == entity.getGameProfile().getName();
	}
	
	public void renderPartTail(MatrixStack matrices, VertexConsumer vertices, ModelPart part, int light, int overlay, CallbackInfo info) {
		if (m_handler.currentPlayerRendering == null) return;
		
		PlayerData playerdata = PixelsCharacterModels.PlayerDataList.get(m_handler.currentPlayerRendering.getId());
		if (part == null) return;
		if (playerdata.limbs.containsKey(part)) {
			ModelPartData data = playerdata.limbs.get(part);
			if (data.copyFromPart != null && playerdata.limbs.containsKey(data.copyFromPart)) data = playerdata.limbs.get(data.copyFromPart);
			if (!isPartFromPlayer(part, data)) return;
			
			MapVec3 animOffset = new MapVec3();
			if (PixelsCharacterModels.playingAnimationData != null) {
				PCMAnimation anim = PixelsCharacterModels.playingAnimationData;
				animOffset = anim.playerTransform;
			}
			matrices.translate((-data.pos.X - animOffset.X)*data.scale.X, (data.pos.Y + animOffset.Y)*data.scale.Y, (data.pos.Z + animOffset.Z)*data.scale.Z);
		} else {
			playerdata.limbs.put(part, new ModelPartData());
		}
	}

    public void ScaleMatrixEntry(MatrixStack.Entry entry, MapVec3 value) {
	    entry.getModel().multiply(Matrix4f.scale(value.X, value.Y, value.Z));
	    if (value.X == value.Y && value.Y == value.Z) {
		    if (value.X > 0.0F) return;
		    entry.getNormal().multiply(-1.0F);
	    }

	    float f = 1.0F / value.X, g = 1.0F / value.Y, h = 1.0F / value.Z;
	    float i = MathHelper.fastInverseCbrt(f * g * h);
	    entry.getNormal().multiply(Matrix3f.scale(i * f, i * g, i * h));
	}
    
}
