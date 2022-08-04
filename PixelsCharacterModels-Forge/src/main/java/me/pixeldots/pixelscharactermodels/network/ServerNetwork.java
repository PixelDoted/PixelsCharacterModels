package me.pixeldots.pixelscharactermodels.network;

import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.Receiver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ServerNetwork {
    
    public static ResourceLocation scale_pehkui = new ResourceLocation("pcm", "scale_pehkui");
    public static ResourceLocation skin_suffix = new ResourceLocation("pcm", "skin_suffix");
    public static ResourceLocation animation = new ResourceLocation("pcm", "animation");

    public static void register() {
        Receiver.registerGlobalReceiver_Server(scale_pehkui, (server, player, buf) -> {
            float scale = Float.parseFloat(buf.readString());
            LivingEntity entity = PlatformUtils.getLivingEntity(buf.readUUID());
            PCMUtils.setPehkuiScale(entity, scale);
        });

        Receiver.registerGlobalReceiver_Server(skin_suffix, (server, player, buf) -> {
            UUID uuid = buf.readUUID();
            String suffix = buf.readString();
            if (suffix.equals("")) PCMMain.skinsuffix_data.remove(uuid);
            else PCMMain.skinsuffix_data.put(uuid, suffix);

            Receiver buffer = new Receiver(ClientNetwork.receive_skinsuffix);
            buffer.buf = buf;
            for (Player plr : server.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;

                if (receiver == player) continue;
                Receiver.send_fromServer(receiver, buffer);
            }
        });

        Receiver.registerGlobalReceiver_Server(animation, (server, player, buf) -> {
            UUID uuid = buf.readUUID();
            if (buf.readBoolean()) {
                String s = buf.readString();
                String name = buf.readString();
                PCMMain.animation_data.put(uuid, name + ";" + s);
            } else
                PCMMain.animation_data.remove(uuid);

            Receiver buffer = new Receiver(ClientNetwork.receive_animation);
            buffer.buf = buf;
            for (Player plr : server.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;

                if (receiver == player) continue;
                Receiver.send_fromServer(receiver, buffer);
            }
        });

        Receiver.registerGlobalReceiver_Server(ClientNetwork.request_skinsuffixs, (server, player, buf) -> {
            Receiver buffer = new Receiver(ClientNetwork.request_skinsuffixs);
            buffer.buf.writeInt(PCMMain.skinsuffix_data.size());
            for (UUID uuid : PCMMain.skinsuffix_data.keySet()) {
                buffer.buf.writeUUID(uuid);
                buffer.buf.writeString(PCMMain.skinsuffix_data.get(uuid));
            }

            Receiver.send_fromServer(player, buffer);
        });
    }

}
