package me.pixeldots.offlinereload;

import java.io.File;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SuffixCommand {

	@SuppressWarnings("resource")
	public static void Register(CommandDispatcher<ServerCommandSource> dispatcher) {
		if (dispatcher == null) return;
		dispatcher.register(CommandManager.literal("skinsuffix").executes(context -> {
			PixelsCharacterModels.client.setSkinSuffix(PixelsCharacterModels.thisPlayer.getGameProfile().getId(), "");
			PixelsCharacterModels.client.ReloadSkins();
			
		    return 1;
		}).then(CommandManager.argument("suffix", StringArgumentType.string()).executes(context -> { 
			PixelsCharacterModels.client.setSkinSuffix(PixelsCharacterModels.thisPlayer.getGameProfile().getId(), StringArgumentType.getString(context, "suffix"));
			PixelsCharacterModels.client.ReloadSkins();
			
		    return 1;
		})));
		dispatcher.register(CommandManager.literal("listskins").executes(context -> {
			
			File[] files = new File(PixelsCharacterModels.client.minecraft.runDirectory+"/cachedImages/skins").listFiles();
			PixelsCharacterModels.client.minecraft.player.sendMessage(new LiteralText("local skins >"), false);
			for (int i = 0; i < files.length; i++) {
				PixelsCharacterModels.client.minecraft.player.sendMessage(new LiteralText(files[i].getName()), false);
			}
			PixelsCharacterModels.client.minecraft.player.sendMessage(new LiteralText("< local skins"), false);
			
			
			return 1;
		}));
	}
	
}