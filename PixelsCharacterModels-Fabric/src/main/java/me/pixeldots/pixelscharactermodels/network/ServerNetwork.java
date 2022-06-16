package me.pixeldots.pixelscharactermodels.network;

import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleTypes;

public class ServerNetwork {
    
    public static Identifier scale_pehkui = new Identifier("scale_pehkui");
    public static Identifier skin_suffix = new Identifier("skin_suffix");
    public static Identifier animation = new Identifier("animation");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(scale_pehkui, (server, player, handler, buf, sender) -> {
            float scale = buf.readFloat();
            LivingEntity entity = PlatformUtils.getLivingEntity(buf.readUuid());
            ScaleTypes.BASE.getScaleData(entity).setScale(scale);
        });

        ServerPlayNetworking.registerGlobalReceiver(skin_suffix, (server, player, handler, buf, sender) -> {
            PCMMain.skinsuffix_data.put(buf.readUuid(), buf.readString());

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
    }

}
