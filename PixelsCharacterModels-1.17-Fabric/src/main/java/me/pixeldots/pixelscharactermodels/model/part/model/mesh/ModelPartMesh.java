package me.pixeldots.pixelscharactermodels.model.part.model.mesh;

import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import me.pixeldots.pixelscharactermodels.utils.MapVecAny;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;

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
}