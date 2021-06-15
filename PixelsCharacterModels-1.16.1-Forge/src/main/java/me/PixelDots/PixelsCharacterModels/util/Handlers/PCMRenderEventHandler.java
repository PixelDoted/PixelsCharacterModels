package me.PixelDots.PixelsCharacterModels.util.Handlers;

import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent.SetupAngles;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent.Held;
import com.mrcrayfish.obfuscate.client.model.CustomPlayerModel;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Frames.Frames;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.Network.NetworkPlayerData;
import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;
import me.PixelDots.PixelsCharacterModels.client.Frames.FrameOldStat;
import me.PixelDots.PixelsCharacterModels.client.commands.PresetCMD;
import me.PixelDots.PixelsCharacterModels.client.model.ModelPart;
import me.PixelDots.PixelsCharacterModels.client.model.ModelParts;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import me.PixelDots.PixelsCharacterModels.util.AnimUTILS;
import me.PixelDots.PixelsCharacterModels.util.Lerp;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapBool;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class PCMRenderEventHandler 
{
	
	@SubscribeEvent
	public void ThirdItemRenderPre(Held.Pre event) {
		LivingEntity player = event.getEntity();
		if (player instanceof PlayerEntity) {
			if (Main.Data.playerData.containsKey(player.getUniqueID())) {
				HandSide hand = event.getHandSide();
				ModelParts parts = Main.Data.playerData.get(player.getUniqueID()).data.parts;
				ModelPart part = null;
				if (hand == HandSide.RIGHT) {
					part = parts.getLimbfromName("ItemRight");
				} else if (hand == HandSide.LEFT) {
					part = parts.getLimbfromName("ItemLeft");
				}
				if (event.getMatrixStack() != null) {
					event.getMatrixStack().push();
					if (part != null) {
						event.getMatrixStack().scale(part.Scale, part.Scale, part.Scale);
						event.getMatrixStack().translate(-part.X, -part.Z, -part.Y );
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void ThirdItemRenderPost(Held.Post event) {
		LivingEntity player = event.getEntity();
		if (player instanceof PlayerEntity) {
			if (Main.Data.playerData.containsKey(player.getUniqueID())) {
				if (event.getMatrixStack() != null) {
					event.getMatrixStack().pop();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void PlayerLimbAnglesPost(SetupAngles.Post event) {
		CustomPlayerModel model = (CustomPlayerModel)event.getModelPlayer();
		PlayerEntity player = event.getPlayer();
		if (!(Main.Data.playerData.containsKey(event.getPlayer().getUniqueID()))) {
			return;
		}
		checkModelData(player);
		GlobalModelData data = Main.Data.playerData.get(event.getPlayer().getUniqueID()).data;
		@SuppressWarnings("unused")
		NetworkPlayerData network = Main.Data.playerData.get(event.getPlayer().getUniqueID());
		if (data.PlayingAnim != null) {
			Animation anim = data.PlayingAnim;
			boolean LA = false;
			boolean LL = false;
			boolean RA = false;
			boolean RL = false;
			boolean B = false;
			boolean H = false;
			for (int i = 0; i < anim.AngleLimb.size(); i++) {
				if (anim.AngleLimb.get(data.PlayingAnim.AngleNames.get(i)).v == true) {
					if (anim.AngleNames.get(i).matches("leftarm")) LA = true;
					else if (anim.AngleNames.get(i).matches("leftleg")) LL = true;
					else if (anim.AngleNames.get(i).matches("rightarm")) RA = true;
					else if (anim.AngleNames.get(i).matches("rightleg")) RL = true;
					else if (anim.AngleNames.get(i).matches("body")) B = true;
					else if (anim.AngleNames.get(i).matches("head")) H = true;
				}
			}
			if (data.Frame != null) {
				if (data.OldFrameStats.animString != data.PlayingString) {
					for (int i = 0; i < data.OldFrameStats.list.size(); i++) {
						if (data.OldFrameStats.listNames.get(i).matches("leftarm") && LA == false) {
							data.PlayingAnim.setAngle("leftarm", new MapBool(true), new MapVec3(0,0,0));
							LA = true;
						}
						else if (data.OldFrameStats.listNames.get(i).matches("leftleg") && LL == false) {
							data.PlayingAnim.setAngleFake("leftleg", new MapBool(true), new MapVec3(0,0,0));
							LL = true;
						}
						else if (data.OldFrameStats.listNames.get(i).matches("rightarm") && RA == false) {
							data.PlayingAnim.setAngleFake("rightarm", new MapBool(true), new MapVec3(0,0,0));
							RA = true;
						}
						else if (data.OldFrameStats.listNames.get(i).matches("rightleg") && RL == false) {
							data.PlayingAnim.setAngleFake("rightleg", new MapBool(true), new MapVec3(0,0,0));
							RL = true;
						}
						else if (data.OldFrameStats.listNames.get(i).matches("body") && B == false) {
							data.PlayingAnim.setAngleFake("body", new MapBool(true), new MapVec3(0,0,0));
							B = true;
						}
						else if (data.OldFrameStats.listNames.get(i).matches("head") && H == false) {
							data.PlayingAnim.setAngleFake("head", new MapBool(true), new MapVec3(0,0,0));
							H = true;
						}
						else {
							for (int x = 0; x < data.parts.Part.size(); x++) {
								if (data.OldFrameStats.listNames.get(i).equalsIgnoreCase(data.parts.Part.get(x).name) 
										&& !data.PlayingAnim.AngleNames.contains(data.parts.Part.get(x).name)) {
									data.PlayingAnim.setAngleFake(data.parts.Part.get(x).name, new MapBool(false), new MapVec3(0,0,0));
								}
							}
						}
					}
				}
				if (data.Frame.resetnextframe) {
					for (int i = 0; i < data.OldFrameStats.listNames.size(); i++) {
						String id = data.OldFrameStats.listNames.get(i);
						if (data.PlayingAnim.AngleFakes.contains(id)) {
							float XR = data.OldFrameStats.list.get(id).RotateAngle.X;
							float YR = data.OldFrameStats.list.get(id).RotateAngle.Y;
							float ZR = data.OldFrameStats.list.get(id).RotateAngle.Z;
							float XP = data.OldFrameStats.list.get(id).RotationPoint.X;
							float YP = data.OldFrameStats.list.get(id).RotationPoint.Y;
							float ZP = data.OldFrameStats.list.get(id).RotationPoint.Z;
							if (XR == 0 && YR == 0 && ZR == 0) {
								if (data.OldFrameStats.DRP.containsKey(id)) {
									if (XP == data.OldFrameStats.DRP.get(id).X && YP == data.OldFrameStats.DRP.get(id).Y && ZP == data.OldFrameStats.DRP.get(id).Z) {
										data.PlayingAnim.removeAngle(id);
										data.OldFrameStats.list.remove(id);
							 			data.OldFrameStats.listNames.remove(i);
									}
								} else {
									if (XP == 0 && YP == 0 && ZP == 0) {
										data.PlayingAnim.removeAngle(id);
										data.OldFrameStats.list.remove(id);
							 			data.OldFrameStats.listNames.remove(i);
									}
								}
							}
						}
					}
				}
			} else {
				data.OldFrameStats.list.clear();
				data.OldFrameStats.listNames.clear();
				data.PlayingAnim.removeFakes();
			}
			if (LA) { 
				String id = "leftarm";
				ModelRenderer renderer = model.bipedLeftArm;
				if (data.OldFrameStats.animString != data.PlayingString && data.Frame != null && !data.OldFrameStats.list.containsKey(id)) {
					FrameOldStat stat = new FrameOldStat();
					stat.RotateAngle.X = renderer.rotateAngleX; stat.RotateAngle.Y = renderer.rotateAngleY; stat.RotateAngle.Z = renderer.rotateAngleZ;
					stat.RotationPoint.X = renderer.rotationPointX; stat.RotationPoint.Y = renderer.rotationPointY; stat.RotationPoint.Z = renderer.rotationPointZ;
					data.OldFrameStats.list.put(id, stat);
					data.OldFrameStats.listNames.add(id);
				}
				ModelLimbAnim(renderer, anim, data, id);
			}
			if (LL) { 
				String id = "leftleg";
				ModelRenderer renderer = model.bipedLeftLeg;
				if (data.OldFrameStats.animString != data.PlayingString && data.Frame != null && !data.OldFrameStats.list.containsKey(id)) {
					FrameOldStat stat = new FrameOldStat();
					stat.RotateAngle.X = renderer.rotateAngleX; stat.RotateAngle.Y = renderer.rotateAngleY; stat.RotateAngle.Z = renderer.rotateAngleZ;
					stat.RotationPoint.X = renderer.rotationPointX; stat.RotationPoint.Y = renderer.rotationPointY; stat.RotationPoint.Z = renderer.rotationPointZ;
					data.OldFrameStats.list.put(id, stat);
					data.OldFrameStats.listNames.add(id);
				}
				ModelLimbAnim(renderer, anim, data, id);
			}
			if (RA) { 
				String id = "rightarm";
				ModelRenderer renderer = model.bipedRightArm;
				if (data.OldFrameStats.animString != data.PlayingString && data.Frame != null && !data.OldFrameStats.list.containsKey(id)) {
					FrameOldStat stat = new FrameOldStat();
					stat.RotateAngle.X = renderer.rotateAngleX; stat.RotateAngle.Y = renderer.rotateAngleY; stat.RotateAngle.Z = renderer.rotateAngleZ;
					stat.RotationPoint.X = renderer.rotationPointX; stat.RotationPoint.Y = renderer.rotationPointY; stat.RotationPoint.Z = renderer.rotationPointZ;
					data.OldFrameStats.list.put(id, stat);
					data.OldFrameStats.listNames.add(id);
				}
				ModelLimbAnim(renderer, anim, data, id);
			}
			if (RL) { 
				String id = "rightleg";
				ModelRenderer renderer = model.bipedRightLeg;
				if (data.OldFrameStats.animString != data.PlayingString && data.Frame != null && !data.OldFrameStats.list.containsKey(id)) {
					FrameOldStat stat = new FrameOldStat();
					stat.RotateAngle.X = renderer.rotateAngleX; stat.RotateAngle.Y = renderer.rotateAngleY; stat.RotateAngle.Z = renderer.rotateAngleZ;
					stat.RotationPoint.X = renderer.rotationPointX; stat.RotationPoint.Y = renderer.rotationPointY; stat.RotationPoint.Z = renderer.rotationPointZ;
					data.OldFrameStats.list.put(id, stat);
					data.OldFrameStats.listNames.add(id);
				}
				ModelLimbAnim(renderer, anim, data, id);
			}
			if (B) { 
				String id = "body";
				ModelRenderer renderer = model.bipedBody;
				if (data.OldFrameStats.animString != data.PlayingString && data.Frame != null && !data.OldFrameStats.list.containsKey(id)) {
					FrameOldStat stat = new FrameOldStat();
					stat.RotateAngle.X = renderer.rotateAngleX; stat.RotateAngle.Y = renderer.rotateAngleY; stat.RotateAngle.Z = renderer.rotateAngleZ;
					stat.RotationPoint.X = renderer.rotationPointX; stat.RotationPoint.Y = renderer.rotationPointY; stat.RotationPoint.Z = renderer.rotationPointZ;
					data.OldFrameStats.list.put(id, stat);
					data.OldFrameStats.listNames.add(id);
				}
				ModelLimbAnim(renderer, anim, data, id);
			}
			if (H) { 
				String id = "head";
				ModelRenderer renderer = model.bipedHead;
				if (data.OldFrameStats.animString != data.PlayingString && data.Frame != null && !data.OldFrameStats.list.containsKey(id)) {
					FrameOldStat stat = new FrameOldStat();
					stat.RotateAngle.X = renderer.rotateAngleX; stat.RotateAngle.Y = renderer.rotateAngleY; stat.RotateAngle.Z = renderer.rotateAngleZ;
					stat.RotationPoint.X = renderer.rotationPointX; stat.RotationPoint.Y = renderer.rotationPointY; stat.RotationPoint.Z = renderer.rotationPointZ;
					data.OldFrameStats.list.put(id, stat);
					data.OldFrameStats.listNames.add(id);
				}
				ModelLimbAnim(renderer, anim, data, id);
			}
			for (int i = 0; i < data.renderParts.size(); i++) {
				String identity = Main.Data.playerData.get(player.getUniqueID()).data.parts.Part.get(i).name.toLowerCase();
				if (anim.AnglePoses.containsKey(identity)) {
					if (anim.AngleLimb.get(identity).v == false) {
						
						if (data.OldFrameStats.animString != data.PlayingString && data.Frame != null && !data.OldFrameStats.list.containsKey(identity)) {
							FrameOldStat stat = new FrameOldStat();
							stat.RotateAngle.X = data.renderParts.get(i).animRot.X; stat.RotateAngle.Y = data.renderParts.get(i).animRot.Y; stat.RotateAngle.Z = data.renderParts.get(i).animRot.Z;
							stat.RotationPoint.X = data.renderParts.get(i).animPoint.X; stat.RotationPoint.Y = data.renderParts.get(i).animPoint.Y; stat.RotationPoint.Z = data.renderParts.get(i).animPoint.Z;
							data.OldFrameStats.list.put(identity, stat);
							data.OldFrameStats.listNames.add(identity);
						}
						if (data.OldFrameStats.list.containsKey(identity)) {
							data.OldFrameStats.list.get(identity).RotateAngle.X = animLerpX(data, data.OldFrameStats.list.get(identity).RotateAngle.X, identity);
							data.OldFrameStats.list.get(identity).RotateAngle.Y = animLerpY(data, data.OldFrameStats.list.get(identity).RotateAngle.Y, identity);
							data.OldFrameStats.list.get(identity).RotateAngle.Z = animLerpZ(data, data.OldFrameStats.list.get(identity).RotateAngle.Z, identity);
							data.renderParts.get(i).animRot.X = data.OldFrameStats.list.get(identity).RotateAngle.X;
							data.renderParts.get(i).animRot.Y = data.OldFrameStats.list.get(identity).RotateAngle.Y;
							data.renderParts.get(i).animRot.Z = data.OldFrameStats.list.get(identity).RotateAngle.Z;
						} else {
							data.renderParts.get(i).animRot.X = anim.AnglePoses.get(identity).X;
							data.renderParts.get(i).animRot.Y = anim.AnglePoses.get(identity).Y;
							data.renderParts.get(i).animRot.Z = anim.AnglePoses.get(identity).Z;
						}
						if (Math.round(anim.AngleRotPoint.get(identity).X*100) != 0 && Math.round(anim.AngleRotPoint.get(identity).Y*100) != 0 && Math.round(anim.AngleRotPoint.get(identity).Z*100) != 0) {
							if (data.OldFrameStats.list.containsKey(identity)) {
								data.OldFrameStats.list.get(identity).RotationPoint.X = animRotLerpX(data, data.OldFrameStats.list.get(identity).RotationPoint.X, identity);
								data.OldFrameStats.list.get(identity).RotationPoint.Y = animRotLerpY(data, data.OldFrameStats.list.get(identity).RotationPoint.Y, identity);
								data.OldFrameStats.list.get(identity).RotationPoint.Z = animRotLerpZ(data, data.OldFrameStats.list.get(identity).RotationPoint.Z, identity);
								data.renderParts.get(i).animPoint.X = data.OldFrameStats.list.get(identity).RotationPoint.X;
								data.renderParts.get(i).animPoint.Y = data.OldFrameStats.list.get(identity).RotationPoint.Y;
								data.renderParts.get(i).animPoint.Z = data.OldFrameStats.list.get(identity).RotationPoint.Z;
							} else {
								data.renderParts.get(i).animPoint.X = anim.AngleRotPoint.get(identity).X;
								data.renderParts.get(i).animPoint.Y = anim.AngleRotPoint.get(identity).Y;
								data.renderParts.get(i).animPoint.Z = anim.AngleRotPoint.get(identity).Z;
							}
						} else {
							data.renderParts.get(i).animPoint.X = 0;
							data.renderParts.get(i).animPoint.Y = 0;
							data.renderParts.get(i).animPoint.Z = 0;
						}
						
						data.renderParts.get(i).useAnimRot = true;
						data.renderParts.get(i).useAnimPoint = true;
					}
				}
			}
			if (data.OldFrameStats.animString != data.PlayingString) {
				data.OldFrameStats.animString = data.PlayingString;
			}
			if (data.Frame != null) {
				if (data.Frame.Linear) {
					data.OldFrameStats.Translate.X = new Lerp().Linear(data.OldFrameStats.Translate.X, data.PlayingAnim.Translate.X, data.Frame.time);
					data.OldFrameStats.Translate.Y = new Lerp().Linear(data.OldFrameStats.Translate.Y, data.PlayingAnim.Translate.Y, data.Frame.time);
					data.OldFrameStats.Translate.Z = new Lerp().Linear(data.OldFrameStats.Translate.Z, data.PlayingAnim.Translate.Z, data.Frame.time);
					data.OldFrameStats.nextAnim = new Lerp().Linear(data.OldFrameStats.nextAnim, 1, data.Frame.time);
				} else {
					data.OldFrameStats.Translate.X = new Lerp().Curve(data.OldFrameStats.Translate.X, data.PlayingAnim.Translate.X, data.Frame.time);
					data.OldFrameStats.Translate.Y = new Lerp().Curve(data.OldFrameStats.Translate.Y, data.PlayingAnim.Translate.Y, data.Frame.time);
					data.OldFrameStats.Translate.Z = new Lerp().Curve(data.OldFrameStats.Translate.Z, data.PlayingAnim.Translate.Z, data.Frame.time);
					data.OldFrameStats.nextAnim = new Lerp().Curve(data.OldFrameStats.nextAnim, 1, data.Frame.time);
				}
			}
		} else {
			for (int i = 0; i < data.renderParts.size(); i++) {
				if (data.renderParts.get(i).useAnimRot) data.renderParts.get(i).useAnimRot = false;
				if (data.renderParts.get(i).useAnimPoint) data.renderParts.get(i).useAnimPoint = false;
			}
			data.OldFrameStats.list.clear(); 
			data.OldFrameStats.listNames.clear();
		}
		setPartModels(model, player);
		PCMPartRenderHandler.setChildrenRotation(data, player, model);
	}

	
	public void ModelLimbAnim(ModelRenderer model, Animation anim, GlobalModelData data, String id) {
		if (anim.AnglePoses.containsKey(id)) {
			if (data.OldFrameStats.list.containsKey(id)) {
				data.OldFrameStats.list.get(id).RotateAngle.X = animLerpX(data, data.OldFrameStats.list.get(id).RotateAngle.X, id);
				data.OldFrameStats.list.get(id).RotateAngle.Y = animLerpY(data, data.OldFrameStats.list.get(id).RotateAngle.Y, id);
				data.OldFrameStats.list.get(id).RotateAngle.Z = animLerpZ(data, data.OldFrameStats.list.get(id).RotateAngle.Z, id);
				model.rotateAngleX = data.OldFrameStats.list.get(id).RotateAngle.X;
				model.rotateAngleY = data.OldFrameStats.list.get(id).RotateAngle.Y;
				model.rotateAngleZ = data.OldFrameStats.list.get(id).RotateAngle.Z;
			} else {
				model.rotateAngleX = data.PlayingAnim.AnglePoses.get(id).X;
				model.rotateAngleY = data.PlayingAnim.AnglePoses.get(id).Y;
				model.rotateAngleZ = data.PlayingAnim.AnglePoses.get(id).Z;
			}
		}
		if (anim.AngleRotPoint.containsKey(id)) {
			if (Math.round(anim.AngleRotPoint.get(id).X*100) != 0 && Math.round(anim.AngleRotPoint.get(id).Y*100) != 0 && Math.round(anim.AngleRotPoint.get(id).Z*100) != 0) {
				if (data.OldFrameStats.list.containsKey(id)) {
					data.OldFrameStats.list.get(id).RotationPoint.X = animRotLerpX(data, data.OldFrameStats.list.get(id).RotationPoint.X, id);
					data.OldFrameStats.list.get(id).RotationPoint.Y = animRotLerpY(data, data.OldFrameStats.list.get(id).RotationPoint.Y, id);
					data.OldFrameStats.list.get(id).RotationPoint.Z = animRotLerpZ(data, data.OldFrameStats.list.get(id).RotationPoint.Z, id);
					model.rotationPointX = data.OldFrameStats.list.get(id).RotationPoint.X;
					model.rotationPointY = data.OldFrameStats.list.get(id).RotationPoint.Y;
					model.rotationPointZ = data.OldFrameStats.list.get(id).RotationPoint.Z;
				} else {
					model.rotationPointX = data.PlayingAnim.AngleRotPoint.get(id).X;
					model.rotationPointY = data.PlayingAnim.AngleRotPoint.get(id).Y;
					model.rotationPointZ = data.PlayingAnim.AngleRotPoint.get(id).Z;
				}
			}
		}
	}

	
	@SubscribeEvent
	public void ClientDisconnection(ClientPlayerNetworkEvent.LoggedOutEvent event) {
		Main.Data.playerData.clear();
	}
	
	@SubscribeEvent
	@OnlyIn(value = Dist.CLIENT)
	public void ClientTickEvent(TickEvent.ClientTickEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player != null) {
			if (Main.Data.playerData.containsKey(player.getUniqueID())) {
				if (Main.Data.playerData.get(player.getUniqueID()) != null) {
					if (Main.Data.playerData.get(player.getUniqueID()).data.PlayingAnim != null) {
						if (!Main.Data.playerData.get(player.getUniqueID()).data.PlayingAnim.stoponwalk) {
						} else {
							if (player.moveForward == 0 && player.moveStrafing == 0) {
							}
							else {
								if (player.isSneaking() == false) {
									Main.runningFrame = -1;
									Main.runningAnimFrame = -1;
									Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(null, "");
								}
							}
						}
						if (player.isElytraFlying()) {
							Main.runningFrame = -1;
							Main.runningAnimFrame = -1;
							Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(null, "");
						}
					}
					//if (Reference.MAINAUTHER != "PIXELDOTS") { player.sendMessage(new StringTextComponent("This mod is an edit of Pixel's Character Models by PixelDots: http://curseforge.com/minecraft/mc-mods/pixels-character-models")); }
					if (Main.runningFrame != -1 && Main.runningAnimFrame != -1) {
						if (Main.Data.playerData.get(player.getUniqueID()).data.OldFrameStats.nextAnim == 1) {
							Main.Data.playerData.get(player.getUniqueID()).data.OldFrameStats.nextAnim = 0;
							Main.runningAnimFrame++;
							if (Main.runningAnimFrame > Main.frames.list.get(Main.runningFrame).anims.size() - 1) {
								if (Main.frames.list.get(Main.runningFrame).Loop) {
									Main.runningAnimFrame = 0;
									if (Main.Data.playerData.get(player.getUniqueID()).data.Frame.animNames.size() - 1 >= Main.runningAnimFrame) {
										Frames frame = Main.Data.playerData.get(player.getUniqueID()).data.Frame;
										String frameS = Main.Data.playerData.get(player.getUniqueID()).data.FrameString;
										Animation anim = frame.anims.get(frame.animNames.get(Main.runningAnimFrame));
										Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(anim
												, anim.toString()
												, frameS,
												frame);
									}
								}
								if (!Main.frames.list.get(Main.runningFrame).Loop) {
									Main.runningFrame = -1;
									Main.runningAnimFrame = -1;
									Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(null
											, "");
								}
							} else {
								if (Main.Data.playerData.get(player.getUniqueID()).data.Frame.animNames.size() - 1 >= Main.runningAnimFrame) {
									Frames frame = Main.Data.playerData.get(player.getUniqueID()).data.Frame;
									String frameS = Main.Data.playerData.get(player.getUniqueID()).data.FrameString;
									Animation anim = frame.anims.get(frame.animNames.get(Main.runningAnimFrame));
									Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(anim
											, anim.toString()
											, frameS,
											frame);
								}
							}
						}
					}
					GlobalModelManager.Model.setModel(player, Main.Data.playerData.get(player.getUniqueID()).data);
				}
			}
		}
	}
	
	@SubscribeEvent
	@OnlyIn(value = Dist.CLIENT)
	public void renderPlayerName(RenderNameplateEvent event) {
		if (!Main.OtherSaveData.showNameTags) {
			event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
		}
	}
	
	@SubscribeEvent
	@OnlyIn(value = Dist.CLIENT)
	public void PlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		if (Main.Data.playerData.containsKey(event.getPlayer().getUniqueID())) {
			if (Main.Data.playerData.get(event.getPlayer().getUniqueID()).data.player == event.getPlayer()) {
				Main.Data.playerData.remove(event.getPlayer().getUniqueID());
			}
		}
	}
	
	@SubscribeEvent
	public void PlayerTick(PlayerTickEvent event) {
		OtherPlayer(event.player);
	}
	
	@SubscribeEvent
	@OnlyIn(value = Dist.CLIENT)
	public void EntityEyeHeight(EntityEvent.EyeHeight event) {
		if (event.getEntity() instanceof PlayerEntity && ConfigHandler.CLIENT.eyeheight.get()) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			if (Main.Data.playerData.containsKey(player.getUniqueID())) {
				event.setNewHeight(getEyeHeight(player));
			}
		}
	}
	
	public float getEyeHeight(PlayerEntity player) {
		float height = 1.625F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale);
		if (Main.Data.playerData.get(player.getUniqueID()).data.PlayingAnim != null) {
			float anim = Main.Data.playerData.get(player.getUniqueID()).data.PlayingAnim.ColHeight;
			if (anim == 0) {
				if (player.isSneaking()) height = 1.3F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale);
				else if (player.isElytraFlying() || player.isSwimming() || player.isSleeping()) 
					height = 0.25F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale);
				return height;
			}
			height = 1.625F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale / (anim / .875f));
			if (player.isSneaking()) height = 1.3F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale / (anim / .875f));
			else if (player.isElytraFlying() || player.isSwimming() || player.isSleeping()) 
				height = 0.25F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale / (anim / .875f));
		}
		else {
			if (player.isSneaking()) height = 1.3F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale);
			else if (player.isElytraFlying() || player.isSwimming() || player.isSleeping()) 
				height = 0.25F * (Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale);
		}
		return height;
	}
	
	public void OtherPlayer(PlayerEntity player) {
		if (!Main.Data.playerData.containsKey(player.getUniqueID())) {
			return;
		}
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		
		player.entityCollisionReduction = (1-data.GlobalScale)*-1;
		if (player.getEyeHeight() != getEyeHeight(player))
			player.recalculateSize();
	}
	
	@SubscribeEvent
	public void LivingRenderPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			CustomPlayerModel model = (CustomPlayerModel)((PlayerModel<?>) event.getRenderer().getEntityModel());
			
			checkModelData(player);
			event.getMatrixStack().push();
			if (Main.Data.playerData.containsKey(player.getUniqueID())) {
				GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
				float scale = data.GlobalScale;
				event.getMatrixStack().scale(scale, scale, scale);
				if (data.PlayingAnim != null) {
					float x = 0, y = 0, z = 0;
					if (data.Frame != null) {
						x = data.OldFrameStats.Translate.X;
						y = data.OldFrameStats.Translate.Y;
						z = data.OldFrameStats.Translate.Z;
					}
					else { 
						x = data.PlayingAnim.Translate.X;
						y = data.PlayingAnim.Translate.Y;
						z = data.PlayingAnim.Translate.Z;
						data.OldFrameStats.Translate.X = 0;
						data.OldFrameStats.Translate.Y = 0;
						data.OldFrameStats.Translate.Z = 0;
					}
					event.getMatrixStack().translate(x,y,z);
				} else { data.OldFrameStats.Translate.X = 0; data.OldFrameStats.Translate.Y = 0; data.OldFrameStats.Translate.Z = 0; }
				
				setPartModels(model, player);//setPartModels(model, model, player);
				setupConnectionPreset(model, player);
				PCMPartRenderHandler.renderParts(player,model,event);
				PCMLimbRenderHandler.renderFakeLimbs(player, model, event);
				//checkLimbChilds(model, player);
			}
		}
	}
	
	public void setupConnectionPreset(CustomPlayerModel model, PlayerEntity player) {
		if (Main.GuiSettings.SelectedPresetID != -1 && Main.Data.playerData.get(player.getUniqueID()).data.ConnectionPreset == false) {
			PresetCMD.setPreset(player, Main.presets.list.get(Main.GuiSettings.SelectedPresetID).name);
			GlobalModelManager.Model.setModel(player, Main.Data.playerData.get(player.getUniqueID()).data);
			Main.Data.playerData.get(player.getUniqueID()).data.ConnectionPreset = true;
		}
	}
	
	@SubscribeEvent
	public void LivingRenderPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
	        
			if (Main.Data.playerData.containsKey(player.getUniqueID())) {
				@SuppressWarnings("unused")
				GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
				float scale = Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale;
				event.getMatrixStack().scale(-scale, -scale, -scale);
			}
			
			event.getMatrixStack().pop();
		}
	}
	
	//Setup Parts
	
	public void setPartModels(CustomPlayerModel model, PlayerEntity player) {
		GlobalModelData data = Main.Data.playerData.get(player.getUniqueID()).data;
		@SuppressWarnings("unused")
		NetworkPlayerData network = Main.Data.playerData.get(player.getUniqueID());
		if (!(Main.Data.playerData.containsKey(player.getUniqueID()))) return;
		if (!(model.bipedBodyWear instanceof PartModelRenderer)) {
			//model.bipedBody = (ModelRenderer)new PartModelRenderer(model, model.bipedBody, data.parts.getLimbfromName("body"));
			ObfuscationReflectionHelper.setPrivateValue(PlayerModel.class, model, new PartModelRenderer(model, model.bipedBodyWear), "field_178730_v");//"bipedBodyWear");
		}
		
		if (!(model.bipedHeadwear instanceof PartModelRenderer)) {
			//model.bipedHead = (ModelRenderer)new PartModelRenderer(model, model.bipedHead, data.parts.getLimbfromName("head"));
			model.bipedHeadwear = (ModelRenderer)new PartModelRenderer(model, model.bipedHeadwear, data.parts.getLimbfromName("head"));
		}
		
		if (!(model.bipedLeftLegwear instanceof PartModelRenderer)) {
			//model.bipedLeftLeg = (ModelRenderer)new PartModelRenderer(model, model.bipedLeftLeg, data.parts.getLimbfromName("leftleg"));
			ObfuscationReflectionHelper.setPrivateValue(PlayerModel.class, model, new PartModelRenderer(model, model.bipedLeftLegwear), "field_178733_c");//"bipedLeftLegwear");
		}
		
		if (!(model.bipedLeftArmwear instanceof PartModelRenderer)) {
			//model.bipedLeftArm = (ModelRenderer)new PartModelRenderer(model, model.bipedLeftArm, data.parts.getLimbfromName("leftarm"));
			ObfuscationReflectionHelper.setPrivateValue(PlayerModel.class, model, new PartModelRenderer(model, model.bipedLeftArmwear), "field_178734_a");//"bipedLeftArmwear");
		}
		
		if (!(model.bipedRightLegwear instanceof PartModelRenderer)) {
			//model.bipedRightLeg = (ModelRenderer)new PartModelRenderer(model, model.bipedRightLeg, data.parts.getLimbfromName("rightleg"));
			ObfuscationReflectionHelper.setPrivateValue(PlayerModel.class, model, new PartModelRenderer(model, model.bipedRightLegwear), "field_178731_d");//"bipedRightLegwear");
		}
		
		if (!(model.bipedRightArmwear instanceof PartModelRenderer)) {
			//model.bipedRightArm = (ModelRenderer)new PartModelRenderer(model, model.bipedRightArm, data.parts.getLimbfromName("rightarm"));
			ObfuscationReflectionHelper.setPrivateValue(PlayerModel.class, model, new PartModelRenderer(model, model.bipedRightArmwear), "field_178732_b");//"bipedRightArmwear");
		}
		//((PartModelRenderer)model.bipedHead).data = data.parts.getLimbfromName("head");
		//((PartModelRenderer)model.bipedBody).data = data.parts.getLimbfromName("body");
		//((PartModelRenderer)model.bipedLeftLeg).data = data.parts.getLimbfromName("leftleg");
		//((PartModelRenderer)model.bipedLeftArm).data = data.parts.getLimbfromName("leftarm");
		//((PartModelRenderer)model.bipedRightLeg).data = data.parts.getLimbfromName("rightleg");
		//((PartModelRenderer)model.bipedRightArm).data = data.parts.getLimbfromName("rightarm");
		PCMLimbRenderHandler.createFakeLimbs(player, model);
		
		((PartModelRenderer)model.bipedHeadwear).data = data.parts.getLimbfromName("head");
		((PartModelRenderer)model.bipedBodyWear).data = data.parts.getLimbfromName("body");
		((PartModelRenderer)model.bipedLeftLegwear).data = data.parts.getLimbfromName("leftleg");
		((PartModelRenderer)model.bipedLeftArmwear).data = data.parts.getLimbfromName("leftarm");
		((PartModelRenderer)model.bipedRightLegwear).data = data.parts.getLimbfromName("rightleg");
		((PartModelRenderer)model.bipedRightArmwear).data = data.parts.getLimbfromName("rightarm");
		
		data.parts.getLimbfromName("head").ModelRenderer = model.bipedHead;//Main.Data.playerData.get(player).FakeHead;
		data.parts.getLimbfromName("body").ModelRenderer = model.bipedBody;//Main.Data.playerData.get(player).FakeBody;
		data.parts.getLimbfromName("leftleg").ModelRenderer = model.bipedLeftLeg;//Main.Data.playerData.get(player).FakeLeftLeg;
		data.parts.getLimbfromName("leftarm").ModelRenderer = model.bipedLeftArm;//Main.Data.playerData.get(player).FakeLeftArm;
		data.parts.getLimbfromName("rightleg").ModelRenderer = model.bipedRightLeg;//Main.Data.playerData.get(player).FakeRightLeg;
		data.parts.getLimbfromName("rightarm").ModelRenderer = model.bipedRightArm;//Main.Data.playerData.get(player).FakeRightArm;
	}
	
	public void checkModelData(PlayerEntity player) {
		if (Main.Data.playerData.containsKey(player.getUniqueID())) {
			if (!SyncedPlayerData.instance().get(player, Main.MODELDATA).equals(Main.Data.playerData.get(player.getUniqueID()).data.toString())) {
				GlobalModelData data = new GlobalModelData();
				data.fromString(SyncedPlayerData.instance().get(player, Main.MODELDATA));
				data.player = player;
				Main.Data.playerData.get(player.getUniqueID()).data = data;
			}
		} else {
			GlobalModelData data = new GlobalModelData();
			data.fromString(SyncedPlayerData.instance().get(player, Main.MODELDATA));
			data.player = player;
			NetworkPlayerData net = new NetworkPlayerData();
			net.username = player.getDisplayName().getString();
			net.data = data;
			Main.Data.playerData.put(player.getUniqueID(), net);
			
		}
	}
	
	//Animation Utillites
		public float animLerpX(GlobalModelData data, float V, String id) {
			return new AnimUTILS().animLerpX(data, V, id);
		}
		
		public float animLerpY(GlobalModelData data, float V, String id) {
			return new AnimUTILS().animLerpY(data, V, id);
		}
		
		public float animLerpZ(GlobalModelData data, float V, String id) {
			return new AnimUTILS().animLerpZ(data, V, id); 
		}
		
		public float animRotLerpX(GlobalModelData data, float V, String id) {
			return new AnimUTILS().animRotLerpX(data, V, id);
		}
		
		public float animRotLerpY(GlobalModelData data, float V, String id) {
			return new AnimUTILS().animRotLerpY(data, V, id);
		}
		
		public float animRotLerpZ(GlobalModelData data, float V, String id) {
			return new AnimUTILS().animRotLerpZ(data, V, id);
		}
		//Animation Utillites
	
}
