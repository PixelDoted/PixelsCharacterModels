package me.PixelDots.PixelsCharacterModels.util.Handlers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.model.CustomPlayerModel;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import me.PixelDots.PixelsCharacterModels.Network.NetworkPlayerData;
import me.PixelDots.PixelsCharacterModels.client.model.ModelPart;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import me.PixelDots.PixelsCharacterModels.util.Utillities;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderLivingEvent;

public class PCMPartRenderHandler {
	
	//Parts
	
	public static void generateParts(PlayerEntity player, CustomPlayerModel model, RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		if (canReset(data)) { clearRenderParts(Main.Data.playerData.get(player.getUniqueID()), model); }
		for (int i = 0; i < data.parts.Part.size(); i++) {
			if (i >= data.parts.Part.size()) break;
			ModelPart part = data.parts.Part.get(i);
			if (data.parts.Part.get(i).Renderer == null) {
				if (part.type.equalsIgnoreCase("cube") || part.type.equals("")) {
					data.parts.Part.get(i).Renderer = createPartBox(i, model, player);
				} else {
					data.parts.Part.get(i).Renderer = createPartMesh(i, model, player);
				}
			}
		}
		generateFakePart(player, model, event, data);
		data.parts.PartsCount = data.parts.Part.size();
	}
	
	public static boolean canReset(GlobalModelData data) {
		boolean value = false;
		if (data.parts.Part.size() != data.parts.PartsCount || data.renderParts.size() != data.parts.PartsCount) value = true;
		else if (data.fakeRenderPart != null && data.parts.FakePart == null) value = true;
		return value;
	}
		
	public static void renderParts(PlayerEntity player, CustomPlayerModel model, RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		if (!(Main.Data.playerData.containsKey(player.getUniqueID()))) return;
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
			
		generateParts(player, model, event);
		
		AbstractClientPlayerEntity clientplayer = (AbstractClientPlayerEntity)player;
		MatrixStack stack = event.getMatrixStack();//null;
		IVertexBuilder buffer = event.getBuffers().getBuffer(model.getRenderType(clientplayer.getLocationSkin()));
		int packedLight = event.getRenderer().getPackedLight(player, event.getPartialRenderTick());
		int packedOverlay = OverlayTexture.NO_OVERLAY;
		
		for (int i = 0; i < data.renderParts.size(); i++) {
			if (i >= data.renderParts.size()) break;
			data.renderParts.get(i).data = data.parts.Part.get(i);
			if (data.renderParts.get(i).hasParent == false) {
				data.renderParts.get(i).anchor.X = 0;
				data.renderParts.get(i).anchor.Y = -(float)Math.toRadians(player.renderYawOffset);
				data.renderParts.get(i).anchor.Z = (float)Math.PI;
					
				data.renderParts.get(i).texture = clientplayer.getLocationSkin();
				data.renderParts.get(i).render(stack, buffer, packedLight, packedOverlay);
			} else {
				data.renderParts.get(i).texture = clientplayer.getLocationSkin();
				data.renderParts.get(i).RotAnchor.Y = -1.40f;
			}
		}
		checkChildrenofPlayers(player, model);
		renderFakePart(clientplayer, stack, buffer, packedLight, packedOverlay, player, data);
	}
	
	public static void checkChildrenofPlayers(PlayerEntity player, CustomPlayerModel model) {
		Utillities.fixShowing(model.bipedHead, player);
		Utillities.fixShowing(model.bipedBody, player);
		
		Utillities.fixShowing(model.bipedLeftLeg, player);
		Utillities.fixShowing(model.bipedRightLeg, player);
		
		Utillities.fixShowing(model.bipedLeftArm, player);
		Utillities.fixShowing(model.bipedRightArm, player);
	}
		
	public static void clearRenderParts(NetworkPlayerData playerdata, CustomPlayerModel model) {
		GlobalModelData data = playerdata.data;
		Utillities.clearChildren(playerdata.FakeHead);
		Utillities.clearChildren(playerdata.FakeBody);
		Utillities.clearChildren(playerdata.FakeLeftLeg);
		Utillities.clearChildren(playerdata.FakeRightLeg);
		Utillities.clearChildren(playerdata.FakeLeftArm);
		Utillities.clearChildren(playerdata.FakeRightArm);
		for (int i = 0; i < data.parts.Part.size(); i++) {
			data.parts.Part.get(i).Renderer = null;
		}
		data.renderParts.clear();
		if (data.parts.FakePart != null) data.parts.FakePart.Renderer = null;
		data.fakeRenderPart = null;
	}
		
