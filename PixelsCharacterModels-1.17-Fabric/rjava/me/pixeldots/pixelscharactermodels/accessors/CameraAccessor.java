package me.pixeldots.pixelscharactermodels.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

@Mixin(Camera.class)
public interface CameraAccessor {

	@Accessor("cameraY")
	float getCameraY();
	
	@Accessor("cameraY")
	void setCameraY(float value);
	
	@Accessor("pos")
	Vec3d getPos();
	
}
