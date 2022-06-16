package me.pixeldots.pixelscharactermodels;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
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

	public static Map<UUID, String> PlayerSkinList = new HashMap<>();
	public static Map<UUID, AnimationPlayer> EntityAnimationList = new HashMap<>();
	public static ModelPartNames EntityPartNames;
	
	@Override
	public void onInitializeClient() {
		LOGGER.info("Pixel's Character Models Version 2 Initialized");
		minecraft = MinecraftClient.getInstance();
		EntityPartNames = new ModelPartNames();
		KeyBindings.registerKeyBindings();

		SkinHelper.registerProviders(false);
		ClientNetwork.register();
	}

	public static void OpenGUI() {
		LivingEntity entity = minecraft.player;

		if (minecraft.player.isSneaking()) {
			HitResult result = minecraft.crosshairTarget;
			if (result instanceof EntityHitResult) {
				EntityHitResult eResult = (EntityHitResult)result;
				if (eResult.getEntity() instanceof LivingEntity) {
					entity = (LivingEntity)eResult.getEntity();
				}
			}
		}

		if (PCMMain.settings.keybinding_opens_editor) minecraft.setScreen(new EditorGui(entity));
		else minecraft.setScreen(new PresetsGui(entity));
	}

}
