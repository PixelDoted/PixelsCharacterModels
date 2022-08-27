package me.pixeldots.pixelscharactermodels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.pixeldots.pixelscharactermodels.PCMMain;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    
    @Inject(method = "hasLabel", at = @At("RETURN"), cancellable = true)
    public void hasLabel(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (!PCMMain.settings.show_nametags) info.setReturnValue(false);
    }

}
