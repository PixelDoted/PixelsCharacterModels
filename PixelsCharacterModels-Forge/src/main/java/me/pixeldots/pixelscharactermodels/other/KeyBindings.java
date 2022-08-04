package me.pixeldots.pixelscharactermodels.other;

import java.io.File;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class KeyBindings {
	
	private static KeyMapping openGUI = new KeyMapping(
		"pcm.key.opengui", // The translation key of the keybinding's name
		GLFW.GLFW_KEY_R, // The keycode of the key
		"pcm.category" // The translation key of the keybinding's category.
	);
	private static KeyMapping reloadSkins = new KeyMapping(
		"pcm.key.reload_skins", // The translation key of the keybinding's name
		GLFW.GLFW_KEY_KP_1, // The keycode of the key
		"pcm.category" // The translation key of the keybinding's category.
	);
	
	private static KeyMapping Anim1 = new KeyMapping("pcm.key.anim1", GLFW.GLFW_KEY_KP_1, "category.PixelsCharacterModels");
	private static KeyMapping Anim2 = new KeyMapping("pcm.key.anim2", GLFW.GLFW_KEY_KP_2, "category.PixelsCharacterModels");
	private static KeyMapping Anim3 = new KeyMapping("pcm.key.anim3", GLFW.GLFW_KEY_KP_3, "category.PixelsCharacterModels");
	private static KeyMapping Anim4 = new KeyMapping("pcm.key.anim4", GLFW.GLFW_KEY_KP_4, "category.PixelsCharacterModels");
	private static KeyMapping Anim5 = new KeyMapping("pcm.key.anim5", GLFW.GLFW_KEY_KP_5, "category.PixelsCharacterModels");
	
	public static void register(RegisterKeyMappingsEvent event) {
		event.register(openGUI);
		event.register(reloadSkins);

		event.register(Anim1);
		event.register(Anim2);
		event.register(Anim3);
		event.register(Anim4);
		event.register(Anim5);
	}
	
	public static void tick(Minecraft client) {
		if (openGUI.consumeClick()) PCMClient.OpenGUI();
		if (reloadSkins.consumeClick()) SkinHelper.reloadSkins();
		
		if (Anim1.consumeClick()) setAnimation(PCMMain.settings.animations[0], client);
		if (Anim2.consumeClick()) setAnimation(PCMMain.settings.animations[1], client);
		if (Anim3.consumeClick()) setAnimation(PCMMain.settings.animations[2], client);
		if (Anim4.consumeClick()) setAnimation(PCMMain.settings.animations[3], client);
		if (Anim5.consumeClick()) setAnimation(PCMMain.settings.animations[4], client);
	}

	public static void setAnimation(String name, Minecraft client) {
		if (name == null) return;
		if (!name.endsWith(".json")) name = name + ".json";

		LivingEntity entity = client.player;
		EntityModel<?> model = PlatformUtils.getModel(entity);
		String current = AnimationHelper.get_current(entity, model);

		File file = Paths.get(".", "PCM" + File.separator + "Animations" + File.separator + name).toFile();
		if (!current.equals("")) AnimationHelper.stop(entity, model, true);
		if (!current.equals(file.getName())) AnimationHelper.play(file, entity, model);
	}
}
