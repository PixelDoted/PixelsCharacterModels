package me.pixeldots.pixelscharactermodels;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.Animation.PCMFrames;
import me.pixeldots.pixelscharactermodels.GUI.PresetsGui;
import me.pixeldots.pixelscharactermodels.Handlers.ClientHandler;
import me.pixeldots.pixelscharactermodels.Handlers.CommandsHandler;
import me.pixeldots.pixelscharactermodels.Handlers.KeyBindings;
import me.pixeldots.pixelscharactermodels.Handlers.RenderingHandler;
import me.pixeldots.pixelscharactermodels.model.PreviewModelPart;
import me.pixeldots.pixelscharactermodels.utils.GuiData;
import me.pixeldots.pixelscharactermodels.utils.TranslatedText;
import me.pixeldots.pixelscharactermodels.utils.Load.AnimationsSaveData;
import me.pixeldots.pixelscharactermodels.utils.Load.FramesSaveData;
import me.pixeldots.pixelscharactermodels.utils.Load.OtherSaveData;
import me.pixeldots.pixelscharactermodels.utils.Load.PresetsSaveData;
import me.pixeldots.pixelscharactermodels.utils.data.LocalData;
import me.pixeldots.pixelscharactermodels.accessors.MinecraftClientAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;

public class PixelsCharacterModels implements ClientModInitializer {
	
	public static TranslatedText TranslatedText = null;
	public static PlayerEntity thisPlayer = null;
	
	public static AnimationsSaveData AnimationsData = null;
	public static FramesSaveData FramesData = null;
	public static PresetsSaveData PresetsData = null;
	public static OtherSaveData saveData = null;
	
	public static GuiData GuiData = null;
	public static RenderingHandler Rendering = null;
	public static ClientHandler client = null;
	
	public static PCMAnimation playingAnimationData = null;
	public static PCMFrames playingFramesData = null;
	public static String playingAnimation = "";
	public static boolean isPlayingFrames = false;
	
	public static LocalData localData = new LocalData();
	public static PreviewModelPart previewModelPart = null;
	public static Map<UUID, PlayerData> PlayerDataList = new HashMap<>();

	@Override
	public void onInitializeClient() {
		System.out.println("(Pixel's Character Models) Initializing Client");
		
		TranslatedText = new TranslatedText();
		AnimationsData = new AnimationsSaveData();
		FramesData = new FramesSaveData();
		PresetsData = new PresetsSaveData();
		saveData = new OtherSaveData();
		GuiData = new GuiData();
		Rendering = new RenderingHandler();
		client = new ClientHandler();

		saveData.Initialize();
		PresetsData.Initialize();
		AnimationsData.Initialize();
		FramesData.Initialize();
		
		KeyBindings.registerKeyBindings();
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			CommandsHandler.Register(dispatcher);
		});
		ClientTickEvents.END_CLIENT_TICK.register(c -> {
			if (c.player == null && client.isConnectedToWorld) client.onDisconnect();
			else if (c.player != null && !client.isConnectedToWorld) client.onConnect();
		});
		System.out.println("(Pixel's Character Models) Initialized Client");
	}
	
	public static void OpenGUI() {
		client.openScreen(new PresetsGui());
	}

	public static int getCurrentFPS() {
		return ((MinecraftClientAccessor)client.minecraft).getCurrentFPS();
	}

}
