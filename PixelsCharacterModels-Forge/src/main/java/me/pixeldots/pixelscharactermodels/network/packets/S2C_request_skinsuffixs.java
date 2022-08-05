package me.pixeldots.pixelscharactermodels.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class S2C_request_skinsuffixs {
    
    public int count;
    public UUID[] uuids;
    public String[] suffixs;

    public S2C_request_skinsuffixs() {}

    public S2C_request_skinsuffixs(FriendlyByteBuf in) {
        count = in.readInt();
        uuids = new UUID[count];
        suffixs = new String[count];
        for (int i = 0; i < count; i++) {
            uuids[i] = in.readUUID();
            suffixs[i] = in.readUtf();
        }
    }

    public static void handle(S2C_request_skinsuffixs msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            int count = msg.count;
            for (int i = 0; i < count; i++) {
                UUID uuid = msg.uuids[i];
                String suffix = msg.suffixs[i];

                PCMClient.PlayerSkinList.clear();
                SkinHelper.setSkinSuffix(uuid, suffix);
                SkinHelper.reloadSkins();
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            out.writeUUID(uuids[i]);
            out.writeUtf(suffixs[i]);
        }
    }

}