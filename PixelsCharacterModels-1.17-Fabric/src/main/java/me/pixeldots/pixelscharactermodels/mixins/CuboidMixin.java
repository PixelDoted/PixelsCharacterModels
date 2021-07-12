package me.pixeldots.pixelscharactermodels.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.accessors.WorldRendererAccessor;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

@Mixin(Cuboid.class)
public class CuboidMixin {
	
	@Inject(at = @At("HEAD"), method = "renderCuboid", cancellable = true)
	public void renderCuboid(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		Cuboid ThisCube = (Cuboid)(Object)this;
		Box box = new Box(ThisCube.minX*2,ThisCube.minY*2,ThisCube.minZ*2,(ThisCube.maxX-ThisCube.minX)*2,(ThisCube.maxY-ThisCube.minY)*2,(ThisCube.maxZ-ThisCube.minZ)*2);
		Frustum frustum = ((WorldRendererAccessor)PixelsCharacterModels.client.worldRenderer).getFrustum();
	    if (!(box.isValid() || box.getAverageSideLength() == 0.0D)) if (!frustum.isVisible(box)) info.cancel();
	}
	
}
