package me.pixeldots.pixelscharactermodels.model.mesh;

import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ModelMeshVertex {
	public Vec3f normal;
	public Vec3f pos;
	public float u;
	public float v;
	
	public ModelMeshVertex(String x, String y, String z, String nx, String ny, String nz, String u, String v) {
		this(new Vec3f(Float.parseFloat(x), Float.parseFloat(z), Float.parseFloat(y)),
				new Vec3f(Float.parseFloat(nx), Float.parseFloat(nz), Float.parseFloat(ny))
				, Float.parseFloat(u), Float.parseFloat(v));
	}
	
	public ModelMeshVertex(String x, String y, String z, String nx, String ny, String nz, float u, float v) {
		this(new Vec3f(Float.parseFloat(x), Float.parseFloat(z), Float.parseFloat(y)),
				new Vec3f(Float.parseFloat(nx), Float.parseFloat(nz), Float.parseFloat(ny))
				, u, v);
	}
	
	public ModelMeshVertex(float x, float y, float z, float nx, float ny, float nz, String u, String v) {
		this(new Vec3f(x, y, z), new Vec3f(nx, ny, nz), Float.parseFloat(u), Float.parseFloat(v));
	}
	
	public ModelMeshVertex remap(float u, float v) {
		return new ModelMeshVertex(this.pos, this.normal, u, v);
	}
	
	public ModelMeshVertex(Vec3f pos, Vec3f normal, float u, float v) {
		this.pos = pos;
		this.u = u;
		this.v = v;
		this.normal = normal;
	}
	
	public ModelMeshVertex setPosSize(MapVec3 Size, MapVec3 Pos) {
		Vec3f p = new Vec3f(pos.getX()*Size.X+Pos.X,pos.getY()*Size.Y+Pos.Y,pos.getZ()*Size.Z+Pos.Z);
		this.pos = p;
		return this;
	}
	
	public ModelMeshVertex addUVOffset(float u, float v) {
		this.u += u;
		this.v += v;
		return this;
	}
}