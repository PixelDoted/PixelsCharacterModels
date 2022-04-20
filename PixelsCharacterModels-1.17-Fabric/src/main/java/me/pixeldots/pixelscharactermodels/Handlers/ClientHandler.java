package me.pixeldots.pixelscharactermodels.Handlers;

import java.io.File;
import java.util.UUID;

import lain.mods.skins.api.SkinProviderAPI;
import lain.mods.skins.impl.PlayerProfile;
import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.Animation.PCMFrames;
import me.pixeldots.pixelscharactermodels.main.PixelsCharacterModelsMain;
import me.pixeldots.pixelscharactermodels.utils.data.AnimationData;
import me.pixeldots.pixelscharactermodels.utils.data.FramesData;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class ClientHandler {
	
	public MinecraftClient minecraft;
	public boolean isConnectedToWorld = false;
	public boolean doesServerUsePCM = false;

	public PCMAnimation currentStoredAnimation = null;
	public String currentPreset = "";
	public int playingAnimation = -1;

	public PCMFrames currentStoredFrames = null;
	public int framesAnimationID = -1;
	public boolean isPlayingFrames = false;
	
	public ClientHandler() {
		minecraft = MinecraftClient.getInstance();
	}

	public void onDisconnect() {
		PixelsCharacterModels.saveData.Save();
		PixelsCharacterModels.dataPackets.clear();
		PixelsCharacterModels.PlayerDataList.clear();
		PixelsCharacterModels.thisPlayer = null;
		isConnectedToWorld = false;
		doesServerUsePCM = false;
	}
	
	public void onConnect() {
		isConnectedToWorld = true;
		PixelsCharacterModels.thisPlayer = minecraft.player;
		if (minecraft.isInSingleplayer()) doesServerUsePCM = true;
		else PixelsCharacterModelsMain.clientHandler.ping();
	}

	public void setSkinSuffix(UUID uuid, String suffix) {
		if (PixelsCharacterModels.PlayerDataList.containsKey(uuid))
			PixelsCharacterModels.PlayerDataList.get(uuid).skinSuffix = suffix;
		else PixelsCharacterModels.PlayerDataList.put(uuid, new PlayerData(suffix));
	}

	public void ReloadSkins() {
    	PixelsCharacterModels.client.sendClientMessage("Reloading Skins");
    	for (PlayerEntity player : minecraft.world.getPlayers()) {
            SkinProviderAPI.SKIN.getSkin(PlayerProfile.wrapGameProfile(player.getGameProfile()));
            SkinProviderAPI.CAPE.getSkin(PlayerProfile.wrapGameProfile(player.getGameProfile()));
            PixelsCharacterModels.client.sendClientMessage("Reloaded skin for " + player.getDisplayName().asString());
        }
    	FabricOfflineSkins.reloadConfig();
    	PixelsCharacterModels.client.sendClientMessage("Reloaded Skins");
    }
	
	public void LoadPreset(String path, PlayerEntity entity, PlayerEntityModel<?> model) {
		sendClientMessage("Loading Preset");
		this.currentPreset = path;
		boolean success = PixelsCharacterModels.PresetsData.loadPreset(path, entity, model);
		if (success == false) {
			sendClientMessage("Failed to load Preset with path: " + path); 
			return;
		}

		sendClientMessage("Success fully loaded preset");
		PixelsCharacterModels.localData.lastUsedPreset = path;
	}
	
	public void writePreset(String path, PlayerEntity entity, PlayerEntityModel<?> model) {
		if (path.endsWith("/")) {
			sendClientMessage("Creating a preset requires a name");
			return;
		}
		if (!path.endsWith(".json")) path = path + ".json";

		ScaleData scale = ScaleTypes.BASE.getScaleData(PixelsCharacterModels.thisPlayer);
		PresetData data = new PresetData();
		
		data.skinSuffix = PixelsCharacterModels.PlayerDataList.get(entity.getGameProfile().getId()).skinSuffix;
		data.GlobalScale = scale.getTargetScale();
		data.convertModelData(model);
		
		PixelsCharacterModels.PresetsData.writePresetFile(data, path);
	}
	
	public void RenamePreset(String path, String name) {
		if (name == null || name.equals("")) {
			sendClientMessage("Cannot rename preset to: \"" + name + "\"");
			return;
		}

		File file = PixelsCharacterModels.PresetsData.getPreset(path);
		file.renameTo(new File(file.getParent()+File.separator+name+".json"));
	}

	public void DeletePreset(String path) {
		File file = PixelsCharacterModels.PresetsData.getPreset(path);
		if (file.delete()) 
			sendClientMessage("Deleted Preset");
		else
			sendClientMessage("Failed to Delete Preset");
	}
	
	//Animation
	public void LoadAnimation(int id, PlayerEntity entity, PlayerEntityModel<?> model) {
		sendClientMessage("Loading Animation");
		this.playingAnimation = id;
		this.isPlayingFrames = false;
		boolean success = PixelsCharacterModels.AnimationsData.loadAnimation(id, entity, model);
		if (success == false) sendClientMessage("Failed to load Animation with file index: " + id);
		else sendClientMessage("Success fully loaded Animation");
	}
	
	public void writeAnimation(String name, PlayerEntity entity, PlayerEntityModel<?> model) {
		if (this.currentStoredAnimation == null) return;
		AnimationData data = this.currentStoredAnimation.convertToData(model);
		PixelsCharacterModels.AnimationsData.writeAnimationFile(data, name);
	}
	
	public void DeleteAnimation(int id) {
		File file = PixelsCharacterModels.AnimationsData.getAnimation(id);
		if (file.delete()) 
			sendClientMessage("Deleted Animation");
		else
			sendClientMessage("Failed to Delete Animation");
	}
	//Animation
	//Frames
	public void LoadFrames(int id, PlayerEntity entity, PlayerEntityModel<?> model) {
		sendClientMessage("Loading Frames");
		this.playingAnimation = id;
		this.isPlayingFrames = true;
		boolean success = PixelsCharacterModels.FramesData.loadFrame(id, entity, model);
		if (success == false) sendClientMessage("Failed to load Frames with file index: " + id);
		else sendClientMessage("Success fully loaded Frames");
	}
	
	public void writeFrames(String name, PlayerEntity entity, PlayerEntityModel<?> model) {
		if (this.currentStoredFrames == null) return;
		FramesData data = this.currentStoredFrames.convertToData();
		PixelsCharacterModels.FramesData.writeFramesFile(data, name);
	}
	
	public void DeleteFrames(int id) {
		File file = PixelsCharacterModels.FramesData.getFrame(id);
		if (file.delete()) 
			sendClientMessage("Deleted Frames");
		else
			sendClientMessage("Failed to Delete Frames");
	}
	//Frames
	public void sendClientMessage(String message) {
		if (PixelsCharacterModels.localData.showUpdateMessage)
			minecraft.player.sendMessage(new LiteralText(message), false);
	}

    public void openScreen(Screen screen) {
		minecraft.openScreen(screen);
    }
	
}
