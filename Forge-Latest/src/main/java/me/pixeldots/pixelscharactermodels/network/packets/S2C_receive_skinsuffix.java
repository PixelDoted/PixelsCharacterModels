package me.pixeldots.pixelscharactermodels.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class S2C_receive_skinsuffix {
    
    public UUID uuid;
    public String suffix;

    public S2C_receive_skinsuffix() {}

    public S2C_receive_skinsuffix(FriendlyByteBuf in) {
        uuid = in.readUUID();
        suffix = in.readUtf();
    }

    public static void handle(S2C_receive_skinsuffix msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UUID uuid = msg.uuid;
            String suffix = msg.suffix;

            SkinHelper.setSkinSuffix(uuid, suffix);
            SkinHelper.reloadSkins();
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(uuid);
        out.writeUtf(suffix);
    }

}