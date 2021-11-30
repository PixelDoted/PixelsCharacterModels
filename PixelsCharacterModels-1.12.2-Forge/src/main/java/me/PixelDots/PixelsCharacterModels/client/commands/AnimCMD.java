package me.PixelDots.PixelsCharacterModels.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AnimCMD {
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
				Commands.literal("anim").then(Commands.argument("name", StringArgumentType.greedyString()).executes((executed) -> {
					return setAnimation(executed.getSource().getEntity(), StringArgumentType.getString(executed, "name"));
				}))
		);
	}
	
	public static int setAnimation(Entity entity, String id) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			if (id == "") {//Main.proxy.playerData.get(player).data.PlayingString.contains(anim) || anim == "") {
				Main.Data.playerData.get(player.getUniqueID()).data.PlayingAnim = null;
				Main.Data.playerData.get(player.getUniqueID()).data.PlayingString = "";
				//Main.proxy.playerData.get(player).data.animationID = 0;
			} else {
				boolean FoundBroke = false;
				for (int i = 0; i < Main.animations.list.size(); i++) {
					if (Main.animations.list.get(i).id.matches(id)) {
						if (!(Main.animations.toString(i).matches(Main.Data.playerData.get(player.getUniqueID()).data.PlayingString))) {
							Main.Data.playerData.get(player.getUniqueID()).data.PlayingAnim = Main.animations.list.get(i);
							Main.Data.playerData.get(player.getUniqueID()).data.PlayingString = Main.animations.toString(i);
							FoundBroke = true;
							break;
						} else {
							FoundBroke = false;
							break;
						}
					}
				}
				if (FoundBroke == false) {
					Main.Data.playerData.get(player.getUniqueID()).data.PlayingAnim = null;
					Main.Data.playerData.get(player.getUniqueID()).data.PlayingString = "";
				}
				//Main.proxy.playerData.get(player).data.animationID = anim;
			}
			GlobalModelManager.Model.setModel(player, Main.Data.playerData.get(player.getUniqueID()).data);
		}
		return 1;
	}
	
}