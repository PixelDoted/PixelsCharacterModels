package me.PixelDots.PixelsCharacterModels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mrcrayfish.obfuscate.common.data.Serializers;
import com.mrcrayfish.obfuscate.common.data.SyncedDataKey;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;

import me.PixelDots.PixelsCharacterModels.Frames.FramesController;
import me.PixelDots.PixelsCharacterModels.client.UpdateTracker;
import me.PixelDots.PixelsCharacterModels.client.Animations.playerAnimations;
import me.PixelDots.PixelsCharacterModels.client.Presets.PresetController;
import me.PixelDots.PixelsCharacterModels.client.commands.AnimCMD;
import me.PixelDots.PixelsCharacterModels.client.commands.PresetCMD;
import me.PixelDots.PixelsCharacterModels.client.gui.GuiSettings;
import me.PixelDots.PixelsCharacterModels.util.OtherSaveData;
import me.PixelDots.PixelsCharacterModels.util.PCMClientData;
import me.PixelDots.PixelsCharacterModels.util.Reference;
import me.PixelDots.PixelsCharacterModels.util.Handlers.ConfigHandler;
import me.PixelDots.PixelsCharacterModels.util.Handlers.PCMRenderEventHandler;
import me.edoren.skin_changer.SkinChanger;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MOD_ID)
public class Main
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static PCMClientData Data = new PCMClientData();
    public static GuiSettings GuiSettings = new GuiSettings();
    public static UpdateTracker updateTracker = new UpdateTracker();
    
    public static playerAnimations animations = new playerAnimations();
    public static PresetController presets = new PresetController();
    public static OtherSaveData OtherSaveData = new OtherSaveData();
    public static FramesController frames = new FramesController();
    
	public static int runningFrame = -1;
	public static int runningAnimFrame = -1;
    
    public static SkinChanger skinchanger;
    
    //Networking
    public static final SyncedDataKey<String> MODELDATA = SyncedDataKey.builder(Serializers.STRING)
    		.id(new ResourceLocation(Reference.MOD_ID, "pcm"))
    		.defaultValueSupplier(() -> "")
    		.build();
    /*private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel packetNetwork = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Reference.MOD_ID, "pcm"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );*/
    
    //Networking

    public Main() {
    	ModLoadingContext.get().registerConfig(Type.CLIENT, ConfigHandler.CLIENT_SPEC, "PCM-client.toml");
    	
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        skinchanger = new SkinChanger();
    }

    private void setup(final FMLCommonSetupEvent event) //Preinit
    {
        SetupNetworking();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	Data.register();
		//get Data
		animations.LoadAnimations();
        frames.LoadFrames();
        
        frames.LoadFrameAnims();
        //get Data
    }

    private void enqueueIMC(final InterModEnqueueEvent event) //Init
    {
        //InterModComms.sendTo("PCM", "", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) //Postinit
    {
    	MinecraftForge.EVENT_BUS.register(new PCMRenderEventHandler());
    	//MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    }
    
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    	event.getCommandDispatcher().register((LiteralArgumentBuilder<CommandSource>) PresetCMD.register(event.getCommandDispatcher()));
    	event.getCommandDispatcher().register((LiteralArgumentBuilder<CommandSource>) AnimCMD.register(event.getCommandDispatcher()));
        LOGGER.info("[PCM] server starting");
    }
    
    public void SetupNetworking() {
    	SyncedPlayerData.instance().registerKey(MODELDATA);
    	/*int ID = 0;
    	
		packetNetwork.registerMessage(ID++, ClientModelPacket.class, ClientModelPacket::encode, ClientModelPacket::new, ClientModelPacket::handle);
		packetNetwork.registerMessage(ID++, ServerModelsPacket.class, ServerModelsPacket::encode, ServerModelsPacket::new, ServerModelsPacket::handle);*/
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        }
    }
}
