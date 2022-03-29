package me.pixeldots.pixelscharactermodels.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
	
	@Inject(at = @At("HEAD"), method = "render")
	public void renderHead(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if (livingEntity instanceof PlayerEntity) {
			PixelsCharacterModels.Rendering.playerRenderHead((PlayerEntityModel<?>)((LivingEntityRenderer<?,?>)(Object)this).getModel(), (PlayerEntity)livingEntity, (LivingEntityRenderer<?,?>)(Object)this);
			PixelsCharacterModels.Rendering.currentPlayerRendering = ((PlayerEntity)livingEntity).getGameProfile();
		}
	}
	
	@Inject(at = @At("TAIL"), method = "render")
	public void renderTail(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if (livingEntity instanceof PlayerEntity) {
			PixelsCharacterModels.Rendering.playerRenderTail((PlayerEntityModel<?>)((LivingEntityRenderer<?,?>)(Object)this).getModel(), (PlayerEntity)livingEntity, (LivingEntityRenderer<?,?>)(Object)this);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "hasLabel", cancellable = true)
	public void hasLabel(LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
		if (!PixelsCharacterModels.localData.showNameTags) info.setReturnValue(false);
	}
	
}
