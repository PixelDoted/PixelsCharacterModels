package me.pixeldots.pixelscharactermodels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PCMMain;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(MatrixStack matrix, VertexConsumerProvider vertex, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        if (!PCMMain.settings.show_armor) info.cancel();
    }

}
