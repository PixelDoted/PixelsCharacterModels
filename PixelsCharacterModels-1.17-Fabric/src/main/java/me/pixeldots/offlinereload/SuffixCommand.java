package me.pixeldots.offlinereload;

import java.io.File;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SuffixCommand {

	@SuppressWarnings("resource")
	public static void Register(CommandDispatcher<ServerCommandSource> dispatcher) {
		if (dispatcher == null) return;
		dispatcher.register(CommandManager.literal("skinsuffix").executes(context -> {
			FabricOfflineSkins.skinSuffix.put(PixelsCharacterModels.thisPlayer.getGameProfile().getId(), "");
			FabricOfflineSkins.ReloadSkins(MinecraftClient.getInstance());
			
		    return 1;
		}).then(CommandManager.argument("suffix", StringArgumentType.string()).executes(context -> { 
			FabricOfflineSkins.skinSuffix.put(PixelsCharacterModels.thisPlayer.getGameProfile().getId(), StringArgumentType.getString(context, "suffix"));
			FabricOfflineSkins.ReloadSkins(MinecraftClient.getInstance());
			
		    return 1;
		})));
		dispatcher.register(CommandManager.literal("listskins").executes(context -> {
			
			File[] files = new File(MinecraftClient.getInstance().runDirectory+"/cachedImages/skins").listFiles();
			MinecraftClient.getInstance().player.sendMessage(new LiteralText("local skins >"), false);
			for (int i = 0; i < files.length; i++) {
				MinecraftClient.getInstance().player.sendMessage(new LiteralText(files[i].getName()), false);
			}
			MinecraftClient.getInstance().player.sendMessage(new LiteralText("< local skins"), false);
			
			
			return 1;
		}));
	}
	
}