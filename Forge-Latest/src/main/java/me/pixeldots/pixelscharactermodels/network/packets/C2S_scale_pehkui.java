package me.pixeldots.pixelscharactermodels.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class C2S_scale_pehkui {
    
    public UUID uuid;
    public float scale;

    public C2S_scale_pehkui() {}

    public C2S_scale_pehkui(FriendlyByteBuf in) {
        this.uuid = in.readUUID();
        this.scale = in.readFloat();
    }

    public static void handle(C2S_scale_pehkui msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            float scale = msg.scale;
            LivingEntity entity = PlatformUtils.getLivingEntity(msg.uuid);
            PCMUtils.setPehkuiScale(entity, scale);
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(this.uuid);
        out.writeFloat(this.scale);
    }

}