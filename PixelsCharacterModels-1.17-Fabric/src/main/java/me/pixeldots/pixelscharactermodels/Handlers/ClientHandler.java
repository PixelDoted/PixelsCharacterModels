package me.pixeldots.pixelscharactermodels.Handlers;

import java.io.File;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
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
	public int currentPreset = -1;
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
		PixelsCharacterModels.EntityModelList.clear();
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
	
	public void LoadPreset(int id, PlayerEntity entity, PlayerEntityModel<?> model) {
		sendClientMessage("Loading Preset");
		this.currentPreset = id;
		boolean success = PixelsCharacterModels.PresetsData.loadPreset(id, entity, model);
		if (success == false) sendClientMessage("Failed to load Preset with file index: " + id);
		else sendClientMessage("Success fully loaded preset");
	}
	
	public void writePreset(String name, PlayerEntity entity, PlayerEntityModel<?> model) {
		ScaleData scale = ScaleTypes.BASE.getScaleData(PixelsCharacterModels.thisPlayer);
		PresetData data = new PresetData();
		
		data.skinSuffix = FabricOfflineSkins.skinSuffix.get(entity.getGameProfile().getId());
		data.GlobalScale = scale.getTargetScale();
		data.convertModelData(model);
		
		PixelsCharacterModels.PresetsData.writePresetFile(data, name);
	}
	
	public void RenamePreset(int id, String name) {
		File file = PixelsCharacterModels.PresetsData.getPreset(id);
		file.renameTo(new File(file.getParent()+"/"+name+".json"));
	}

	public void DeletePreset(int id) {
		File file = PixelsCharacterModels.PresetsData.getPreset(id);
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
