package me.pixeldots.pixelscharactermodels.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.accessors.CameraAccessor;
import net.minecraft.client.render.Camera;

@Mixin(Camera.class)
public class CameraMixin {

	@Inject(at = @At("TAIL"), method = "updateEyeHeight")
	public void updateEyeHeight(CallbackInfo info) {
		Camera camera = (Camera)(Object)this;
		
		float CameraHeight = 0;
		if (PixelsCharacterModels.playingAnimationData != null)
			CameraHeight = PixelsCharacterModels.playingAnimationData.playerTransform.Y/2;
		
		CameraAccessor accessor = ((CameraAccessor)camera);
		accessor.setCameraY(accessor.getCameraY()+CameraHeight);
	}
	
}
