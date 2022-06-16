package me.pixeldots.pixelscharactermodels.network;

import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleTypes;

public class ServerNetwork {
    
    public static Identifier scale_pehkui = new Identifier("scale_pehkui");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(scale_pehkui, (server, player, handler, buf, sender) -> {
            float scale = buf.readFloat();
            LivingEntity entity = PlatformUtils.getLivingEntity(buf.readUuid());
            ScaleTypes.BASE.getScaleData(entity).setScale(scale);
        });
    }

}
