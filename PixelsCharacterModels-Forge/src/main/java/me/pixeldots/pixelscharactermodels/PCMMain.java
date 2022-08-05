package me.pixeldots.pixelscharactermodels;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.pixelscharactermodels.network.ClientNetwork;
import me.pixeldots.pixelscharactermodels.network.ServerNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pcm")
public class PCMMain {

	public static final Logger LOGGER = LoggerFactory.getLogger("pcm");

	public static Path SettingsPath;
	public static Path EntityDataPath;
	public static PCMSettings settings; // Client and Server settings

	public static Map<UUID, String> animation_data = new HashMap<>(); // the stored animation data
	public static Map<UUID, String> skinsuffix_data = new HashMap<>(); // the stored skin suffix data

	public static String ProVer = "1"; // Protocol_Version

	public PCMMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::commonSetup);
		
		PCMClient client = new PCMClient();
		modEventBus.addListener(client::onClientSetup);
		MinecraftForge.EVENT_BUS.register(client);
	}

	public void commonSetup(final FMLCommonSetupEvent event) {
		SettingsPath = Paths.get(".", "config/PCM.json"); // set the config path
		settings = PCMSettings.load(SettingsPath); // load settings
		ServerNetwork.register(0); // register all server receivers
		ClientNetwork.register(4); // register all client receivers
	}

}