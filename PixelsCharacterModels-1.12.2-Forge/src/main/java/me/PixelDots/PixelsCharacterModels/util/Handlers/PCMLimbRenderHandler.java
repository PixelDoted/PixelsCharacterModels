package me.PixelDots.PixelsCharacterModels.util.Handlers;

import com.mrcrayfish.obfuscate.client.model.CustomPlayerModel;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Network.NetworkPlayerData;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class PCMLimbRenderHandler 
{
	
	public static void createFakeLimbs(PlayerEntity player, CustomPlayerModel model) {
		if (Main.Data.playerData.containsKey(player.getUniqueID())) {
			NetworkPlayerData playerdata = Main.Data.playerData.get(player.getUniqueID());
			if (playerdata.FakeHead == null) {
			    playerdata.FakeHead = new PartModelRenderer(model, 0, 0, 64, 64);
			    playerdata.FakeHead.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0);
			    playerdata.FakeHead.setRotationPoint(0.0F, 0.0F, 0.0F);//-5.0F, 2.0F - 1, 0.0F

				playerdata.FakeHead.uuid = player.getUniqueID();
				playerdata.FakeHead.fakeLimbID = "Head";
				
			    model.bipedHead.addChild(playerdata.FakeHead);
			    ObfuscationReflectionHelper.setPrivateValue(ModelRenderer.class, model.bipedHead, new ObjectArrayList<>(), "field_78804_l");
			}
			if (playerdata.FakeBody == null) {
				playerdata.FakeBody = new PartModelRenderer(model, 16, 16, 64, 64);
				playerdata.FakeBody.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0);
				playerdata.FakeBody.setRotationPoint(0.0F, 0.0F, 0.0F); //5.0F, 2.0F - 1, 0.0F

				playerdata.FakeBody.uuid = player.getUniqueID();
				playerdata.FakeBody.fakeLimbID = "Body";
				
				model.bipedBody.addChild(playerdata.FakeBody);
				ObfuscationReflectionHelper.setPrivateValue(ModelRenderer.class, model.bipedBody, new ObjectArrayList<>(), "field_78804_l");
			}
			
			if (playerdata.FakeLeftArm == null) {
			    playerdata.FakeLeftArm = new PartModelRenderer(model, 32, 48, 64, 64);
			    playerdata.FakeLeftArm.mirror = true;
			    if ((boolean) ObfuscationReflectionHelper.getPrivateValue(PlayerModel.class, model, "field_178735_y")) { //smallArms
			    	playerdata.FakeLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0);
				} else {
					playerdata.FakeLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
				}
			    playerdata.FakeLeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);//-5.0F, 2.0F - 1, 0.0F

				playerdata.FakeLeftArm.uuid = player.getUniqueID();
				playerdata.FakeLeftArm.fakeLimbID = "LeftArm";
				
			    model.bipedLeftArm.addChild(playerdata.FakeLeftArm);
			    ObfuscationReflectionHelper.setPrivateValue(ModelRenderer.class, model.bipedLeftArm, new ObjectArrayList<>(), "field_78804_l");
			}
			if (playerdata.FakeRightArm == null) {
				playerdata.FakeRightArm = new PartModelRenderer(model, 40, 16, 64, 64);
				if ((boolean) ObfuscationReflectionHelper.getPrivateValue(PlayerModel.class, model, "field_178735_y")) { //smallArms
					playerdata.FakeRightArm.addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0);
				} else {
					playerdata.FakeRightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
				}
				playerdata.FakeRightArm.setRotationPoint(0.0F, 0.0F, 0.0F); //5.0F, 2.0F - 1, 0.0F

				playerdata.FakeRightArm.uuid = player.getUniqueID();
				playerdata.FakeRightArm.fakeLimbID = "RightArm";
				
				model.bipedRightArm.addChild(playerdata.FakeRightArm);
				ObfuscationReflectionHelper.setPrivateValue(ModelRenderer.class, model.bipedRightArm, new ObjectArrayList<>(), "field_78804_l");
			}
			
			if (playerdata.FakeLeftLeg == null) {
			    playerdata.FakeLeftLeg = new PartModelRenderer(model, 0, 16, 64, 64);
			    playerdata.FakeLeftLeg.mirror = true;
			    playerdata.FakeLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
			    playerdata.FakeLeftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);//-5.0F, 2.0F - 1, 0.0F

				playerdata.FakeLeftLeg.uuid = player.getUniqueID();
				playerdata.FakeLeftLeg.fakeLimbID = "LeftLeg";
				
			    model.bipedLeftLeg.addChild(playerdata.FakeLeftLeg);
			    ObfuscationReflectionHelper.setPrivateValue(ModelRenderer.class, model.bipedLeftLeg, new ObjectArrayList<>(), "field_78804_l");
			}
			if (playerdata.FakeRightLeg == null) {
				playerdata.FakeRightLeg = new PartModelRenderer(model, 0, 16, 64, 64);
				playerdata.FakeRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
				playerdata.FakeRightLeg.setRotationPoint(0.0F, 0.0F, 0.0F); //5.0F, 2.0F - 1, 0.0F
				
				playerdata.FakeRightLeg.uuid = player.getUniqueID();
				playerdata.FakeRightLeg.fakeLimbID = "RightLeg";
				
				model.bipedRightLeg.addChild(playerdata.FakeRightLeg);
				ObfuscationReflectionHelper.setPrivateValue(ModelRenderer.class, model.bipedRightLeg, new ObjectArrayList<>(), "field_78804_l");
			}
		}
	}
	
	public static void renderFakeLimbs(PlayerEntity player, CustomPlayerModel model, RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		if (Main.Data.playerData.containsKey(player.getUniqueID())) {
			NetworkPlayerData playerdata = Main.Data.playerData.get(player.getUniqueID());
			
			if (playerdata.FakeHead != null) {
				playerdata.FakeHead.data = playerdata.data.parts.getLimbfromName("Head");
				
				playerdata.FakeHead.anchor.Z = (float)-Math.PI;
				playerdata.FakeHead.RotAnchor.Y = -1.40f;
			}
			if (playerdata.FakeBody != null) {
				playerdata.FakeBody.data = playerdata.data.parts.getLimbfromName("Body");
				
				playerdata.FakeBody.anchor.Z = (float)-Math.PI;
				playerdata.FakeBody.RotAnchor.Y = -1.40f;
			}
			
			if (playerdata.FakeLeftArm != null) {
				playerdata.FakeLeftArm.data = playerdata.data.parts.getLimbfromName("LeftArm");
				
				playerdata.FakeLeftArm.anchor.Z = (float)-Math.PI;
				playerdata.FakeLeftArm.RotAnchor.Y = -1.40f;
			}
			if (playerdata.FakeRightArm != null) {
				playerdata.FakeRightArm.data = playerdata.data.parts.getLimbfromName("RightArm");
				
				playerdata.FakeRightArm.anchor.Z = (float)-Math.PI;
				playerdata.FakeRightArm.RotAnchor.Y = -1.40f;
			}
			
			if (playerdata.FakeLeftLeg != null) {
				playerdata.FakeLeftLeg.data = playerdata.data.parts.getLimbfromName("LeftLeg");
				
				playerdata.FakeLeftLeg.anchor.Z = (float)-Math.PI;
				playerdata.FakeLeftLeg.RotAnchor.Y = -1.40f;
			}
			if (playerdata.FakeRightLeg != null) {
				playerdata.FakeRightLeg.data = playerdata.data.parts.getLimbfromName("RightLeg");
				
				playerdata.FakeRightLeg.anchor.Z = (float)-Math.PI;
				playerdata.FakeRightLeg.RotAnchor.Y = -1.40f;
			}
		}
	}
	
}
