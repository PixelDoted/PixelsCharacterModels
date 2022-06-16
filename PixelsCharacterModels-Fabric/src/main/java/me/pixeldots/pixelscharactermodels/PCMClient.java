package me.pixeldots.pixelscharactermodels;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.pixelscharactermodels.files.AnimationPlayer;
import me.pixeldots.pixelscharactermodels.gui.EditorGui;
import me.pixeldots.pixelscharactermodels.gui.PresetsGui;
import me.pixeldots.pixelscharactermodels.network.ClientNetwork;
import me.pixeldots.pixelscharactermodels.other.KeyBindings;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;

public class PCMClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("pcm");
	public static MinecraftClient minecraft;

	public static Map<UUID, String> PlayerSkinList = new HashMap<>(); // A map of all Player Skin Suffix's
	public static Map<UUID, AnimationPlayer> EntityAnimationList = new HashMap<>(); // A map of all Entity Animations
	public static ModelPartNames EntityPartNames; // Nme mappings for Entity Modelparts

	public static ClientWorld world;
	
	@Override
	public void onInitializeClient() {
		minecraft = MinecraftClient.getInstance(); // get the minecraft instance
		EntityPartNames = new ModelPartNames(); // initialize ModelPart name mappings
		KeyBindings.registerKeyBindings(); // register all key bindings

		SkinHelper.registerProviders(false); // register custom skin providers
		ClientNetwork.register(); // register all client receivers

		ClientTickEvents.END_CLIENT_TICK.register((c) -> { // Clear Entity data when Leaving a world
			if (c.world == null && world != null) {
				world = null;
				PlayerSkinList.clear();
				EntityAnimationList.clear();
			} else world = c.world;
		});
		LOGGER.info("Pixel's Character Models Initialized");
	}

	public static void OpenGUI() {
		LivingEntity entity = minecraft.player;

		// If the player is sneaking and looking at an Entity open the GUI for that entity
		if (minecraft.player.isSneaking()) {
			HitResult result = minecraft.crosshairTarget;
			if (result instanceof EntityHitResult) {
				EntityHitResult eResult = (EntityHitResult)result;
				if (eResult.getEntity() instanceof LivingEntity) {
					entity = (LivingEntity)eResult.getEntity();
				}
			}
		}

		// open EditorGui instead of PresetsGui if enabled
		if (PCMMain.settings.keybinding_opens_editor) minecraft.setScreen(new EditorGui(entity));
		else minecraft.setScreen(new PresetsGui(entity));
	}

}
