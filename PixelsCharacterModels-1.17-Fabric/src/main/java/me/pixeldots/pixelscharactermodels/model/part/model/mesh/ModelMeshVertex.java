package me.pixeldots.pixelscharactermodels.model.part.model.mesh;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ModelMeshVertex {
	public final Vec3f pos;
	public final float u;
	public final float v;
	
	public ModelMeshVertex(float x, float y, float z, float u, float v) {
		this(new Vec3f(x, y, z), u, v);
	}
	
	public ModelMeshVertex remap(float u, float v) {
		return new ModelMeshVertex(this.pos, u, v);
	}
	
	public ModelMeshVertex(Vec3f pos, float u, float v) {
		this.pos = pos;
		this.u = u;
		this.v = v;
	}
}