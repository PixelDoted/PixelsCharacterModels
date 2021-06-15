package me.pixeldots.pixelscharactermodels.model.part.model.mesh;

import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ModelMeshQuad {
	public final ModelMeshVertex[] vertices;
	public final Vec3f direction;
	
	public ModelMeshQuad(ModelMeshVertex[] vertices, MapVec2[] UVs, float squishU, float squishV, boolean flip, MapVec3 direction) {
		this.vertices = vertices;
		float f = 0.0F / squishU;
		float g = 0.0F / squishV;
		for (int i = 0; i < vertices.length; i++) {
			vertices[0] = vertices[0].remap(UVs[i].X / squishU, UVs[i].Y / squishV);
		}
		if (flip) {
			int i = vertices.length;
			
			for(int j = 0; j < i / 2; ++j) {
				ModelMeshVertex vertex = vertices[j];
				vertices[j] = vertices[i - 1 - j];
				vertices[i - 1 - j] = vertex;
			}
		}
	
		this.direction = direction.toVec();
		if (flip) {
			this.direction.multiplyComponentwise(-1.0F, 1.0F, 1.0F);
		}
	
	}
}