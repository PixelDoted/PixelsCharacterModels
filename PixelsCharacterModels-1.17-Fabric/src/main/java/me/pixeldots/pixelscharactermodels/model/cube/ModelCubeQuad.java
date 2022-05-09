package me.pixeldots.pixelscharactermodels.model.cube;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ModelCubeQuad {
   public final ModelCubeVertex[] vertices;
   public final Vec3f direction;

   public ModelCubeQuad(ModelCubeVertex[] vertices, float u1, float v1, float u2, float v2, float squishU, float squishV, boolean flip, Direction direction) {
      this.vertices = vertices;
      float f = 0.0F / squishU;
      float g = 0.0F / squishV;
      vertices[0] = vertices[0].remap(u2 / squishU - f, v1 / squishV + g);
      vertices[1] = vertices[1].remap(u1 / squishU + f, v1 / squishV + g);
      vertices[2] = vertices[2].remap(u1 / squishU + f, v2 / squishV - g);
      vertices[3] = vertices[3].remap(u2 / squishU - f, v2 / squishV - g);
      if (flip) {
         int i = vertices.length;

         for(int j = 0; j < i / 2; ++j) {
        	 ModelCubeVertex vertex = vertices[j];
            vertices[j] = vertices[i - 1 - j];
            vertices[i - 1 - j] = vertex;
         }
      }

      this.direction = direction.getUnitVector();
      if (flip) {
         this.direction.multiplyComponentwise(-1.0F, 1.0F, 1.0F);
      }

   }
}