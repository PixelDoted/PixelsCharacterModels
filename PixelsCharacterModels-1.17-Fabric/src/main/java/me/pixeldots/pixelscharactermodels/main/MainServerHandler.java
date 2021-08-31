package me.pixeldots.pixelscharactermodels.main;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class MainServerHandler {

	public boolean isRegistered = false;
	
	public void Register() {
		System.out.println("Registering Main Server Handler");
		
		ServerPlayNetworking.registerGlobalReceiver(PixelsCharacterModelsMain.NetworkConstants.ServerModelData, (server, senderplayer, network, buf, sender) -> {
			for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) senderplayer.world, senderplayer.getBlockPos())) {
	            ServerPlayNetworking.send(player, PixelsCharacterModelsMain.NetworkConstants.ModelData, buf);
	        }
		});
		ServerPlayNetworking.registerGlobalReceiver(PixelsCharacterModelsMain.NetworkConstants.ServerRequestModelData, (server, senderplayer, network, buf, sender) -> {
			for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) senderplayer.world, senderplayer.getBlockPos())) {
	            ServerPlayNetworking.send(player, PixelsCharacterModelsMain.NetworkConstants.requestModelData, buf);
	        }
		});
		
		isRegistered = true;
	}
	
}