	//create
	public static PartModelRenderer createPartBox(int i, CustomPlayerModel model, PlayerEntity player) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		ModelPart part = data.parts.Part.get(i);
		
		int ScaleX = part.PreScaleX, ScaleY = part.PreScaleY, ScaleZ = part.PreScaleZ;
		float X = part.PreX, Y = part.PreY, Z = part.PreZ;
		float RotPointX = part.RotPointX, RotPointY = part.RotPointY, RotPointZ = part.RotPointZ;
		
		data.renderParts.add(new PartModelRenderer(model));
		data.renderParts.get(i).setTextureOffset(part.PreU, part.PreV);
		
		data.renderParts.get(i).addBox(X - ScaleX / 2, Y - ScaleY / 2, Z - ScaleZ / 2, ScaleX, ScaleY, ScaleZ, 0);
		data.renderParts.get(i).setRotationPoint(RotPointX, RotPointY, RotPointZ);
		
		data.renderParts.get(i).data = data.parts.Part.get(i);
		addChild(data.renderParts.get(i), model, player);
		return data.renderParts.get(i);
	}
		
	public static PartModelRenderer createPartMesh(int i, CustomPlayerModel model, PlayerEntity player) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		ModelPart part = data.parts.Part.get(i);
		
		int ScaleX = part.PreScaleX, ScaleY = part.PreScaleY, ScaleZ = part.PreScaleZ;
		float X = part.PreX, Y = part.PreY, Z = part.PreZ;
		float RotPointX = part.RotPointX, RotPointY = part.RotPointY, RotPointZ = part.RotPointZ;
		
		data.renderParts.add(new PartModelRenderer(model, part.type));
		data.renderParts.get(i).setTextureOffset(part.PreU, part.PreV);
		
		data.renderParts.get(i).addMesh(0, X - ScaleX / 2, Y - ScaleY / 2, Z - ScaleZ / 2, ScaleX, ScaleY, ScaleZ);
		data.renderParts.get(i).setRotationPoint(RotPointX, RotPointY, RotPointZ);
		
		data.renderParts.get(i).data = data.parts.Part.get(i);
		addChild(data.renderParts.get(i), model, player);
		return data.renderParts.get(i);
	}
	//create
	//Child system
	public static void addChild(PartModelRenderer renderer, CustomPlayerModel model, PlayerEntity player) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		if (renderer.data.ParentID == -1) return;
		if (renderer.data.ParentisLimb) {
			if (data.parts.Limb.get(renderer.data.ParentID).Renderer != null) {
				data.parts.Limb.get(renderer.data.ParentID).Renderer.addChild(renderer);
			}
		} else {
			if (data.renderParts.get(renderer.data.ParentID) != null) {
				data.renderParts.get(renderer.data.ParentID).addChild(renderer);
			}
		}
		renderer.hasParent = true;
	}
	//Child system
	
	//Fakes
	public static void renderFakePart(AbstractClientPlayerEntity clientplayer, MatrixStack stack, IVertexBuilder buffer, 
			int packedLight, int packedOverlay, PlayerEntity player, GlobalModelData data) {
		if (data.fakeRenderPart != null) {
			data.fakeRenderPart.data = data.parts.FakePart;
			if (data.fakeRenderPart.hasParent == false) {
				data.fakeRenderPart.anchor.X = 0;
				data.fakeRenderPart.anchor.Y = -(float)Math.toRadians(player.renderYawOffset);
				data.fakeRenderPart.anchor.Z = (float)Math.PI;
					
				data.fakeRenderPart.texture = clientplayer.getLocationSkin();
				data.fakeRenderPart.render(stack, buffer, packedLight, packedOverlay);
			} else {
				data.fakeRenderPart.texture = clientplayer.getLocationSkin();
				data.fakeRenderPart.RotAnchor.Y = -1.40f;
			}
		}
	}
	
	public static void generateFakePart(PlayerEntity player, CustomPlayerModel model, 
			RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event, GlobalModelData data) {
		if (data.parts.FakePart != null) {
			ModelPart part = data.parts.FakePart;
			if (data.parts.FakePart.Renderer == null) {
				if (part.type.equalsIgnoreCase("cube") || part.type.equals("")) {
					data.parts.FakePart.Renderer = createFakePartBox(model, player);
				} else {
					data.parts.FakePart.Renderer = createFakePartMesh(model, player);
				}
			}
		}
	}
	//create
	public static PartModelRenderer createFakePartBox(CustomPlayerModel model, PlayerEntity player) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		ModelPart part = data.parts.FakePart;
		
		int ScaleX = part.PreScaleX, ScaleY = part.PreScaleY, ScaleZ = part.PreScaleZ;
		float X = part.PreX, Y = part.PreY, Z = part.PreZ;
		float RotPointX = part.RotPointX, RotPointY = part.RotPointY, RotPointZ = part.RotPointZ;
		
		data.fakeRenderPart = new PartModelRenderer(model);
		data.fakeRenderPart.setTextureOffset(part.PreU, part.PreV);
		
		data.fakeRenderPart.addBox(X - ScaleX / 2, Y - ScaleY / 2, Z - ScaleZ / 2, ScaleX, ScaleY, ScaleZ, 0);
		data.fakeRenderPart.setRotationPoint(RotPointX, RotPointY, RotPointZ);
		
		data.fakeRenderPart.data = data.parts.FakePart;
		addChild(data.fakeRenderPart, model, player);
		return data.fakeRenderPart;
	}
			
	public static PartModelRenderer createFakePartMesh(CustomPlayerModel model, PlayerEntity player) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		ModelPart part = data.parts.FakePart;
		
		int ScaleX = part.PreScaleX, ScaleY = part.PreScaleY, ScaleZ = part.PreScaleZ;
		float X = part.PreX, Y = part.PreY, Z = part.PreZ;
		float RotPointX = part.RotPointX, RotPointY = part.RotPointY, RotPointZ = part.RotPointZ;
		
		data.fakeRenderPart = new PartModelRenderer(model, part.type);
		data.fakeRenderPart.setTextureOffset(part.PreU, part.PreV);
		
		data.fakeRenderPart.addMesh(0, X - ScaleX / 2, Y - ScaleY / 2, Z - ScaleZ / 2, ScaleX, ScaleY, ScaleZ);
		data.fakeRenderPart.setRotationPoint(RotPointX, RotPointY, RotPointZ);
		
		data.fakeRenderPart.data = data.parts.FakePart;
		addChild(data.fakeRenderPart, model, player);
		return data.fakeRenderPart;
	}
	//create
	//Fakes
	
	/*public static void setChildrenRotationPoint(GlobalModelData data, PlayerEntity player, CustomPlayerModel model) {
		for (int i = 0; i < data.renderParts.size(); i++) {
			PartModelRenderer renderer = null;
			ModelRenderer rendererM = null;
			if (data.renderParts.get(i).data.ParentID != -1) {
				if (data.renderParts.get(i).data.ParentisLimb) {
					rendererM = data.parts.Limb.get(data.renderParts.get(i).data.ParentID).ModelRenderer;
				} else {
					renderer = data.parts.Part.get(data.renderParts.get(i).data.ParentID).Renderer;
				}
			}
			if (renderer == null && rendererM == null) continue;
			data.renderParts.get(i).ParentRotPoint.X = (renderer != null ? renderer : rendererM).rotationPointX;
			data.renderParts.get(i).ParentRotPoint.Y = (renderer != null ? renderer : rendererM).rotationPointY;
			data.renderParts.get(i).ParentRotPoint.Z = (renderer != null ? renderer : rendererM).rotationPointZ;
			
		}
	}
	
	public static void setChildrenRotation(GlobalModelData data, PlayerEntity player, CustomPlayerModel model) {
		for (int i = 0; i < data.renderParts.size(); i++) {
			PartModelRenderer renderer = null;
			ModelRenderer rendererM = null;
			if (data.renderParts.get(i).data.ParentID != -1) {
				if (data.renderParts.get(i).data.ParentisLimb) {
					rendererM = data.parts.Limb.get(data.renderParts.get(i).data.ParentID).ModelRenderer;
				} else {
					renderer = data.parts.Part.get(data.renderParts.get(i).data.ParentID).Renderer;
				}
			}
			if (renderer == null && rendererM == null) continue;
			data.renderParts.get(i).ParentRotAngle.X = (renderer != null ? renderer : rendererM).rotateAngleX;
			if (data.renderParts.get(i).data.ParentisLimb && data.PlayingAnim != null) {
				if (data.parts.Limb.get(data.renderParts.get(i).data.ParentID).name != "head") {
					data.renderParts.get(i).ParentRotAngle.Y = (renderer != null ? renderer : rendererM).rotateAngleY + (float)Math.PI;
				}
			} else {
				data.renderParts.get(i).ParentRotAngle.Y = (renderer != null ? renderer : rendererM).rotateAngleY + (float)Math.PI;
			}
			data.renderParts.get(i).ParentRotAngle.Z = -(renderer != null ? renderer : rendererM).rotateAngleZ;
			
		}
	}*/

	
}
