package me.pixeldots.pixelscharactermodels.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

	@Accessor("currentfps")
	int getCurrentFPS();

	@Accessor("pausedTickDelta")
	float getPausedTickDelta();

	@Accessor("renderTickCounter")
	RenderTickCounter getRenderTickCounter();

	@Accessor("paused")
	boolean getPaused();
	
}
