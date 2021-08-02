package me.pixeldots.pixelscharactermodels.Handlers;

import org.lwjgl.glfw.GLFW;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;

public class KeyBindings {
	
	private static KeyBinding openGUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.pfu.openGui", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_R, // The keycode of the key
		"category.PixelsCharacterModels" // The translation key of the keybinding's category.
	));
	private static KeyBinding reloadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.OfflineSkin.Reload", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_KP_1, // The keycode of the key
		"category.OfflineSkin.Keybindings" // The translation key of the keybinding's category.
	));
	
	private static KeyBinding Anim1 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pfu.anim1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_1, "category.PixelsCharacterModels"));
	private static KeyBinding Anim2 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pfu.anim2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_2, "category.PixelsCharacterModels"));
	private static KeyBinding Anim3 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pfu.anim3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_3, "category.PixelsCharacterModels"));
	private static KeyBinding Anim4 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pfu.anim4", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_4, "category.PixelsCharacterModels"));
	private static KeyBinding Anim5 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pfu.anim5", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_5, "category.PixelsCharacterModels"));
	
	public static void registerKeyBindings() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    while (openGUI.wasPressed()) {
		    	PixelsCharacterModels.GuiData.entity = client.player;
		    	PixelsCharacterModels.GuiData.model = PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer);
		    	PixelsCharacterModels.OpenGUI();
		    }
		    while (KeyBindings.reloadKey.wasPressed()) {
            	FabricOfflineSkins.ReloadSkins(MinecraftClient.getInstance());
            }
		    
		    while (Anim1.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDOne, PixelsCharacterModels.localData.AnimationOneisFrames);
		    }
		    while (Anim2.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDTwo, PixelsCharacterModels.localData.AnimationTwoisFrames);
		    }
		    while (Anim3.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDThree, PixelsCharacterModels.localData.AnimationThreeisFrames);
		    }
		    while (Anim4.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDFour, PixelsCharacterModels.localData.AnimationFourisFrames);
		    }
		    while (Anim5.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDFive, PixelsCharacterModels.localData.AnimationFiveisFrames);
		    }
		});
	}
	
	public static void setAnimation(String key, boolean isFrames) {
		if (!isFrames) {
			if (PixelsCharacterModels.playingAnimation != key) {
				PixelsCharacterModels.isPlayingFrames = false;
				PixelsCharacterModels.playingFramesData = null;
	    		if (PixelsCharacterModels.AnimationsData.loadAnimation(key, PixelsCharacterModels.thisPlayer, PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer))) {
		    		PixelsCharacterModels.playingAnimation = key;
		    		PixelsCharacterModels.playingAnimationData = PixelsCharacterModels.PCMClient.currentStoredAnimation;
	    		}
	    	}
	    	else {
	    		PixelsCharacterModels.playingAnimation = "";
	    		PixelsCharacterModels.playingAnimationData = null;
	    	}
		} else {
			if (PixelsCharacterModels.playingFramesData == null || PixelsCharacterModels.playingFramesData.name != key) {
	    		if (PixelsCharacterModels.FramesData.loadFrame(key, PixelsCharacterModels.thisPlayer, PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer))) {
	    			PixelsCharacterModels.isPlayingFrames = true;
	    			PixelsCharacterModels.playingFramesData = PixelsCharacterModels.PCMClient.currentStoredFrames;
	    			PixelsCharacterModels.PCMClient.framesAnimationID = 0;
					//loadAnimationFrames(PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer), PixelsCharacterModels.thisPlayer);
				}
	    	}
	    	else if (PixelsCharacterModels.playingFramesData.name == key) {
	    		PixelsCharacterModels.isPlayingFrames = false;
	    		PixelsCharacterModels.playingFramesData = null;
	    		PixelsCharacterModels.playingAnimation = "";
	    		PixelsCharacterModels.playingAnimationData = null;
	    	}
		}
	}
	
	/*public static void loadAnimationFrames(PlayerEntityModel<?> model, PlayerEntity entity) {
		String s = PixelsCharacterModels.playingFramesData.frames.get(PixelsCharacterModels.PCMClient.framesAnimationID);
		PixelsCharacterModels.AnimationsData.loadAnimation(s, entity, model);
		PixelsCharacterModels.playingAnimation = s;
		PixelsCharacterModels.playingAnimationData = PixelsCharacterModels.PCMClient.currentStoredAnimation;
	}*/
}
