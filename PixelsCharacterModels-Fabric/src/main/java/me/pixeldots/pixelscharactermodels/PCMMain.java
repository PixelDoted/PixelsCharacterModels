package me.pixeldots.pixelscharactermodels;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.pixelscharactermodels.network.ServerNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

public class PCMMain implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("pcm");

	public static PCMSettings settings; // Client and Server settings

	@Environment(EnvType.SERVER) 
	public static Map<UUID, String> animation_data = new HashMap<>(); // the stored animation data
	@Environment(EnvType.SERVER) 
	public static Map<UUID, String> skinsuffix_data = new HashMap<>(); // the stored skin suffix data

	@Override
	public void onInitialize() {
		settings = PCMSettings.load(Paths.get(".", "config/PCM.json")); // load settings
		ServerNetwork.register(); // register all server receivers
	}

}