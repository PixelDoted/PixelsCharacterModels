package me.pixeldots.pixelscharactermodels.main;

import com.google.gson.Gson;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

public class MainClientHandler {

	public boolean isRegistered = false;
	
	public void Register() {
		System.out.println("Registering Main Client Handler");
		
		ClientPlayNetworking.registerGlobalReceiver(PixelsCharacterModelsMain.NetworkConstants.ModelData, (client, handler, buf, responseSender) -> {
		    Gson gson = new Gson(); 
		    PresetData data = gson.fromJson(buf.readString(), PresetData.class);
		    data.convertToModel((PlayerEntity)client.targetedEntity, PixelsCharacterModels.EntityModelList.get((PlayerEntity)client.targetedEntity), true);
		});
		ClientPlayNetworking.registerGlobalReceiver(PixelsCharacterModelsMain.NetworkConstants.requestModelData, (client, handler, buf, responseSender) -> {
			PresetData data = new PresetData();
			
			data.skinSuffix = FabricOfflineSkins.skinSuffix.get(PixelsCharacterModels.thisPlayer.getGameProfile().getId());
			data.convertModelData(PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer));
			
			Gson gson = new Gson();
			sendModelData(gson.toJson(data), ((PlayerEntity)client.targetedEntity).getUuidAsString());
		});
		
		isRegistered = true;
	}
	
	public void requestModelData() {
		ClientPlayNetworking.send(PixelsCharacterModelsMain.NetworkConstants.ServerRequestModelData, PacketByteBufs.empty());
	}
	
	public void sendModelData(String json, String reciverID) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeString(json);
		buf.writeString(reciverID);
		ClientPlayNetworking.send(PixelsCharacterModelsMain.NetworkConstants.ServerModelData, buf);
	}
	
}
