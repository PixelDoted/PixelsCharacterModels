package me.PixelDots.PixelsCharacterModels.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.client.model.ModelLoadJob;
import me.PixelDots.PixelsCharacterModels.util.Utillities;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ModelManager 
{
	
	
	public List<GlobalModelData> Models = new ArrayList<GlobalModelData>();
	
	public static void loadModels() {
		
	}
	
	public static class Model {
		
		//public static float GlobalScale = 1F;
		
		private static GlobalModelData missing = new GlobalModelData();
		
		private static Map<UUID, GlobalModelData> playerModelMap = Maps.newHashMap();
		private static Map<UUID, GlobalModelData> originalModelMap = Maps.newHashMap();
		
		private static Map<String, GlobalModelData> cachedModels = Maps.newHashMap();
		
		private static List<ModelLoadJob> modelsToLoad = new ArrayList<>();
		
		public static void clearModelCache() {
			cachedModels.clear();
			
			for (Map.Entry<UUID, GlobalModelData> entry : playerModelMap.entrySet()) {
				GlobalModelData modeldata = originalModelMap.getOrDefault(entry.getKey(), missing);
				playerModelMap.put(entry.getKey(), modeldata == null ? missing : modeldata);
			}
		}
		
		public static void setModel(UUID uuid,String data) {
			setModel(Utillities.getPlayerFromUUID(uuid), data);
		}
		
		public static void setModel(PlayerEntity player, String data) {
			UUID uuid = player.getUniqueID();
			if(!data.equalsIgnoreCase("")) {
	            if(originalModelMap.containsKey(uuid)) {
	                playerModelMap.put(uuid, originalModelMap.get(uuid));
	                playerModelMap.get(uuid).player = player;
	                
	            }
	        }
	        else if(cachedModels.containsKey(data)) {
	            playerModelMap.put(uuid, cachedModels.get(data));
	            playerModelMap.get(uuid).player = player;
	        }
	        else {
	            modelsToLoad.add(new ModelLoadJob(player, data));
	        }
		}
		
		public static void loadQueuedModels() {
			Iterator<ModelLoadJob> i = modelsToLoad.iterator();
			while(i.hasNext()) {
				ModelLoadJob loadJob = i.next();
				UUID uuid = loadJob.player.getUniqueID();
				GlobalModelData modeldata = new GlobalModelData();//ConvertModelData.fromString(loadJob.data);
				modeldata.fromString(loadJob.data);
				
				cachedModels.put(loadJob.data, modeldata);
				cachedModels.get(loadJob.data).player = loadJob.player;
				
				playerModelMap.put(uuid, modeldata);
				playerModelMap.get(uuid).player = loadJob.player;
				i.remove();
			}
		}
		
		public static void checkModel(AbstractClientPlayerEntity player) {
			if (!(Main.Data.playerData.containsKey(player.getUniqueID()))) {
				return;
			}
	   	    GlobalModelData currentModel = Main.Data.playerData.get(player.getUniqueID()).data;
		    GlobalModelData wantedModel = playerModelMap.get(player.getUniqueID());
		    if (wantedModel != null && currentModel != wantedModel) {
		    	if (!originalModelMap.containsKey(player.getUniqueID()))
		    		originalModelMap.put(player.getUniqueID(), Main.Data.playerData.get(player.getUniqueID()).data);
		    	if (wantedModel.player == player) Main.Data.playerData.get(player.getUniqueID()).data = wantedModel; Main.Data.playerData.get(player.getUniqueID()).username = player.getDisplayName().getString();
		    }
		}
	}
}
