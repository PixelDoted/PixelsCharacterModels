package me.pixeldots.pixelscharactermodels;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.pixelscharactermodels.gui.EditorGui;
import me.pixeldots.pixelscharactermodels.other.KeyBindings;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;

public class PixelsCharacterModels implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("pcm");
	public static MinecraftClient minecraft;

	public static Map<UUID, String> PlayerSkinList = new HashMap<>();
	public static ModelPartNames EntityPartNames;

	public static PCMSettings settings;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello Fabric world!");
		minecraft = MinecraftClient.getInstance();
		EntityPartNames = new ModelPartNames();
		KeyBindings.registerKeyBindings();

		SkinHelper.reloadSkins();

		settings = PCMSettings.load(Paths.get(".", "config/PCM.json"));
	}

	public static void OpenGUI() {
		minecraft.setScreen(new EditorGui());
	}

}
