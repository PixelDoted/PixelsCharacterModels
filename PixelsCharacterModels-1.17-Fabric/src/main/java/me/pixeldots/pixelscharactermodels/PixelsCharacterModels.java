package me.pixeldots.pixelscharactermodels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.GUI.PresetsGui;
import me.pixeldots.pixelscharactermodels.Handlers.ClientHandler;
import me.pixeldots.pixelscharactermodels.Handlers.CommandsHandler;
import me.pixeldots.pixelscharactermodels.Handlers.KeyBindings;
import me.pixeldots.pixelscharactermodels.Handlers.RenderingHandler;
import me.pixeldots.pixelscharactermodels.model.LocalData;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import me.pixeldots.pixelscharactermodels.utils.GuiData;
import me.pixeldots.pixelscharactermodels.utils.TranslatedText;
import me.pixeldots.pixelscharactermodels.utils.Load.AnimationsSaveData;
import me.pixeldots.pixelscharactermodels.utils.Load.OtherSaveData;
import me.pixeldots.pixelscharactermodels.utils.Load.PresetsSaveData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PixelsCharacterModels implements ClientModInitializer {
	
	public static String modVersion = "1R Fabric";
	
	public static TranslatedText TranslatedText = new TranslatedText();
	public static PlayerEntity thisPlayer = null;
	
	public static AnimationsSaveData AnimationsData = new AnimationsSaveData();
	public static PresetsSaveData PresetsData = new PresetsSaveData();
	public static OtherSaveData saveData = new OtherSaveData();
	
	public static GuiData GuiData = new GuiData();
	public static RenderingHandler Rendering = new RenderingHandler();
	public static ClientHandler PCMClient = new ClientHandler();
	
	public static PCMAnimation playingAnimationData = null;
	public static String playingAnimation = "";
	
	public static LocalData localData = new LocalData();
	public static Map<ModelPart, ModelPartData> dataPackets = new HashMap<ModelPart, ModelPartData>();
	public static Map<PlayerEntity, PlayerEntityModel<?>> EntityModelList = new HashMap<PlayerEntity, PlayerEntityModel<?>>();
	
	public static MinecraftClient client;
	
	@Override
	public void onInitializeClient() {
		System.out.println("(Pixel's Character Models) Initializing Client");
		client = MinecraftClient.getInstance();
		saveData.Initialize();
		PresetsData.Initialize();
		AnimationsData.Initialize();
		
		KeyBindings.registerKeyBindings();
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			CommandsHandler.Register(dispatcher);
		});
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null && PCMClient.isConnectedToWorld) {
				PCMClient.onDisconnect();
			} else if (client.player != null && !PCMClient.isConnectedToWorld) {
				PCMClient.onConnect();
			}
		});
		System.out.println("(Pixel's Character Models) Initialized Client");
	}
	
	public static void OpenGUI() {
		MinecraftClient.getInstance().openScreen(new PresetsGui());
	}
	
	public static String checkForUpdate() {
		String s = "";
		try {
			URL tracker = new URL("https://raw.githubusercontent.com/PixelDoted/PixelsCharacterModels/main/PCM.Update");
			BufferedReader reader = new BufferedReader(new InputStreamReader(tracker.openStream()));
			Object[] version = reader.lines().toArray();
			for (int i = 0; i < version.length; i++) {
				if (((String)version[i]).equalsIgnoreCase(modVersion.split(" ")[1] + ": ")) {
					s = ((String)version[i]).split(": ")[1];
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("(Pixel's Character Models) Failed to collect version checker data");
			System.out.println(e);
			System.out.println("(Pixel's Character Models) Failed to collect version checker data");
		}
		String versionType = s.endsWith("B") ? "B" : "R";
		if (modVersion.endsWith(versionType)) {
			if (Float.parseFloat(modVersion.replace(versionType, "")) >= Float.parseFloat(s.replace(versionType, ""))) return "";
		}
		return s;
	}

}
