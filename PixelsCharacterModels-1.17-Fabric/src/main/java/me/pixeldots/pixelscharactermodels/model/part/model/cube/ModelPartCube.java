package me.pixeldots.pixelscharactermodels.model.part.model.cube;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

@Environment(EnvType.CLIENT)
public class ModelPartCube {
   public final ModelCubeQuad[] sides;
   public final float minX;
   public final float minY;
   public final float minZ;
   public final float maxX;
   public final float maxY;
   public final float maxZ;

   public ModelPartCube(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extraX, float extraY, float extraZ, boolean mirror, float textureWidth, float textureHeight) {
      this.minX = x;
      this.minY = y;
      this.minZ = z;
      this.maxX = x + sizeX;
      this.maxY = y + sizeY;
      this.maxZ = z + sizeZ;
      this.sides = new ModelCubeQuad[6];
      float f = x + sizeX;
      float g = y + sizeY;
      float h = z + sizeZ;
      x -= extraX;
      y -= extraY;
      z -= extraZ;
      f += extraX;
      g += extraY;
      h += extraZ;
      if (mirror) {
         float i = f;
         f = x;
         x = i;
      }

      ModelCubeVertex vertex = new ModelCubeVertex(x, y, z, 0.0F, 0.0F);
      ModelCubeVertex vertex2 = new ModelCubeVertex(f, y, z, 0.0F, 8.0F);
      ModelCubeVertex vertex3 = new ModelCubeVertex(f, g, z, 8.0F, 8.0F);
      ModelCubeVertex vertex4 = new ModelCubeVertex(x, g, z, 8.0F, 0.0F);
      ModelCubeVertex vertex5 = new ModelCubeVertex(x, y, h, 0.0F, 0.0F);
      ModelCubeVertex vertex6 = new ModelCubeVertex(f, y, h, 0.0F, 8.0F);
      ModelCubeVertex vertex7 = new ModelCubeVertex(f, g, h, 8.0F, 8.0F);
      ModelCubeVertex vertex8 = new ModelCubeVertex(x, g, h, 8.0F, 0.0F);
      float j = (float)u;
      float k = (float)u + sizeZ;
      float l = (float)u + sizeZ + sizeX;
      float m = (float)u + sizeZ + sizeX + sizeX;
      float n = (float)u + sizeZ + sizeX + sizeZ;
      float o = (float)u + sizeZ + sizeX + sizeZ + sizeX;
      float p = (float)v;
      float q = (float)v + sizeZ;
      float r = (float)v + sizeZ + sizeY;
      this.sides[2] = new ModelCubeQuad(new ModelCubeVertex[]{vertex6, vertex5, vertex, vertex2}, k, p, l, q, textureWidth, textureHeight, mirror, Direction.DOWN);
      this.sides[3] = new ModelCubeQuad(new ModelCubeVertex[]{vertex3, vertex4, vertex8, vertex7}, l, q, m, p, textureWidth, textureHeight, mirror, Direction.UP);
      this.sides[1] = new ModelCubeQuad(new ModelCubeVertex[]{vertex, vertex5, vertex8, vertex4}, j, q, k, r, textureWidth, textureHeight, mirror, Direction.WEST);
      this.sides[4] = new ModelCubeQuad(new ModelCubeVertex[]{vertex2, vertex, vertex4, vertex3}, k, q, l, r, textureWidth, textureHeight, mirror, Direction.NORTH);
      this.sides[0] = new ModelCubeQuad(new ModelCubeVertex[]{vertex6, vertex2, vertex3, vertex7}, l, q, n, r, textureWidth, textureHeight, mirror, Direction.EAST);
      this.sides[5] = new ModelCubeQuad(new ModelCubeVertex[]{vertex5, vertex6, vertex7, vertex8}, n, q, o, r, textureWidth, textureHeight, mirror, Direction.SOUTH);
   }
   
   public void render(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
       Matrix4f matrix4f = entry.getModel();
       Matrix3f matrix3f = entry.getNormal();
       ModelCubeQuad[] var11 = this.sides;
       int var12 = var11.length;

       for(int var13 = 0; var13 < var12; ++var13) {
          ModelCubeQuad quad = var11[var13];
          Vec3f vec3f = quad.direction.copy();
          vec3f.transform(matrix3f);
          float f = vec3f.getX();
          float g = vec3f.getY();
          float h = vec3f.getZ();
          ModelCubeVertex[] var19 = quad.vertices;
          int var20 = var19.length;

          for(int var21 = 0; var21 < var20; ++var21) {
             ModelCubeVertex vertex = var19[var21];
             float i = vertex.pos.getX() / 16.0F;
             float j = vertex.pos.getY() / 16.0F;
             float k = vertex.pos.getZ() / 16.0F;
             Vector4f vector4f = new Vector4f(i, j, k, 1.0F);
             vector4f.transform(matrix4f);
             vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, f, g, h);
          }
       }

    }
}