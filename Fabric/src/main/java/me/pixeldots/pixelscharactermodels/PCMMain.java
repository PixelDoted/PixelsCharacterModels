package me.pixeldots.pixelscharactermodels;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.pixelscharactermodels.network.ServerNetwork;
import net.fabricmc.api.ModInitializer;

public class PCMMain implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("pcm");

	public static Path SettingsPath;
	public static Path EntityDataPath;
	public static PCMSettings settings; // Client and Server settings

	public static Map<UUID, String> animation_data = new HashMap<>(); // the stored animation data
	public static Map<UUID, String> skinsuffix_data = new HashMap<>(); // the stored skin suffix data

	@Override
	public void onInitialize() {
		// TODO: support versions older then 1.19.x (JOML)

		SettingsPath = Paths.get(".", "config/PCM.json"); // set the config path
		settings = PCMSettings.load(SettingsPath); // load settings
		ServerNetwork.register(); // register all server receivers
	}

}