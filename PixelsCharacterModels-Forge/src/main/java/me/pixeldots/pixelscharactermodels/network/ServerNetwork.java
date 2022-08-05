package me.pixeldots.pixelscharactermodels.network;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.network.packets.S2C_receive_animation;
import me.pixeldots.pixelscharactermodels.network.packets.S2C_receive_skinsuffix;
import me.pixeldots.pixelscharactermodels.network.packets.S2C_request_skinsuffixs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ServerNetwork {
    
    public static SimpleChannel receive_skinsuffix = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("pcm", "s2c_receive_skinsuffix"),
        () -> PCMMain.ProVer, PCMMain.ProVer::equals, PCMMain.ProVer::equals);
    public static SimpleChannel receive_animation = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("pcm", "s2c_receive_animation"),
        () -> PCMMain.ProVer, PCMMain.ProVer::equals, PCMMain.ProVer::equals);
    public static SimpleChannel request_skinsuffixs = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("pcm", "s2c_request_skinsuffixs"),
        () -> PCMMain.ProVer, PCMMain.ProVer::equals, PCMMain.ProVer::equals);

    public static void register(int id) {
        receive_skinsuffix.registerMessage(id++, S2C_receive_skinsuffix.class, S2C_receive_skinsuffix::encode, S2C_receive_skinsuffix::new, S2C_receive_skinsuffix::handle);
        receive_animation.registerMessage(id++, S2C_receive_animation.class, S2C_receive_animation::encode, S2C_receive_animation::new, S2C_receive_animation::handle);
        request_skinsuffixs.registerMessage(id++, S2C_request_skinsuffixs.class, S2C_request_skinsuffixs::encode, S2C_request_skinsuffixs::new, S2C_request_skinsuffixs::handle);
    }

}
