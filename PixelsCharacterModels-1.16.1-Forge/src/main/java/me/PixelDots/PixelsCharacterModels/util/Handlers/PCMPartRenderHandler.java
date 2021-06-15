package me.PixelDots.PixelsCharacterModels.util.Handlers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.model.CustomPlayerModel;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import me.PixelDots.PixelsCharacterModels.Network.NetworkPlayerData;
import me.PixelDots.PixelsCharacterModels.client.model.ModelPart;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import me.PixelDots.PixelsCharacterModels.util.Lerp;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderLivingEvent;

public class PCMPartRenderHandler {
	
	//Parts
	
	public static void generateParts(PlayerEntity player, CustomPlayerModel model, RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		if (data.parts.Part.size() != data.parts.PartsCount || data.renderParts.size() != data.parts.PartsCount) { clearRenderParts(Main.Data.playerData.get(player.getUniqueID()), model); }
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
		data.parts.PartsCount = data.parts.Part.size();
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
				addChild(data.renderParts.get(i), data);
				data.renderParts.get(i).anchor.X = 0;
				data.renderParts.get(i).anchor.Z = (float)Math.PI;
				if (data.renderParts.get(i).data.ParentisLimb) {
					if (data.parts.Limb.get(data.renderParts.get(i).data.ParentID).name.equalsIgnoreCase("head")) {
						data.renderParts.get(i).anchor.Y = new Lerp().Linear(data.renderParts.get(i).anchor.Y, (float)Math.toRadians(player.renderYawOffset), .75f);
					} else {
						data.renderParts.get(i).anchor.Y = (float)Math.toRadians(player.renderYawOffset);
					}
				} else {
					data.renderParts.get(i).anchor.Y = (float)Math.toRadians(player.renderYawOffset);
				}
					
				data.renderParts.get(i).texture = clientplayer.getLocationSkin();
<<<<<<< Updated upstream
				//data.renderParts.get(i).anchor.Z = (float)Math.PI;
				data.renderParts.get(i).RotAnchor.Y = -1.40f;
				//data.renderParts.get(i).packedLight = event.getRenderer().getPackedLight(player, event.getPartialRenderTick());
				//data.renderParts.get(i).packedOverlay = OverlayTexture.NO_OVERLAY;
			}
		}
		renderFakePart(clientplayer, stack, buffer, packedLight, packedOverlay, player, data);
=======
				data.renderParts.get(i).render(stack, buffer, packedLight, packedOverlay);
			} /*{
				data.renderParts.get(i).texture = clientplayer.getLocationSkin();
				data.renderParts.get(i).anchor.Z = (float)Math.PI;
				data.renderParts.get(i).RotAnchor.Y = -1.40f;
				//data.renderParts.get(i).packedLight = event.getRenderer().getPackedLight(player, event.getPartialRenderTick());
				//data.renderParts.get(i).packedOverlay = OverlayTexture.NO_OVERLAY;
			}*/
		}
