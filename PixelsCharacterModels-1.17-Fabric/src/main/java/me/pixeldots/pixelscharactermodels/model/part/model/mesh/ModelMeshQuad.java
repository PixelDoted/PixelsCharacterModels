package me.pixeldots.pixelscharactermodels.model.part.model.mesh;

import java.util.List;

import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ModelMeshQuad {
	public final ModelMeshVertex[] vertices;
	public final Vec3f direction;
	
	public ModelMeshQuad(List<ModelMeshVertex> vertices, float squishU, float squishV, MapVec3 direction) {
		this.vertices = new ModelMeshVertex[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			this.vertices[i] = vertices.get(i);
		}
	
		this.direction = direction.toVec();
	
	}
}