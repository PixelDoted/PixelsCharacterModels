package me.pixeldots.pixelscharactermodels.network;

import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerNetwork {
    
    public static Identifier scale_pehkui = new Identifier("scale_pehkui");
    public static Identifier skin_suffix = new Identifier("skin_suffix");
    public static Identifier animation = new Identifier("animation");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(scale_pehkui, (server, player, handler, buf, sender) -> {
            float scale = buf.readFloat();

            Entity entity = player.getWorld().getEntity(buf.readUuid());
            if (entity instanceof LivingEntity) {
                PCMUtils.setPehkuiScale((LivingEntity)entity, scale);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(skin_suffix, (server, player, handler, buf, sender) -> {
            UUID uuid = buf.readUuid();
            String suffix = buf.readString();
            if (suffix.equals("")) PCMMain.skinsuffix_data.remove(uuid);
            else PCMMain.skinsuffix_data.put(uuid, suffix);

            for (ServerPlayerEntity receiver : server.getPlayerManager().getPlayerList()) {
                if (receiver == player) continue;
                ServerPlayNetworking.send(receiver, ClientNetwork.receive_skinsuffix, buf);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(animation, (server, player, handler, buf, sender) -> {
            UUID uuid = buf.readUuid();
            if (buf.readBoolean()) {
                String s = buf.readString();
                String name = buf.readString();
                PCMMain.animation_data.put(uuid, name + ";" + s);
            } else
                PCMMain.animation_data.remove(uuid);

            for (ServerPlayerEntity receiver : server.getPlayerManager().getPlayerList()) {
                if (receiver == player) continue;
                ServerPlayNetworking.send(receiver, ClientNetwork.receive_animation, buf);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(ClientNetwork.request_skinsuffixs, (server, player, handler, buf, sender) -> {
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeInt(PCMMain.skinsuffix_data.size());
            for (UUID uuid : PCMMain.skinsuffix_data.keySet()) {
                buffer.writeUuid(uuid);
                buffer.writeString(PCMMain.skinsuffix_data.get(uuid));
            }

            ServerPlayNetworking.send(player, ClientNetwork.request_skinsuffixs, buffer);
        });
    }

}
