package me.pixeldots.pixelscharactermodels.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(ModelPart.class)
public class ModelPartMixin {
	
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", cancellable = true)
	public void renderMainHEAD(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		PixelsCharacterModels.Rendering.renderPartHead(matrices, vertices, (ModelPart)(Object)this, light, overlay, info);
    }
	
	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", cancellable = true)
	public void renderMainTAIL(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		PixelsCharacterModels.Rendering.renderPartTail(matrices, vertices, (ModelPart)(Object)this, light, overlay, info);
    }
	
	@Inject(at = @At("HEAD"), method = "renderCuboids(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", cancellable = true)
	public void renderCuboidsHead(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		PixelsCharacterModels.Rendering.renderPartCubioudsHead(matrices, (ModelPart)(Object)this, vertexConsumer, light, overlay, info);
	}
	
	@Inject(at = @At("TAIL"), method = "renderCuboids(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", cancellable = true)
	public void renderCuboidsTail(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		PixelsCharacterModels.Rendering.renderPartCubioudsTail(matrices, (ModelPart)(Object)this, vertexConsumer, light, overlay, info);
	}
	
}
