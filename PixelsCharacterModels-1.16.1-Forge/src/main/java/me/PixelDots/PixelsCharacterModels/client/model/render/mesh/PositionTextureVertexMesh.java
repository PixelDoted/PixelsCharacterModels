package me.PixelDots.PixelsCharacterModels.client.model.render.mesh;

import net.minecraft.client.renderer.Vector3f;

public class PositionTextureVertexMesh 
{
	public Vector3f position;
    public final float textureU;
    public final float textureV;

    public PositionTextureVertexMesh(float x, float y, float z, float texU, float texV) {
       this(new Vector3f(x, y, z), texU, texV);
    }

    public PositionTextureVertexMesh setTextureUV(float texU, float texV) {
       return new PositionTextureVertexMesh(this.position, texU, texV);
    }

    public PositionTextureVertexMesh(Vector3f posIn, float texU, float texV) {
       this.position = posIn;
       this.textureU = texU;
       this.textureV = texV;
    }
}
