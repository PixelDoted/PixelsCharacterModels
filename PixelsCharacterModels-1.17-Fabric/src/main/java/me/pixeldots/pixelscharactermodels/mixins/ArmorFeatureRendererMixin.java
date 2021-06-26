package me.pixeldots.pixelscharactermodels.mixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorItem;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
	
	@Inject(at = @At("HEAD"), method = { "renderArmorParts" }, cancellable = true)
	public void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, BipedEntityModel<?> model, boolean legs, float red, float green, float blue, @Nullable String overlay, CallbackInfo info) {
		if (!PixelsCharacterModels.localData.showArmor) info.cancel();
	}
	
}
