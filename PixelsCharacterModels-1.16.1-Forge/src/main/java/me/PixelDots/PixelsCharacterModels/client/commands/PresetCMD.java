package me.PixelDots.PixelsCharacterModels.client.commands;

import java.net.MalformedURLException;
import java.net.URL;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import me.PixelDots.PixelsCharacterModels.Main;
import me.edoren.skin_changer.server.SkinsCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class PresetCMD {
	
	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("preset").then(Commands.argument("name", StringArgumentType.greedyString()).executes((executed) -> {
			return setPreset(executed.getSource().getEntity(), StringArgumentType.getString(executed, "name"));
		}));
	}
	
	public static int setPreset(Entity entity, String id) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			if (Main.presets.getPresetfromName(id) == null || Main.presets.getPresetIDfromName(id) == -1) return 0;
			if (Main.Data.playerData.containsKey(Main.GuiSettings.player.getUniqueID())) {
				if (Main.OtherSaveData.showLoadingDatainChat) player.sendMessage(new StringTextComponent("Loading preset..."));
				
				Main.Data.playerData.get(player.getUniqueID()).data = Main.presets.getPresetfromName(id).data;
				//Main.presets.lockedin = Main.presets.getPresetIDfromName(id);
				Main.GuiSettings.SelectedPresetID = Main.presets.getPresetIDfromName(id);
				/* Skin Texture */
				String skin = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.ClientMemorySkinTexture;
				/*GlobalModelManager.Skin.*/setSkin(skin, player);
				/* Elytra Texture */
				String elytra = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.ClientMemoryElytraTexture;
				/*GlobalModelManager.Elytra.*/setElytra(elytra, player);
				String LoadedTXT = "";
				if (Main.Data.playerData.get(player.getUniqueID()).data == Main.presets.getPresetfromName(id).data) 
				{ LoadedTXT = "Loaded preset..."; }
				else { LoadedTXT = "Failed to load preset..."; }
				if (Main.OtherSaveData.showLoadingDatainChat) {
					player.sendMessage(new StringTextComponent(LoadedTXT));
				}
			}
			
		}
		return 1;
	}
	
	public static void setElytra(String url, PlayerEntity player) {
		if (url == "") return;
		try {
			SkinsCommand.setPlayerCapeByURL(player, player, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}   
	}
	
	public static void setSkin(String url, PlayerEntity player) {
		if (url == "") return;
		try {
			SkinsCommand.setPlayerSkinByURL(player, player, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}