package me.pixeldots.pixelscharactermodels.network;

import java.util.UUID;

import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.AnimationFile;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.files.AnimationPlayer;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ClientNetwork {

    public static Identifier receive_skinsuffix = new Identifier("receive_skinsuffix");
    public static Identifier receive_animation = new Identifier("receive_animation");

    public static Identifier request_skinsuffixs = new Identifier("request_skinsuffixs");

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(receive_skinsuffix, (client, handler, buf, sender) -> {
            UUID uuid = buf.readUuid();
            String suffix = buf.readString();

            SkinHelper.setSkinSuffix(uuid, suffix);
            SkinHelper.reloadSkins();
        });

        ClientPlayNetworking.registerGlobalReceiver(receive_animation, (client, handler, buf, sender) -> {
            UUID uuid = buf.readUuid();

            if (buf.readBoolean()) {
                String s = buf.readString();
                String name = buf.readString();

                Gson gson = new Gson();
                AnimationFile animation = gson.fromJson(s, AnimationFile.class);
                AnimationPlayer player = new AnimationPlayer(animation, name);
                PCMClient.EntityAnimationList.put(uuid, player);
            } else {
                LivingEntity entity = PlatformUtils.getLivingEntity(uuid);
                EntityModel<?> model = PlatformUtils.getModel(entity);
                AnimationHelper.stop(entity, model, true);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(request_skinsuffixs, (client, handler, buf, sender) -> {
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                UUID uuid = buf.readUuid();
                String suffix = buf.readString();

                PCMClient.PlayerSkinList.clear();
                SkinHelper.setSkinSuffix(uuid, suffix);
                SkinHelper.reloadSkins();
            }
        });
    }

    // sends and updates an entity's pehkui scale
    @Environment(EnvType.CLIENT)
    public static void send_pehkui_scale(LivingEntity entity, float scale) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeFloat(scale);
        buf.writeUuid(entity.getUuid());
        ClientPlayNetworking.send(ServerNetwork.scale_pehkui, buf);
    }

    // sends animation data to the server
    @Environment(EnvType.CLIENT)
    public static void send_animation(LivingEntity entity, AnimationFile animation, String name) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(entity.getUuid());
        buf.writeBoolean(animation != null);

        if (animation != null) {
            Gson gson = new Gson();
            String s = gson.toJson(animation);
            buf.writeString(s);
            buf.writeString(name);
        }
        ClientPlayNetworking.send(ServerNetwork.animation, buf);
    }

    // sends skin suffix data to the server
    @Environment(EnvType.CLIENT)
    public static void send_skin_suffix(UUID uuid, String suffix) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeString(suffix);
        ClientPlayNetworking.send(ServerNetwork.skin_suffix, buf);
    }

    @Environment(EnvType.CLIENT)
    public static void request_skinsuffixs() {
        ClientPlayNetworking.send(request_skinsuffixs, PacketByteBufs.empty());
    }

}
