package me.pixeldots.pixelscharactermodels.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.network.ServerNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2S_skin_suffix {
    
    public UUID uuid;
    public String suffix;
    
    public C2S_skin_suffix() {}

    public C2S_skin_suffix(FriendlyByteBuf in) {
        uuid = in.readUUID();
        suffix = in.readUtf();
    }

    public static void handle(C2S_skin_suffix msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            UUID uuid = msg.uuid;
            String suffix = msg.suffix;
            if (suffix.equals("")) PCMMain.skinsuffix_data.remove(uuid);
            else PCMMain.skinsuffix_data.put(uuid, suffix);

            S2C_receive_skinsuffix buffer = new S2C_receive_skinsuffix();
            buffer.uuid = uuid;
            buffer.suffix = suffix;
            for (Player plr : sender.level.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;

                if (receiver == sender) continue;
                ServerNetwork.receive_skinsuffix.sendTo(buffer, receiver.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(this.uuid);
        out.writeUtf(this.suffix);
    }

}