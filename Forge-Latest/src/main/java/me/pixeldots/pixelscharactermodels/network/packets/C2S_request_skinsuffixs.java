package me.pixeldots.pixelscharactermodels.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.network.ServerNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2S_request_skinsuffixs {

    public C2S_request_skinsuffixs() {}

    public C2S_request_skinsuffixs(FriendlyByteBuf in) {}

    public static void handle(C2S_request_skinsuffixs msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            S2C_request_skinsuffixs packet = new S2C_request_skinsuffixs();
            packet.count = PCMMain.skinsuffix_data.size();
            packet.uuids = new UUID[packet.count];
            packet.suffixs = new String[packet.count];

            int index = 0;
            for (UUID uuid : PCMMain.skinsuffix_data.keySet()) {
                packet.uuids[index] = uuid;
                packet.suffixs[index] = PCMMain.skinsuffix_data.get(uuid);
                index += 1;
            }

            ServerNetwork.request_skinsuffixs.sendTo(packet, ctx.get().getSender().connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
    }

}