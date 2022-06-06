package me.pixeldots.pixelscharactermodels;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PixelsCharacterModels implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("pcm");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}
