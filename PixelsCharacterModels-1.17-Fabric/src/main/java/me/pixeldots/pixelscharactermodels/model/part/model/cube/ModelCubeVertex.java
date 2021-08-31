package me.pixeldots.pixelscharactermodels.model.part.model.cube;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ModelCubeVertex {
   public final Vec3f pos;
   public final float u;
   public final float v;

   public ModelCubeVertex(float x, float y, float z, float u, float v) {
      this(new Vec3f(x, y, z), u, v);
   }

   public ModelCubeVertex remap(float u, float v) {
      return new ModelCubeVertex(this.pos, u, v);
   }

   public ModelCubeVertex(Vec3f pos, float u, float v) {
      this.pos = pos;
      this.u = u;
      this.v = v;
   }
}