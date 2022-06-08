package me.pixeldots.pixelscharactermodels.skin;

import java.io.File;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SuffixCommand {

	public static void Register(CommandDispatcher<ServerCommandSource> dispatcher) {
		if (dispatcher == null) return;
		dispatcher.register(CommandManager.literal("skinsuffix").executes(context -> {
			SkinHelper.setSkinSuffix(PixelsCharacterModels.minecraft.player.getGameProfile().getId(), "");
			SkinHelper.ReloadSkins();
			
		    return 1;
		}).then(CommandManager.argument("suffix", StringArgumentType.string()).executes(context -> { 
			SkinHelper.setSkinSuffix(PixelsCharacterModels.minecraft.player.getGameProfile().getId(), StringArgumentType.getString(context, "suffix"));
			SkinHelper.ReloadSkins();
			
		    return 1;
		})));
		dispatcher.register(CommandManager.literal("listskins").executes(context -> {
			
			File[] files = new File(PixelsCharacterModels.minecraft.runDirectory+"/cachedImages/skins").listFiles();
			PixelsCharacterModels.minecraft.player.sendMessage(new LiteralText("local skins >"), false);
			for (File file : files) {
				PixelsCharacterModels.minecraft.player.sendMessage(new LiteralText(file.getName()), false);
			}
			PixelsCharacterModels.minecraft.player.sendMessage(new LiteralText("< local skins"), false);
			
			
			return 1;
		}));
	}
	
}