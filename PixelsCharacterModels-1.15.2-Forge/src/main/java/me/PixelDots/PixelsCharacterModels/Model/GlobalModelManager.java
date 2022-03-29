package me.PixelDots.PixelsCharacterModels.Model;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.Capabilities.model.IModelPath;
import me.PixelDots.PixelsCharacterModels.Model.Capabilities.model.ModelPath;
import me.PixelDots.PixelsCharacterModels.Model.Capabilities.model.ModelPathProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;

public class GlobalModelManager 
{
	
	public static class Model {
		private static Map<UUID, String> playerModels = Maps.newHashMap();
		
		public static void playerLoggedOut(UUID uuid) {
			playerModels.remove(uuid);
		}
		
		/*public static void sendAllToPlayer(ServerPlayerEntity player, boolean excludeSelf) {
			for (Map.Entry<UUID, String> model : playerModels.entrySet()) {
				if (!(excludeSelf && model.getKey() == player.getUniqueID())) {
					SyncedPlayerData.instance().set(player, Main.MODELDATA, model.getValue());
					//Main.packetNetwork.send(PacketDistributor.PLAYER.with((Supplier<ServerPlayerEntity>) player), new ClientModelPacket(player.getUniqueID().toString(), model.getValue()));
				}
			}
		}*/
		
		public static void setModel(PlayerEntity player,  GlobalModelData GMD) {
			@SuppressWarnings("unused")
			UUID uuid = player.getUniqueID();
			String data = GMD.toString();
			if(player != null) {
				LazyOptional<IModelPath> lazy = player.getCapability(ModelPathProvider.MODEL_LOC, null);
				IModelPath path = lazy.orElse(new ModelPath());
				path.setModel(data);
	        	//((IModelPath)player.getCapability(ModelPathProvider.MODEL_LOC, null)).setModel(data);
	        	if (data.length() > 0) {
	        		SyncedPlayerData.instance().set(player, Main.MODELDATA, data);
	        		//Main.packetNetwork.send(PacketDistributor.ALL.noArg(), new ClientModelPacket(player.getUniqueID().toString(), data));
	                playerModels.put(player.getUniqueID(), data);
	        	}
	        }
	    }
	  
	    public static void setModel(PlayerEntity player, String data) {
	        if(player != null) {
	        	LazyOptional<IModelPath> lazy = player.getCapability(ModelPathProvider.MODEL_LOC, null);
				IModelPath path = lazy.orElse(new ModelPath());
				path.setModel(data);
	        	//((IModelPath)player.getCapability(ModelPathProvider.MODEL_LOC, null)).setModel(data);
	        	if (data.length() > 0) {
	        		SyncedPlayerData.instance().set(player, Main.MODELDATA, data);
	        		//Main.packetNetwork.send(PacketDistributor.ALL.noArg(), new ClientModelPacket(player.getUniqueID().toString(), data));
	                playerModels.put(player.getUniqueID(), data);
	        	}
	        }
	    }
	}
	
	/*public static class Skin {

	    private static Map<UUID, String> playerSkins = Maps.newHashMap();

	    public static void playerLoggedOut(UUID uuid) {
	        playerSkins.remove(uuid);
	    }

	    public static void sendAllToPlayer(ServerPlayerEntity player, boolean excludeSelf) {
	        for(Map.Entry<UUID, String> skin : playerSkins.entrySet()) {
	            if(!(excludeSelf && skin.getKey() == player.getUniqueID())) {
	            	Main.packetNetwork.send(PacketDistributor.PLAYER.with((Supplier<ServerPlayerEntity>) player), new ClientSkinPacket(player.getUniqueID().toString(), skin.getValue(), false));
	            }
	        }
	    }
	  
	    public static void setSkin(PlayerEntity player, String url) {
	        if(player != null) {
	        	LazyOptional<ISkinPath> lazy = player.getCapability(SkinPathProvider.SKIN_LOC, null);
				ISkinPath path = lazy.orElse(new SkinPath());
				path.setSkin(url);
	            //((ISkinPath)player.getCapability(SkinPathProvider.SKIN_LOC, null)).setSkin(url);
	            if(url.length() > 0) {
	            	Main.packetNetwork.send(PacketDistributor.ALL.noArg(), new ClientSkinPacket(player.getUniqueID().toString(), url, false));
	                playerSkins.put(player.getUniqueID(), url);
	            }
	        }
	    }
	}
	
	public static class Elytra {

	    private static Map<UUID, String> playerElytras = Maps.newHashMap();

	    public static void playerLoggedOut(UUID uuid) {
	        playerElytras.remove(uuid);
	    }

	    public static void sendAllToPlayer(ServerPlayerEntity player, boolean excludeSelf) {
	        for(Map.Entry<UUID, String> elytra : playerElytras.entrySet()) {
	            if(!(excludeSelf && elytra.getKey() == player.getUniqueID())) {
	            	Main.packetNetwork.send(PacketDistributor.PLAYER.with((Supplier<ServerPlayerEntity>) player), new ClientElytraPacket(player.getUniqueID().toString(), elytra.getValue(), false));
	            }
	        }
	    }
	  
	    public static void setElytra(PlayerEntity player, String url) {
	        if(player != null) {
	        	LazyOptional<IElytraPath> lazy = player.getCapability(ElytraPathProvider.ELYTRA_LOC, null);
				IElytraPath path = lazy.orElse(new ElytraPath());
				path.setElytra(url);
	            //((IElytraPath)player.getCapability(ElytraPathProvider.ELYTRA_LOC, null)).setElytra(url);
	            if(url.length() > 0) {
	            	Main.packetNetwork.send(PacketDistributor.ALL.noArg(), new ClientElytraPacket(player.getUniqueID().toString(), url, false));
	                playerElytras.put(player.getUniqueID(), url);
	            }
	        }
	    }
	}*/
	
}
