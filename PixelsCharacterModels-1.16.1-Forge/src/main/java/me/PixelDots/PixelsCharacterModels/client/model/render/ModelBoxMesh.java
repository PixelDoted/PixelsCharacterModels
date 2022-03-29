package me.PixelDots.PixelsCharacterModels.client.model.render;

import me.PixelDots.PixelsCharacterModels.client.model.render.mesh.PositionTextureVertexMesh;
import me.PixelDots.PixelsCharacterModels.client.model.render.mesh.TexturedQuadMesh;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelBoxMesh {
   public final TexturedQuadMesh[] quads;

   public ModelBoxMesh(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
      this.quads = new TexturedQuadMesh[6];
      float f = x + width;
      float f1 = y + height;
      float f2 = z + depth;
      x = x - deltaX;
      y = y - deltaY;
      z = z - deltaZ;
      f = f + deltaX;
      f1 = f1 + deltaY;
      f2 = f2 + deltaZ;
      if (mirorIn) {
         float f3 = f;
         f = x;
         x = f3;
      }

      PositionTextureVertexMesh modelrenderer$positiontexturevertex7 = new PositionTextureVertexMesh(x, y, z, 0.0F, 0.0F);
      PositionTextureVertexMesh modelrenderer$positiontexturevertex = new PositionTextureVertexMesh(f, y, z, 0.0F, 8.0F);
      PositionTextureVertexMesh modelrenderer$positiontexturevertex1 = new PositionTextureVertexMesh(f, f1, z, 8.0F, 8.0F);
      PositionTextureVertexMesh modelrenderer$positiontexturevertex2 = new PositionTextureVertexMesh(x, f1, z, 8.0F, 0.0F);
      PositionTextureVertexMesh modelrenderer$positiontexturevertex3 = new PositionTextureVertexMesh(x, y, f2, 0.0F, 0.0F);
      PositionTextureVertexMesh modelrenderer$positiontexturevertex4 = new PositionTextureVertexMesh(f, y, f2, 0.0F, 8.0F);
      PositionTextureVertexMesh modelrenderer$positiontexturevertex5 = new PositionTextureVertexMesh(f, f1, f2, 8.0F, 8.0F);
      PositionTextureVertexMesh modelrenderer$positiontexturevertex6 = new PositionTextureVertexMesh(x, f1, f2, 8.0F, 0.0F);
      float f4 = (float)texOffX;
      float f5 = (float)texOffX + depth;
      float f6 = (float)texOffX + depth + width;
      float f7 = (float)texOffX + depth + width + width;
      float f8 = (float)texOffX + depth + width + depth;
      float f9 = (float)texOffX + depth + width + depth + width;
      float f10 = (float)texOffY;
      float f11 = (float)texOffY + depth;
      float f12 = (float)texOffY + depth + height;
      this.quads[2] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
      this.quads[3] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{modelrenderer$positiontexturevertex1, modelrenderer$positiontexturevertex2, modelrenderer$positiontexturevertex6, modelrenderer$positiontexturevertex5}, f6, f11, f7, f10, texWidth, texHeight, mirorIn, Direction.UP);
      this.quads[1] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex6, modelrenderer$positiontexturevertex2}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
      this.quads[4] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{modelrenderer$positiontexturevertex, modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex2, modelrenderer$positiontexturevertex1}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
      this.quads[0] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex, modelrenderer$positiontexturevertex1, modelrenderer$positiontexturevertex5}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
      this.quads[5] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex5, modelrenderer$positiontexturevertex6}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
   }
}