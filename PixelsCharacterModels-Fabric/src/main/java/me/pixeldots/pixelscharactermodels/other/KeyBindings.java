package me.pixeldots.pixelscharactermodels.other;

import java.io.File;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;

public class KeyBindings {
	
	private static KeyBinding openGUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.pixelscharactermodels.opengui", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_R, // The keycode of the key
		"category.pixelscharactermodels" // The translation key of the keybinding's category.
	));
	private static KeyBinding reloadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.pixelscharactermodels.reload", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_KP_1, // The keycode of the key
		"category.pixelscharactermodels" // The translation key of the keybinding's category.
	));
	
	private static KeyBinding Anim1 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_1, "category.PixelsCharacterModels"));
	/*private static KeyBinding Anim2 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_2, "category.PixelsCharacterModels"));
	private static KeyBinding Anim3 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_3, "category.PixelsCharacterModels"));
	private static KeyBinding Anim4 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim4", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_4, "category.PixelsCharacterModels"));
	private static KeyBinding Anim5 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim5", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_5, "category.PixelsCharacterModels"));*/
	
	public static void registerKeyBindings() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    if (openGUI.wasPressed()) {
		    	PCMClient.OpenGUI();
		    }
			if (reloadKey.wasPressed()) {
				SkinHelper.reloadSkins();
			}
		    
		    if (Anim1.wasPressed()) {
				LivingEntity entity = client.player;
				EntityModel<?> model = PlatformUtils.getModel(entity);
				setAnimation("ScriptedModels.json", entity, model);
		    }
		    /*if (Anim2.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDTwo, PixelsCharacterModels.localData.AnimationTwoisFrames);
		    }
		    if (Anim3.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDThree, PixelsCharacterModels.localData.AnimationThreeisFrames);
		    }
		    if (Anim4.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDFour, PixelsCharacterModels.localData.AnimationFourisFrames);
		    }
		    if (Anim5.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDFive, PixelsCharacterModels.localData.AnimationFiveisFrames);
		    }*/
		});
	}

	public static void setAnimation(String name, LivingEntity entity, EntityModel<?> model) {
		String current = AnimationHelper.get_current(entity, model);
		if (current.equals(name))
			AnimationHelper.stop(entity, model, true);
		else
			AnimationHelper.play(Paths.get(".", "PCM" + File.separator + "Animations" + File.separator + name).toFile(), entity, model);
	}
}
