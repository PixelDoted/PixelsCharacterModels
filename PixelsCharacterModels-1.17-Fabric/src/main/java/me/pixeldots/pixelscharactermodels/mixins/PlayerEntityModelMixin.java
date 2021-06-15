package me.pixeldots.pixelscharactermodels.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T>
{
	public PlayerEntityModelMixin(ModelPart part) {
		super(part);
	}

	/*@Inject(at = @At("TAIL"), method = "setAngles", cancellable = true)
	public void setAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
		PixelsCharacterModels.Rendering.setAnglesPre((PlayerEntityModel<?>)(Object)this, (PlayerEntity)livingEntity);
	}*/
	
}