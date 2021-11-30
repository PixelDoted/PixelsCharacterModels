package me.PixelDots.PixelsCharacterModels.util.Handlers;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Frames.Frames;
import me.PixelDots.PixelsCharacterModels.KeyBindings.KeyBindings;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PCMInputEventHandler 
{
	
	private KeyBindings getPressedKey() {
		for (KeyBindings key : KeyBindings.values()) {
			if (key.isPressed()) return key;
		}
		return null;
	}
	
	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
		KeyBindings key = getPressedKey();
		Main.GuiSettings.player = Minecraft.getInstance().player;
		if (key != null) {
			switch(key) {
				case CharCreatorMenu:
					Minecraft.getInstance().displayGuiScreen(new PresetsGui());
					break;
				case Anim1BTN:
					UpdatedKeyCode(Main.GuiSettings.player, Main.animations.AnimKey1, Main.animations.Anim1isFrames);
					break;
				case Anim2BTN:
					UpdatedKeyCode(Main.GuiSettings.player, Main.animations.AnimKey2, Main.animations.Anim2isFrames);
					break;
				case Anim3BTN:
					UpdatedKeyCode(Main.GuiSettings.player, Main.animations.AnimKey3, Main.animations.Anim3isFrames);
					break;
				case Anim4BTN:
					UpdatedKeyCode(Main.GuiSettings.player, Main.animations.AnimKey4, Main.animations.Anim4isFrames);
					break;
				case Anim5BTN:
					UpdatedKeyCode(Main.GuiSettings.player, Main.animations.AnimKey5, Main.animations.Anim5isFrames);
					break;
				default:
					break;
			}
		}
	}
	
	public void UpdatedKeyCode(PlayerEntity player, String anim, boolean isframe) {
		if (anim == "") {
			Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(null, "");
		} else {
			boolean FoundBroke = false;
			if (!isframe) {
				for (int i = 0; i < Main.animations.list.size(); i++) {
					if (!(Main.animations.toString(i).matches(Main.Data.playerData.get(player.getUniqueID()).data.PlayingString))) {
						if (Main.animations.list.get(i).id.equalsIgnoreCase(anim)) {
							Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(Main.animations.list.get(i)
									, Main.animations.toString(i));
							Main.runningFrame = -1;
							Main.runningAnimFrame = -1;
							FoundBroke = true;
							break;
						}
					} else {
						FoundBroke = false;
					}
				}
			} else {
				for (int i = 0; i < Main.frames.list.size(); i++) {
					if (!(Main.frames.list.get(i).toString().matches(Main.Data.playerData.get(player.getUniqueID()).data.FrameString))) {
						if (Main.frames.list.get(i).id.equalsIgnoreCase(anim)) {
							String animName = Main.frames.list.get(i).animNames.get(0);
							Frames frame = Main.frames.list.get(i);
							Animation animFrame = frame.anims.containsKey(animName) ? Main.frames.list.get(i).anims.get(Main.frames.list.get(i).animNames.get(0)) : null;
							if (animFrame != null) {
								Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(animFrame
										, animFrame.toString()
										, frame.toString(),
										frame);
								Main.runningFrame = i;
								Main.runningAnimFrame = 0;
								FoundBroke = true;
							}
							break;
						}
					} else {
						FoundBroke = false;
					}
				}
			}
			if (FoundBroke == false) {
				Main.Data.playerData.get(player.getUniqueID()).data.setAnimation(null, "");
			}
		}
		GlobalModelManager.Model.setModel(player, Main.Data.playerData.get(player.getUniqueID()).data);
	}
	
	public void Animate(String s) {
		//playerAnimations("Sit", Minecraft.getMinecraft().player);
	}
	
}
