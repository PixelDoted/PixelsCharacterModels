package me.pixeldots.pixelscharactermodels.network;

import java.util.UUID;

import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.AnimationFile;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.files.AnimationPlayer;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.Receiver;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientNetwork {

    public static ResourceLocation receive_skinsuffix = new ResourceLocation("pcm", "receive_skinsuffix");
    public static ResourceLocation receive_animation = new ResourceLocation("pcm", "receive_animation");

    public static ResourceLocation request_skinsuffixs = new ResourceLocation("pcm", "request_skinsuffixs");

    @OnlyIn(Dist.CLIENT)
    public static void register() {
        Receiver.registerGlobalReceiver_Client(receive_skinsuffix, (server, player, buf) -> {
            UUID uuid = buf.readUUID();
            String suffix = buf.readString();

            SkinHelper.setSkinSuffix(uuid, suffix);
            SkinHelper.reloadSkins();
        });

        Receiver.registerGlobalReceiver_Client(receive_animation, (server, player, buf) -> {
            UUID uuid = buf.readUUID();

            if (buf.readBoolean()) {
                String s = buf.readString();
                String name = buf.readString();

                Gson gson = new Gson();
                AnimationFile animation = gson.fromJson(s, AnimationFile.class);
                AnimationPlayer anim_player = new AnimationPlayer(animation, name);
                PCMClient.EntityAnimationList.put(uuid, anim_player);
            } else {
                LivingEntity entity = PlatformUtils.getLivingEntity(uuid);
                EntityModel<?> model = PlatformUtils.getModel(entity);
                AnimationHelper.stop(entity, model, true);
            }
        });

        Receiver.registerGlobalReceiver_Client(request_skinsuffixs, (server, player, buf) -> {
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                UUID uuid = buf.readUUID();
                String suffix = buf.readString();

                PCMClient.PlayerSkinList.clear();
                SkinHelper.setSkinSuffix(uuid, suffix);
                SkinHelper.reloadSkins();
            }
        });
    }

    // sends and updates an entity's pehkui scale
    @OnlyIn(Dist.CLIENT)
    public static void send_pehkui_scale(LivingEntity entity, float scale) {
        Receiver data = new Receiver(ServerNetwork.scale_pehkui);
        data.buf.writeString(String.valueOf(scale));
        data.buf.writeUUID(entity.getUUID());
        Receiver.send_fromClient(data);
        PCMUtils.setPehkuiScale(entity, scale);
    }

    // sends animation data to the server
    @OnlyIn(Dist.CLIENT)
    public static void send_animation(LivingEntity entity, AnimationFile animation, String name) {
        Receiver data = new Receiver(ServerNetwork.animation);
        data.buf.writeUUID(entity.getUUID());
        data.buf.writeBoolean(animation != null);

        if (animation != null) {
            Gson gson = new Gson();
            String s = gson.toJson(animation);
            data.buf.writeString(s);
            data.buf.writeString(name);
        }
        Receiver.send_fromClient(data);
    }

    // sends skin suffix data to the server
    @OnlyIn(Dist.CLIENT)
    public static void send_skin_suffix(UUID uuid, String suffix) {
        Receiver data = new Receiver(ServerNetwork.skin_suffix);
        data.buf.writeUUID(uuid);
        data.buf.writeString(suffix);
        Receiver.send_fromClient(data);
        SkinHelper.setSkinSuffix(uuid, suffix);
    }

    @OnlyIn(Dist.CLIENT)
    public static void request_skinsuffixs() {
        Receiver.send_fromClient(new Receiver(request_skinsuffixs));
    }

}
