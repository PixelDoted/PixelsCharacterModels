package me.pixeldots.pixelscharactermodels;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
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
	
	public static String modVersion = "5R";
	
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
	
	public static void checkForUpdate(UpdateCallback callback) {
		new Thread(() -> {
			String s = "";
			try {
				URL tracker = new URL("https://raw.githubusercontent.com/PixelDoted/PixelsCharacterModels/main/PCM.Update");
				BufferedReader reader = new BufferedReader(new InputStreamReader(tracker.openStream()));
				Object[] version = reader.lines().toArray();
				for (Object obj_ver : version) {
					String ver = (String)obj_ver;
					if (ver.startsWith("Fabric: ")) {
						s = ver.split(": ")[1];
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("(Pixel's Character Models) Failed to collect version checker data");
				System.out.println(e);
				System.out.println("(Pixel's Character Models) Failed to collect version checker data");
			}
			String versionType = s.contains("B") ? "B" : (s.contains("R") ? "R" : "N");
			String userVersionType = modVersion.contains("B") ? "B" : (modVersion.contains("R") ? "R" : "N");
			
			if (VersionIDs.getVersionID(versionType) == VersionIDs.getVersionID(userVersionType)) {
				if (Float.parseFloat(modVersion.replace(versionType, "")) >= Float.parseFloat(s.replace(versionType, ""))) s = "";
			} else if (VersionIDs.getVersionID(versionType) < VersionIDs.getVersionID(userVersionType)) s = "";
			
			if (callback != null) callback.run(s);
		}).start();
	}
	
	public static class VersionIDs {
		public static int B = 0;
		public static int R = 1;
		public static int N = 2;
		
		public static int getVersionID(String v) {
			if (v == "R") return R;
			else if (v == "N") return N;
			return B;
		}
	}

	public interface UpdateCallback { void run(String s); }

}
