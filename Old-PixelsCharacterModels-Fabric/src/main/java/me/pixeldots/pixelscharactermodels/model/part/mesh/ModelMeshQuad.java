package me.pixeldots.pixelscharactermodels.model.part.mesh;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModelMeshQuad {
	public final ModelMeshVertex[] vertices;
	
	public ModelMeshQuad(List<ModelMeshVertex> vertices, float squishU, float squishV) {
		this.vertices = new ModelMeshVertex[vertices.size()];

		for (int i = 0; i < vertices.size(); i++) {
			this.vertices[i] = vertices.get(i);
		}
	}
}