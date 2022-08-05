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

public class C2S_animation {
    
    public UUID uuid;
    public boolean set_animation;
    public String animation;
    public String name;

    public C2S_animation() {}

    public C2S_animation(FriendlyByteBuf in) {
        uuid = in.readUUID();
        set_animation = in.readBoolean();
        if (set_animation) {
            animation = in.readUtf();
            name = in.readUtf();
        }
    }

    public static void handle(C2S_animation msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            UUID uuid = msg.uuid;
            if (msg.set_animation) {
                String s = msg.animation;
                String name = msg.name;
                PCMMain.animation_data.put(uuid, name + ";" + s);
            } else
                PCMMain.animation_data.remove(uuid);

            S2C_receive_animation buffer = new S2C_receive_animation();
            buffer.uuid = msg.uuid;
            buffer.set_animation = msg.set_animation;
            buffer.animation = msg.animation;
            buffer.name = msg.name;
            for (Player plr : sender.level.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;

                if (receiver == sender) continue;
                ServerNetwork.receive_animation.sendTo(buffer, receiver.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(this.uuid);
        out.writeBoolean(this.set_animation);
        if (this.set_animation) {
            out.writeUtf(this.animation);
            out.writeUtf(this.name);
        }
    }

}