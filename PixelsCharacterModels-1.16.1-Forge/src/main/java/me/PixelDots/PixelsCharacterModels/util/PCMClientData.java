package me.PixelDots.PixelsCharacterModels.util;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import me.PixelDots.PixelsCharacterModels.KeyBindings.KeyBindings;
import me.PixelDots.PixelsCharacterModels.Network.NetworkPlayerData;
import me.PixelDots.PixelsCharacterModels.util.Handlers.PCMInputEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class PCMClientData 
{
	public Map<UUID, NetworkPlayerData> playerData = Maps.newHashMap();
	
	public void register() {
		registerKeybinds();
	}
	
	private void registerKeybinds() {
		MinecraftForge.EVENT_BUS.register(new PCMInputEventHandler());
		for (KeyBindings key : KeyBindings.values()) {
			ClientRegistry.registerKeyBinding(key.getKeybind());
		}
	}
	
}
