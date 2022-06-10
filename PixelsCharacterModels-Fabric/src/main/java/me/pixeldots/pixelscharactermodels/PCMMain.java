package me.pixeldots.pixelscharactermodels;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class PCMMain implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("pcm");

	public static PCMSettings settings;

	@Override
	public void onInitialize() {
		settings = PCMSettings.load(Paths.get(".", "config/PCM.json"));
	}

}