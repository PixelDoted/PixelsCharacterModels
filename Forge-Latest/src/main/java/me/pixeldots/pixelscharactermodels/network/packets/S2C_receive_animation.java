package me.pixeldots.pixelscharactermodels.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.AnimationFile;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.files.AnimationPlayer;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class S2C_receive_animation {
    
    public UUID uuid;
    public boolean set_animation;
    public String animation;
    public String name;
    
    public S2C_receive_animation() {}

    public S2C_receive_animation(FriendlyByteBuf in) {
        uuid = in.readUUID();
        set_animation = in.readBoolean();
        animation = in.readUtf();
        name = in.readUtf();
    }

    public static void handle(S2C_receive_animation msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UUID uuid = msg.uuid;

            if (msg.set_animation) {
                String s = msg.animation;
                String name = msg.name;

                Gson gson = new Gson();
                AnimationFile animation = gson.fromJson(s, AnimationFile.class);
                AnimationPlayer anim_player = new AnimationPlayer(animation, name);
                PCMClient.EntityAnimationList.put(uuid, anim_player);
            } else {
                LivingEntity entity = PlatformUtils.getLivingEntity(uuid);
                EntityModel<?> model = PlatformUtils.getModel(entity);
                AnimationHelper.stop(entity, model, true);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(uuid);
        out.writeBoolean(set_animation);
        out.writeUtf(animation);
        out.writeUtf(name);
    }

}