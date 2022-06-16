package me.pixeldots.pixelscharactermodels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public interface IMinecraftClientMixin {
    
    @Accessor("fpsCounter")
    int getFPS();

}
