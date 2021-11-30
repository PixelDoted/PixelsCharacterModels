package me.PixelDots.PixelsCharacterModels.client.model.render.mesh;

import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec2;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraft.util.Direction;

public class TexturedQuadMesh 
{
	public final PositionTextureVertexMesh[] vertexPositions;
    //public final Vector3f normal;
    public final MapVec3[] normals;

    public TexturedQuadMesh(PositionTextureVertexMesh[] vertices, MapVec2[] textures, MapVec3[] normals, float textWidth, float textHeight, boolean mirror) {
    	this.vertexPositions = vertices;
    	float f = 0.0F / textWidth;
    	float f1 = 0.0F / textHeight;
    	for (int i = 0; i < vertices.length; i++) {
    		vertices[i] = vertices[i].setTextureUV(textures[i].X / textWidth - f, textures[i].Y / textHeight + f1);
    		//vertices[i].position = new Vector3f(textures[i].X / textWidth - f, textures[i].Y / textHeight + f1, 0);
    	}
    	if (mirror) {
    		int i = vertices.length;
    		
    		for (int j = 0; j < i / 2; j++) {
    			PositionTextureVertexMesh positiontexturevertex = vertices[j];
    			vertices[j] = vertices[i - 1 - j];
    			vertices[i - 1 - j] = positiontexturevertex;
    		}
    	}
    	this.normals = normals;
    	/*this.normal = normals[0].toVec();
    	if (mirror) this.normal.mul(-1.0F, 1.0F, 1.0F);*/
    }
    
    public TexturedQuadMesh(PositionTextureVertexMesh[] positionsIn, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn) {
       this.vertexPositions = positionsIn;
       float f = 0.0F / texWidth;
       float f1 = 0.0F / texHeight;
       positionsIn[0] = positionsIn[0].setTextureUV(u2 / texWidth - f, v1 / texHeight + f1);
       positionsIn[1] = positionsIn[1].setTextureUV(u1 / texWidth + f, v1 / texHeight + f1);
       positionsIn[2] = positionsIn[2].setTextureUV(u1 / texWidth + f, v2 / texHeight - f1);
       positionsIn[3] = positionsIn[3].setTextureUV(u2 / texWidth - f, v2 / texHeight - f1);
       if (mirrorIn) {
          int i = positionsIn.length;

          for(int j = 0; j < i / 2; ++j) {
             PositionTextureVertexMesh positiontexturevertex = positionsIn[j];
             positionsIn[j] = positionsIn[i - 1 - j];
             positionsIn[i - 1 - j] = positiontexturevertex;
          }
       }
       this.normals = new MapVec3[4];
       this.normals[0] = new MapVec3(directionIn.toVector3f().getX(), directionIn.toVector3f().getY(), directionIn.toVector3f().getZ());
       this.normals[1] = new MapVec3(directionIn.toVector3f().getX(), directionIn.toVector3f().getY(), directionIn.toVector3f().getZ());
       this.normals[2] = new MapVec3(directionIn.toVector3f().getX(), directionIn.toVector3f().getY(), directionIn.toVector3f().getZ());
       this.normals[3] = new MapVec3(directionIn.toVector3f().getX(), directionIn.toVector3f().getY(), directionIn.toVector3f().getZ());
       /*if (mirrorIn) {
          this.normals[0].mul(-1.0F, 1.0F, 1.0F);
       }*/

    }
}
