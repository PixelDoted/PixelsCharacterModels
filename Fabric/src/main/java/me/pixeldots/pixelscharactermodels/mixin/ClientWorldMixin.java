package me.pixeldots.pixelscharactermodels.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    
    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        if (PCMClient.WorldTickCount != -1) {
            if (PCMClient.WorldTickCount == 100) {
                ClientNetwork.request_entitys();
                PCMClient.WorldTickCount = -1;
            } else PCMClient.WorldTickCount++;
        }
    }

}
