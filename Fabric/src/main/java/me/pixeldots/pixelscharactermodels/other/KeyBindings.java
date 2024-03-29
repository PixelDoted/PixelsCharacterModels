package me.pixeldots.pixelscharactermodels.other;

import java.io.File;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.PCMFileSystem;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;

public class KeyBindings {
	
	private static KeyBinding openGUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"pcm.key.opengui", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_R, // The keycode of the key
		"pcm.category" // The translation key of the keybinding's category.
	));
	private static KeyBinding reloadSkins = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"pcm.key.reload_skins", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_KP_1, // The keycode of the key
		"pcm.category" // The translation key of the keybinding's category.
	));
	
	private static KeyBinding Anim1 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("pcm.key.anim1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_1, "category.PixelsCharacterModels"));
	private static KeyBinding Anim2 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("pcm.key.anim2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_2, "category.PixelsCharacterModels"));
	private static KeyBinding Anim3 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("pcm.key.anim3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_3, "category.PixelsCharacterModels"));
	private static KeyBinding Anim4 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("pcm.key.anim4", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_4, "category.PixelsCharacterModels"));
	private static KeyBinding Anim5 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("pcm.key.anim5", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_5, "category.PixelsCharacterModels"));
	
	public static void registerKeyBindings() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    if (openGUI.wasPressed()) PCMClient.OpenGUI();
			if (reloadSkins.wasPressed()) SkinHelper.reloadSkins();
		    
		    if (Anim1.wasPressed()) setAnimation(PCMMain.settings.animations[0], client);
			if (Anim2.wasPressed()) setAnimation(PCMMain.settings.animations[1], client);
			if (Anim3.wasPressed()) setAnimation(PCMMain.settings.animations[2], client);
			if (Anim4.wasPressed()) setAnimation(PCMMain.settings.animations[3], client);
			if (Anim5.wasPressed()) setAnimation(PCMMain.settings.animations[4], client);
		});
	}

	public static void setAnimation(String name, MinecraftClient client) {
		if (name == null) return;
		if (!name.endsWith(".json")) name = name + ".json";

		LivingEntity entity = client.player;
		EntityModel<?> model = PlatformUtils.getModel(entity);
		String current = AnimationHelper.get_current(entity, model);

		File file = Paths.get(".", PCMFileSystem.Animations_Path + File.separator + name).toFile();
		if (!current.equals("")) AnimationHelper.stop(entity, model, true);
		if (!current.equals(file.getName())) AnimationHelper.play(file, entity, model);
	}
}
