package me.pixeldots.pixelscharactermodels.model.part.model.mesh;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelCubeQuad;
import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelCubeVertex;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import me.pixeldots.pixelscharactermodels.utils.MapVecAny;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

@Environment(EnvType.CLIENT)
public class ModelPartMesh {
   public final ModelMeshQuad[] sides;

   public ModelPartMesh(MapVec3[] Vertices, MapVecAny[] Faces, MapVec2[] UVs, MapVec3[] Normals, MapVec3 Pos, MapVec3 Size, boolean mirror, MapVec2 textureSize) {
      this.sides = new ModelMeshQuad[6];
      ModelMeshVertex[] verts = new ModelMeshVertex[Vertices.length];
      for (int i = 0; i < Vertices.length; i++) {
    	  MapVec3 vert = Vertices[i];
    	  verts[i] = new ModelMeshVertex(Pos.X+(vert.X*Size.X), Pos.Y+(vert.Y*Size.Y), Pos.Z+(vert.Z*Size.Z), UVs[i].X, UVs[i].Y);
      }
      ModelMeshQuad[] quads = new ModelMeshQuad[Faces.length];
      for (int i = 0; i < Faces.length; i++) {
    	  ModelMeshVertex[] FaceVerts = new ModelMeshVertex[Faces[i].nums.length];
    	  MapVec2[] FaceUVs = new MapVec2[Faces[i].nums.length];
    	  for (int s = 0; s < Faces[i].nums.length; s++) {
			FaceVerts[i] = verts[Math.round(Faces[i].nums[i])];
			FaceUVs[i] = UVs[Math.round(Faces[i].nums[i])];
    	  }
    	  quads[i] = new ModelMeshQuad(FaceVerts, FaceUVs, textureSize.X, textureSize.Y, mirror, Normals[i]);
      }
   }
   
   public void render(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
	   Tessellator tessellator = Tessellator.getInstance();
	   BufferBuilder bufferBuilder = tessellator.getBuffer();
	   
	   RenderSystem.enableCull();
	   RenderSystem.enableDepthTest();
	   
	   Matrix4f matrix4f = entry.getModel();
       Matrix3f matrix3f = entry.getNormal();
       ModelMeshQuad[] var11 = this.sides;
       int var12 = var11.length;

       for(int var13 = 0; var13 < var12; ++var13) {
    	   ModelMeshQuad quad = var11[var13];
          Vec3f vec3f = quad.direction.copy();
          vec3f.transform(matrix3f);
          float f = vec3f.getX();
          float g = vec3f.getY();
          float h = vec3f.getZ();
          ModelMeshVertex[] var19 = quad.vertices;
          int var20 = var19.length;
          bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
          for(int var21 = 0; var21 < var20; ++var21) {
        	 ModelMeshVertex vertex = var19[var21];
             float i = vertex.pos.getX() / 16.0F;
             float j = vertex.pos.getY() / 16.0F;
             float k = vertex.pos.getZ() / 16.0F;
             Vector4f vector4f = new Vector4f(i, j, k, 1.0F);
             vector4f.transform(matrix4f);
             bufferBuilder.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, f, g, h);
             //vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, f, g, h);
          }
          tessellator.draw();
       }
       RenderSystem.disableCull();
	   RenderSystem.disableDepthTest();
   }
}