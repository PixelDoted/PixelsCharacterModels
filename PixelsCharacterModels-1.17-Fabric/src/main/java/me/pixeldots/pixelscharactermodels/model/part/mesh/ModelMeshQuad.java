package me.pixeldots.pixelscharactermodels.model.part.mesh;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ModelMeshQuad {
	public final ModelMeshVertex[] vertices;
	public final float identityPoint;
	
	public ModelMeshQuad(List<ModelMeshVertex> vertices, float squishU, float squishV) {
		this.vertices = new ModelMeshVertex[vertices.size()];
		float point = 0;

		for (int i = 0; i < vertices.size(); i++) {
			this.vertices[i] = vertices.get(i);
			point += vertices.get(i).pos.getX()*vertices.get(i).pos.getY()*vertices.get(i).pos.getZ();
		}
		identityPoint = point;
	}
}