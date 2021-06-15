package me.pixeldots.pixelscharactermodels.Handlers;

import org.lwjgl.glfw.GLFW;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

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
		    	if (PixelsCharacterModels.playingAnimation != PixelsCharacterModels.localData.AnimationIDOne)
		    		PixelsCharacterModels.playingAnimation = PixelsCharacterModels.localData.AnimationIDOne;
		    	else PixelsCharacterModels.playingAnimation = "";
		    }
		    while (Anim2.wasPressed()) {
		    	if (PixelsCharacterModels.playingAnimation != PixelsCharacterModels.localData.AnimationIDTwo)
		    		PixelsCharacterModels.playingAnimation = PixelsCharacterModels.localData.AnimationIDTwo;
		    	else PixelsCharacterModels.playingAnimation = "";
		    }
		    while (Anim3.wasPressed()) {
		    	if (PixelsCharacterModels.playingAnimation != PixelsCharacterModels.localData.AnimationIDThree)
		    		PixelsCharacterModels.playingAnimation = PixelsCharacterModels.localData.AnimationIDThree;
		    	else PixelsCharacterModels.playingAnimation = "";
		    }
		    while (Anim4.wasPressed()) {
		    	if (PixelsCharacterModels.playingAnimation != PixelsCharacterModels.localData.AnimationIDFour)
		    		PixelsCharacterModels.playingAnimation = PixelsCharacterModels.localData.AnimationIDFour;
		    	else PixelsCharacterModels.playingAnimation = "";
		    }
		    while (Anim5.wasPressed()) {
		    	if (PixelsCharacterModels.playingAnimation != PixelsCharacterModels.localData.AnimationIDFive)
		    		PixelsCharacterModels.playingAnimation = PixelsCharacterModels.localData.AnimationIDFive;
		    	else PixelsCharacterModels.playingAnimation = "";
		    }
		});
	}
}
