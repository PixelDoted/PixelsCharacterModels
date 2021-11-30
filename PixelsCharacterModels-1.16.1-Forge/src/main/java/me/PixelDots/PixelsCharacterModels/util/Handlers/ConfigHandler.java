package me.PixelDots.PixelsCharacterModels.util.Handlers;

import org.apache.commons.lang3.tuple.Pair;

import me.PixelDots.PixelsCharacterModels.util.Reference;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigHandler {
	
	public static class Client {
		
		public final BooleanValue collisionbox;
		public final BooleanValue eyeheight;
		
		public Client(ForgeConfigSpec.Builder builder) {
			builder.comment("All PCM settings")
				   .push("PCM Settings");
			
			collisionbox = builder
					.comment("Allow's the mod to change the players collision box (Broken)")
					.translation("PCM.config.collisionbox")
					.worldRestart()
					.define("collisionbox", false);
			
			eyeheight = builder
					.comment("Allow's the mod to change the players eye height")
					.translation("PCM.config.eyeheight")
					.worldRestart()
					.define("eyeheight", true);
			
			builder.pop();
		}
	}
	
	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;
	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading event) {
		
	}
	
	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading event) {
		
	}
}
