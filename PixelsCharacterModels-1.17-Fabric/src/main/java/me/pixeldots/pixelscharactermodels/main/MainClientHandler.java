package me.pixeldots.pixelscharactermodels.main;

import java.util.UUID;

import com.google.gson.Gson;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class MainClientHandler {

	public boolean isRegistered = false;
	
	public void Register() {
		System.out.println("Registering Main Client Handler");
		
		ClientPlayNetworking.registerGlobalReceiver(PixelsCharacterModelsMain.NetworkConstants.ping, (client, handler, buf, responseSender) -> {
			PixelsCharacterModels.PCMClient.doesServerUsePCM = true;
		});
		ClientPlayNetworking.registerGlobalReceiver(PixelsCharacterModelsMain.NetworkConstants.ModelData, (client, handler, buf, responseSender) -> {
		    reciveModelData(buf.readString(), client.world.getPlayerByUuid(UUID.fromString(buf.readString())));
		});
		ClientPlayNetworking.registerGlobalReceiver(PixelsCharacterModelsMain.NetworkConstants.requestModelData, (client, handler, buf, responseSender) -> {
			PresetData data = new PresetData();
			
			data.skinSuffix = FabricOfflineSkins.skinSuffix.get(PixelsCharacterModels.thisPlayer.getGameProfile().getId());
			data.convertModelData(PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer));
			
			Gson gson = new Gson();
			sendModelData(gson.toJson(data), buf.readString());
		});
		
		isRegistered = true;
	}

	public void ping() {
		ClientPlayNetworking.send(PixelsCharacterModelsMain.NetworkConstants.ping, PacketByteBufs.empty());
	}

	public void reciveModelData(String json, PlayerEntity player) {
		Gson gson = new Gson(); 
		PresetData data = gson.fromJson(json, PresetData.class);
		data.convertToModel(player, PixelsCharacterModels.EntityModelList.get(player), true);
	}
	
	public void requestModelData() {
		if (PixelsCharacterModels.client.isInSingleplayer()) {
			PresetData data = new PresetData();
			
			data.skinSuffix = FabricOfflineSkins.skinSuffix.get(PixelsCharacterModels.thisPlayer.getGameProfile().getId());
			data.convertModelData(PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer));
			
			Gson gson = new Gson();
			reciveModelData(gson.toJson(data), PixelsCharacterModels.client.player);
		} else {
			ClientPlayNetworking.send(PixelsCharacterModelsMain.NetworkConstants.ServerRequestModelData, PacketByteBufs.empty());
		}
	}
	
	public void sendModelData(String json, String reciverID) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeString(json);
		buf.writeString(reciverID);
		ClientPlayNetworking.send(PixelsCharacterModelsMain.NetworkConstants.ServerModelData, buf);
	}

	public void sendModelData(String json) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeString(json);
		ClientPlayNetworking.send(PixelsCharacterModelsMain.NetworkConstants.ServerModelDataAll, buf);
	}

	public static void changePlayerScale(float scale) {
		if (PixelsCharacterModels.client.isInSingleplayer()) {
			ScaleData data = ScaleTypes.BASE.getScaleData(PixelsCharacterModels.client.player);
			data.setScale(scale);
		} else {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeFloat(scale);
			ClientPlayNetworking.send(PixelsCharacterModelsMain.NetworkConstants.ServerChangePlayerScale, buf);
		}
	}
	
}