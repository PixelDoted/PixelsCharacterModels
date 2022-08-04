package me.pixeldots.pixelscharactermodels;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.files.AnimationPlayer;
import me.pixeldots.pixelscharactermodels.gui.EditorGui;
import me.pixeldots.pixelscharactermodels.gui.PresetsGui;
import me.pixeldots.pixelscharactermodels.network.ClientNetwork;
import me.pixeldots.pixelscharactermodels.other.KeyBindings;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PCMClient {

	public static Minecraft minecraft;

	public static Map<UUID, String> PlayerSkinList = new HashMap<>(); // A map of all Player Skin Suffix's
	public static Map<UUID, AnimationPlayer> EntityAnimationList = new HashMap<>(); // A map of all Entity Animations
	public static ModelPartNames EntityPartNames; // Nme mappings for Entity Modelparts

	public static ClientLevel world;
	public static int WorldTickCount = 0;
	public static boolean offlineskins_loaded;
	
    public void onClientSetup(FMLClientSetupEvent event) {
		minecraft = Minecraft.getInstance(); // get the minecraft instance
		EntityPartNames = new ModelPartNames(); // initialize ModelPart name mappings

		//offlineskins_loaded = MinecraftForge. FabricLoader.getInstance().isModLoaded("offlineskins");
		if (offlineskins_loaded) SkinHelper.registerProviders(false); // register custom skin providers
		ClientNetwork.register(); // register all client receivers

		PCMMain.LOGGER.info("Pixel's Character Models Initialized");
	}

	@SubscribeEvent
	public void onRegisterKeymappings(RegisterKeyMappingsEvent event) {
		KeyBindings.register(event);
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) { // Clear Entity data when Leaving a world
		if (minecraft == null) {
			minecraft = Minecraft.getInstance(); // get the minecraft instance
			return;
		}

		if (minecraft.level == null && world != null) {
			world = null;
			PlayerSkinList.clear();
			EntityAnimationList.clear();
			PCMMain.skinsuffix_data.clear();
			PCMMain.animation_data.clear();
			WorldTickCount = 0;
		} else if (minecraft.level != null && world == null) { 
			world = minecraft.level;
			ClientNetwork.request_skinsuffixs();
		}

		if (event.phase == TickEvent.Phase.START) {
			KeyBindings.tick(minecraft);
		}
	}

	public static void OpenGUI() {
		LivingEntity entity = minecraft.player;

		// If the player is sneaking and looking at an Entity open the GUI for that entity
		if (minecraft.player.isShiftKeyDown()) {
			Entity eResult = minecraft.crosshairPickEntity;
			if (eResult instanceof LivingEntity) {
				entity = (LivingEntity)eResult;
			}
		}

		// open EditorGui instead of PresetsGui if enabled
		if (PCMMain.settings.keybinding_opens_editor) minecraft.setScreen(new EditorGui(entity));
		else minecraft.setScreen(new PresetsGui(entity));
	}

}
