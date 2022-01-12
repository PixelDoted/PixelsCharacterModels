package me.pixeldots.pixelscharactermodels.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class PixelsCharacterModelsMain implements ModInitializer {

	public static MainClientHandler clientHandler = null;
	public static MainServerHandler serverHandler = new MainServerHandler();
	
	@Override
	public void onInitialize() {
		System.out.println("(Pixel's Character Models) Initializing Main");
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			clientHandler = new MainClientHandler();
			clientHandler.Register();
		}
		else {
			serverHandler.Register();
		}
		
		System.out.println("(Pixel's Character Models) Initialized Main");
	}
	
	public static class NetworkConstants {
		public static Identifier ModelData = new Identifier("pixelscharactermodels", "clientmodeldata");
		public static Identifier ServerModelData = new Identifier("pixelscharactermodels", "servermodeldata");
		public static Identifier ServerModelDataAll = new Identifier("pixelscharactermodels", "servermodeldataall");
		
		public static Identifier requestModelData = new Identifier("pixelscharactermodels", "clientrequestmodeldata");
		public static Identifier ServerRequestModelData = new Identifier("pixelscharactermodels", "serverrequestmodeldata");
	
		public static Identifier ServerChangePlayerScale = new Identifier("pixelscharactermodels", "serverchangeplayerscale");
	}
}