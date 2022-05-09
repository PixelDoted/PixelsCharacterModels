package me.pixeldots.pixelscharactermodels.Handlers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CommandsHandler {
	
	public static void Register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("PCM").executes((value) -> {
			PixelsCharacterModels.GuiData.entity = PixelsCharacterModels.client.minecraft.player;
	    	PixelsCharacterModels.GuiData.model = PixelsCharacterModels.PlayerDataList.get(PixelsCharacterModels.client.minecraft.player.getUuid()).model;
	    	PixelsCharacterModels.OpenGUI();
			return 1;
		}).then(CommandManager.literal("Animation").then(CommandManager.argument("anim", StringArgumentType.string()).executes((value) -> {
			String animName = StringArgumentType.getString(value, "anim");
			PixelsCharacterModels.client.minecraft.player.sendMessage(Text.of("Animation: " + animName), false);
			return 1;
		}))).then(CommandManager.literal("Preset").then(CommandManager.argument("preset", StringArgumentType.string()).executes((value) -> {
			String presetName = StringArgumentType.getString(value, "preset");
			PixelsCharacterModels.client.minecraft.player.sendMessage(Text.of("Preset: " + presetName), false);
			return 1;
		}))));
	}
	
}