>>>>>>> Stashed changes
	}
		
	public static void clearRenderParts(NetworkPlayerData playerdata, CustomPlayerModel model) {
		GlobalModelData data = playerdata.data;
		for (int i = 0; i < data.parts.Part.size(); i++) {
			data.parts.Part.get(i).Renderer = null;
		}
		data.renderParts.clear();
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
		addChild(data.renderParts.get(i), data);
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
		addChild(data.renderParts.get(i), data);
		return data.renderParts.get(i);
	}
	//create
	//Child system
	public static void addChild(PartModelRenderer renderer, GlobalModelData data) {
		if (renderer.data == null) return;
		if (renderer.data.ParentID == -1) return;
		renderer.hasParent = true;
	}
	//Child system
	
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
			data.renderParts.get(i).ParentRotAngle.Y = (renderer != null ? renderer : rendererM).rotateAngleY + (float)Math.PI;
			data.renderParts.get(i).ParentRotAngle.Z = -(renderer != null ? renderer : rendererM).rotateAngleZ;
			
			data.renderParts.get(i).ParentRotPoint.X = (renderer != null ? renderer : rendererM).rotationPointX;
			data.renderParts.get(i).ParentRotPoint.Y = (renderer != null ? renderer : rendererM).rotationPointY;
			data.renderParts.get(i).ParentRotPoint.Z = (renderer != null ? renderer : rendererM).rotationPointZ;
		}
	}
	
	//reeeeeeeeeee
	/*public static void generateParts(PlayerEntity player, CustomPlayerModel model, RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		GlobalModelData data = Main.Data.playerData.get(player).data;
		ModelParts parts = data.parts;
		
		if (parts.Part.size() == 0) data.renderParts.clear();
		if (parts.Part.size() != data.renderParts.size() && data.renderParts.size() != 0) data.renderParts.clear();
		
		for (int i = 0; i < parts.Part.size(); i++) {
			
			if (i >= parts.Part.size() || i >= data.renderParts.size()) break;
			
			if (parts.Part.get(i).Renderer == null) {
				if (parts.Part.get(i).type.equalsIgnoreCase("cube") || parts.Part.get(i).type == "") {
					createPartBox(i, parts, model, data, player);
				} else {
					createPartMesh(i, parts, model, data, player);
				}
			}
			
			data.renderParts.get(i).data = parts.Part.get(i);
		}
	}
	
	public static void renderParts(PlayerEntity player, CustomPlayerModel model, RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		GlobalModelData data = Main.Data.playerData.get(player).data;
		ModelParts parts = data.parts;
		
		checkLimbChilds(model, player);
		generateParts(player, model, event);
		
		for (int i = 0; i < data.renderParts.size(); i++) {
			if (i == -1 || data.renderParts.size() - 1 < i || data.renderParts.size() == 0) break;
			data.parts.Part.get(i).Renderer = data.renderParts.get(i);
			if (!data.renderParts.get(i).hasParent) {
				AbstractClientPlayerEntity clientplayer = (AbstractClientPlayerEntity)player;
				data.renderParts.get(i).anchor.X = 0;
				data.renderParts.get(i).anchor.Y =  (float)Math.toRadians(player.renderYawOffset);
				data.renderParts.get(i).anchor.Z = (float)Math.PI;
				
				MatrixStack stack = event.getMatrixStack();//null;
				IVertexBuilder buffer = event.getBuffers().getBuffer(model.getRenderType(clientplayer.getLocationSkin()));
				int packedLight = event.getRenderer().getPackedLight(player, event.getPartialRenderTick());
				int packedOverlay = OverlayTexture.NO_OVERLAY;
				
				data.renderParts.get(i).texture = clientplayer.getLocationSkin();
				data.renderParts.get(i).render(stack, buffer, packedLight, packedOverlay);
			} else {
				data.renderParts.get(i).anchor.X = 0;
				data.renderParts.get(i).anchor.Y = 0;
				data.renderParts.get(i).anchor.Z = (float)Math.PI;
			}
		}
	}
	
	//create
	public static void createPartBox(int i, ModelParts parts, CustomPlayerModel model, GlobalModelData data, PlayerEntity player) {
		int ScaleX = parts.Part.get(i).PreScaleX, ScaleY = parts.Part.get(i).PreScaleY, ScaleZ = parts.Part.get(i).PreScaleZ;
		float X = parts.Part.get(i).PreX, Y = parts.Part.get(i).PreY, Z = parts.Part.get(i).PreZ;
		float RotPointX = parts.Part.get(i).RotPointX, RotPointY = parts.Part.get(i).RotPointY, RotPointZ = parts.Part.get(i).RotPointZ;
		
		data.renderParts.add(new PartModelRenderer(model, "cube"));
		data.renderParts.get(i).setTextureOffset(parts.Part.get(i).PreU,parts.Part.get(i).PreV);
		
		data.renderParts.get(i).addBox(X - ScaleX / 2, Y - ScaleY / 2, Z - ScaleZ / 2, ScaleX, ScaleY, ScaleZ, 0);
		data.renderParts.get(i).setRotationPoint(RotPointX, RotPointY, RotPointZ);
		
		parts.Part.get(i).Renderer = data.renderParts.get(i);
		data.renderParts.get(i).data = parts.Part.get(i);
		addChild(data.renderParts.get(i), data);
	}
	
	public static void createPartMesh(int i, ModelParts parts, CustomPlayerModel model, GlobalModelData data, PlayerEntity player) {
		int ScaleX = parts.Part.get(i).PreScaleX, ScaleY = parts.Part.get(i).PreScaleY, ScaleZ = parts.Part.get(i).PreScaleZ;
		float X = parts.Part.get(i).PreX, Y = parts.Part.get(i).PreY, Z = parts.Part.get(i).PreZ;
		float RotPointX = parts.Part.get(i).RotPointX, RotPointY = parts.Part.get(i).RotPointY, RotPointZ = parts.Part.get(i).RotPointZ;
		
		data.renderParts.add(new PartModelRenderer(model, parts.Part.get(i).type));
		data.renderParts.get(i).setTextureOffset(parts.Part.get(i).PreU,parts.Part.get(i).PreV);
		
		data.renderParts.get(i).addMesh(0, X - ScaleX / 2, Y - ScaleY / 2, Z - ScaleZ / 2, ScaleX, ScaleY, ScaleZ, parts.Part.get(i).PreU, parts.Part.get(i).PreV);
		data.renderParts.get(i).setRotationPoint(RotPointX, RotPointY, RotPointZ);
		
		parts.Part.get(i).Renderer = data.renderParts.get(i);
		data.renderParts.get(i).data = parts.Part.get(i);
		addChild(data.renderParts.get(i), data);
	}
	//create
	//Child system
	public static void addChild(PartModelRenderer renderer, GlobalModelData data) {
		if (renderer.data.ParentisLimb) {
			data.parts.Limb.get(renderer.data.ParentID).Renderer.addChild(renderer);
		} else {
			data.parts.Part.get(renderer.data.ParentID).Renderer.addChild(renderer);
		}
	}
	
	public static void checkLimbChilds(CustomPlayerModel model, PlayerEntity player) {
		if (Utillities.childsSize(model.bipedHead) > 0) {
			for (int i = Utillities.childsSize(model.bipedHead) - 1; i > 0; i--) {
				if (Utillities.getChild(playerdata.FakeHead, i) == null) { 
					Utillities.removeChild(model.bipedHead, i);
				}
			}
		}
		if (Utillities.childsSize(model.bipedBody) > 0) {
			for (int i = Utillities.childsSize(model.bipedBody) - 1; i > 0; i--) {
				if (Utillities.getChild(playerdata.FakeBody, i) == null) Utillities.removeChild(model.bipedBody, i);
			}
		}
		if (Utillities.childsSize(model.bipedLeftLeg) > 0) {
			for (int i = Utillities.childsSize(model.bipedLeftLeg) - 1; i > 0; i--) {
				if (Utillities.getChild(playerdata.FakeLeftLeg, i) == null) Utillities.removeChild(model.bipedLeftLeg, i);
			}
		}
		if (Utillities.childsSize(model.bipedLeftArm) > 0) {
			for (int i = Utillities.childsSize(model.bipedLeftArm) - 1; i > 0; i--) {
				if (Utillities.getChild(playerdata.FakeLeftArm, i) == null) Utillities.removeChild(model.bipedLeftArm, i);
			}
		}
		if (Utillities.childsSize(model.bipedRightLeg) > 0) {
			for (int i = Utillities.childsSize(model.bipedRightLeg) - 1; i > 0; i--) {
				if (Utillities.getChild(playerdata.FakeRightLeg, i) == null) Utillities.removeChild(model.bipedRightLeg, i);
			}
		}
		if (Utillities.childsSize(model.bipedRightArm) > 0) {
			for (int i = Utillities.childsSize(model.bipedRightArm) - 1; i > 0; i--) {
				if (Utillities.getChild(playerdata.FakeRightArm, i) == null) Utillities.removeChild(model.bipedRightArm, i);
			}
		}
	}*/
	//Child system
	
}
