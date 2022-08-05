package me.pixeldots.pixelscharactermodels.network;

import java.util.UUID;

import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.AnimationFile;
import me.pixeldots.pixelscharactermodels.network.packets.C2S_animation;
import me.pixeldots.pixelscharactermodels.network.packets.C2S_request_skinsuffixs;
import me.pixeldots.pixelscharactermodels.network.packets.C2S_scale_pehkui;
import me.pixeldots.pixelscharactermodels.network.packets.C2S_skin_suffix;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ClientNetwork {

    public static SimpleChannel scale_pehkui = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("pcm", "c2s_scale_pehkui"),
        () -> PCMMain.ProVer, PCMMain.ProVer::equals, PCMMain.ProVer::equals);
    public static SimpleChannel skin_suffix = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("pcm", "c2s_skin_suffix"),
        () -> PCMMain.ProVer, PCMMain.ProVer::equals, PCMMain.ProVer::equals);
    public static SimpleChannel animation = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("pcm", "c2s_animation"),
        () -> PCMMain.ProVer, PCMMain.ProVer::equals, PCMMain.ProVer::equals);
    public static SimpleChannel request_skinsuffixs = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("pcm", "c2s_request_skinsuffixs"),
        () -> PCMMain.ProVer, PCMMain.ProVer::equals, PCMMain.ProVer::equals);

    @OnlyIn(Dist.CLIENT)
    public static void register(int id) {
        scale_pehkui.registerMessage(id++, C2S_scale_pehkui.class, C2S_scale_pehkui::encode, C2S_scale_pehkui::new, C2S_scale_pehkui::handle);
        skin_suffix.registerMessage(id++, C2S_skin_suffix.class, C2S_skin_suffix::encode, C2S_skin_suffix::new, C2S_skin_suffix::handle);
        animation.registerMessage(id++, C2S_animation.class, C2S_animation::encode, C2S_animation::new, C2S_animation::handle);
        request_skinsuffixs.registerMessage(id++, C2S_request_skinsuffixs.class, C2S_request_skinsuffixs::encode, C2S_request_skinsuffixs::new, C2S_request_skinsuffixs::handle);
    }

    // sends and updates an entity's pehkui scale
    @OnlyIn(Dist.CLIENT)
    public static void send_pehkui_scale(LivingEntity entity, float scale) {
        C2S_scale_pehkui packet = new C2S_scale_pehkui();
        packet.uuid = entity.getUUID();
        packet.scale = scale;
        scale_pehkui.sendToServer(packet);
    }

    // sends animation data to the server
    @OnlyIn(Dist.CLIENT)
    public static void send_animation(LivingEntity entity, AnimationFile animation_file, String name) {
        C2S_animation packet = new C2S_animation();
        packet.uuid = entity.getUUID();
        packet.set_animation = animation_file != null;

        if (animation_file != null) {
            Gson gson = new Gson();
            String s = gson.toJson(animation_file);
            packet.animation = s;
            packet.name = name;
        }
        animation.sendToServer(packet);
    }

    // sends skin suffix data to the server
    @OnlyIn(Dist.CLIENT)
    public static void send_skin_suffix(UUID uuid, String suffix) {
        C2S_skin_suffix packet = new C2S_skin_suffix();
        packet.uuid = uuid;
        packet.suffix = suffix;
        skin_suffix.sendToServer(packet);
    }

    @OnlyIn(Dist.CLIENT)
    public static void request_skinsuffixs() {
        request_skinsuffixs.sendToServer(new C2S_request_skinsuffixs());
    }

}
