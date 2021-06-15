package me.pixeldots.pixelscharactermodels.Handlers;

import java.io.File;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public class ClientHandler {
	
	public boolean isConnectedToWorld = false;
	public int currentPreset = -1;
	
	public void onDisconnect() {
		PixelsCharacterModels.saveData.Save();
		PixelsCharacterModels.dataPackets.clear();
		PixelsCharacterModels.EntityModelList.clear();
		PixelsCharacterModels.thisPlayer = null;
		isConnectedToWorld = false;
	}
	
	public void onConnect() {
		isConnectedToWorld = true;
		PixelsCharacterModels.thisPlayer = PixelsCharacterModels.client.player;
	}
	
	public void LoadPreset(int id, PlayerEntity entity, PlayerEntityModel<?> model) {
		sendClientMessage("Loading Preset");
		this.currentPreset = id;
		boolean success = PixelsCharacterModels.PresetsData.loadPreset(id, entity, model);
		if (PixelsCharacterModels.localData.showUpdateMessage) {
			if (success == false) sendClientMessage("Failed to load Preset with file index: " + id);
			else sendClientMessage("Success fully loaded preset");
		}
	}
	
	public void writePreset(String name, PlayerEntity entity, PlayerEntityModel<?> model) {
		ScaleData scale = ScaleType.BASE.getScaleData(PixelsCharacterModels.thisPlayer);
		PresetData data = new PresetData();
		
		data.skinSuffix = FabricOfflineSkins.skinSuffix;
		data.GlobalScale = scale.getTargetScale();
		data.convertModelData(model);
		
		PixelsCharacterModels.PresetsData.writePresetFile(data, name);
	}
	
	public void DeletePreset(int id) {
		File file = PixelsCharacterModels.PresetsData.getPreset(id);
		if (file.delete()) 
			sendClientMessage("Deleted Preset");
		else
			sendClientMessage("Failed to Delete Preset");
	}
	
	public void sendClientMessage(String message) {
		if (PixelsCharacterModels.localData.showUpdateMessage)
			MinecraftClient.getInstance().player.sendMessage(new LiteralText(message), false);
	}
	
}
