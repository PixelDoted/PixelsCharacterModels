package me.pixeldots.pixelscharactermodels.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import virtuoel.pehkui.api.ScaleTypes;

public class ClientNetwork {

    @Environment(EnvType.CLIENT)
    public static void register() {

    }

    @Environment(EnvType.CLIENT)
    public static void send_pehkui_scale(LivingEntity entity, float scale) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeFloat(scale);
        buf.writeUuid(entity.getUuid());
        ClientPlayNetworking.send(ServerNetwork.scale_pehkui, buf);

        ScaleTypes.BASE.getScaleData(entity).setScale(scale);
    }

}
