package me.PixelDots.PixelsCharacterModels.client.model;

import java.util.UUID;
import java.util.function.Supplier;

import me.PixelDots.PixelsCharacterModels.Model.ModelManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientModelPacket {
	
    private String uuid;
    private String data;

    public ClientModelPacket() {
    }

    public ClientModelPacket(String uuid, String data) {
        this.uuid = uuid;
        this.data = data;
        //this.isTransparent = isTransparent;
    }

    public ClientModelPacket(PacketBuffer buf) {
        this.uuid = buf.readString(Short.MAX_VALUE);
        this.data = buf.readString(Short.MAX_VALUE);
        ModelManager.Model.setModel(UUID.fromString(uuid), data);
    }

    public void encode(PacketBuffer buf) {
        buf.writeString(this.uuid);
        buf.writeString(this.data);
    }
    
    public static void handle(ClientModelPacket packet, Supplier<NetworkEvent.Context> ctx) {
    	ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            @SuppressWarnings("unused")
			PlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
            ModelManager.Model.setModel(UUID.fromString(packet.uuid), packet.data);
            // do stuff
        });
        ctx.get().setPacketHandled(true);
    }
    
}