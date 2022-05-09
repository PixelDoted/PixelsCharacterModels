package me.pixeldots.pixelscharactermodels.model.cube;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class ModelPartCube {
	public final ModelCubeQuad[] sides;
	public final float minX;
	public final float minY;
	public final float minZ;
	public final float maxX;
	public final float maxY;
	public final float maxZ;
	
	public String name;
	public MapVec3 pos;
	public MapVec3 size;
	public MapVec2 uv;
	public Identifier texture = null;
	public String textureFile = "";

	public ModelPartCube(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float textureWidth, float textureHeight, String name) {
		this.pos = new MapVec3(x, y, z);
		this.size = new MapVec3(sizeX, sizeY, sizeZ);
		this.uv = new MapVec2(u, v);
		this.name = name;
		
		x+=(sizeX*-.5f);
		y+=(sizeY*-1);
		z+=(sizeZ*-.5f);
		
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
        this.sides[2] = new ModelCubeQuad(new ModelCubeVertex[]{vertex6, vertex5, vertex, vertex2}, k, p, l, q, textureWidth, textureHeight, false, Direction.DOWN);
        this.sides[3] = new ModelCubeQuad(new ModelCubeVertex[]{vertex3, vertex4, vertex8, vertex7}, l, q, m, p, textureWidth, textureHeight, false, Direction.UP);
        this.sides[1] = new ModelCubeQuad(new ModelCubeVertex[]{vertex, vertex5, vertex8, vertex4}, j, q, k, r, textureWidth, textureHeight, false, Direction.WEST);
        this.sides[4] = new ModelCubeQuad(new ModelCubeVertex[]{vertex2, vertex, vertex4, vertex3}, k, q, l, r, textureWidth, textureHeight, false, Direction.NORTH);
        this.sides[0] = new ModelCubeQuad(new ModelCubeVertex[]{vertex6, vertex2, vertex3, vertex7}, l, q, n, r, textureWidth, textureHeight, false, Direction.EAST);
        this.sides[5] = new ModelCubeQuad(new ModelCubeVertex[]{vertex5, vertex6, vertex7, vertex8}, n, q, o, r, textureWidth, textureHeight, false, Direction.SOUTH);
	}

	public void render(TextureManager tm, MatrixStack.Entry entry, VertexConsumer vc, int light, int overlay, float red, float green, float blue, float alpha, PlayerEntity entity) {
		if (texture != null) {
			RenderSystem.setShaderTexture(0, texture);
			RenderSystem.setShaderColor(red, green, blue, alpha);
			RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader);
			RenderSystem.enableDepthTest();

			renderTextured(tm,entry,vc,light,overlay,red,green,blue,alpha,entity);
			return;
		}
		
		Matrix4f m = entry.getModel();
		Matrix3f n = entry.getNormal();
		
		for (ModelCubeQuad side : sides) {
			for (ModelCubeVertex vertex : side.vertices) {
				vc.vertex(m, vertex.pos.getX()/16, vertex.pos.getY()/16, vertex.pos.getZ()/16)
					.color(red, green, blue, alpha).texture(vertex.u, vertex.v).overlay(overlay)
					.light(light).normal(n, side.direction.getX(), side.direction.getY(), side.direction.getZ()).next();
			}
		}
    }

	public void renderTextured(TextureManager tm, MatrixStack.Entry entry, VertexConsumer vc, int light, int overlay, float red, float green, float blue, float alpha, PlayerEntity entity) {		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();
		Matrix4f m = entry.getModel();
		Matrix3f n = entry.getNormal();
				for (ModelCubeQuad side : sides) {
			buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
			for (ModelCubeVertex vertex : side.vertices) {
				buffer.vertex(m, vertex.pos.getX()/16, vertex.pos.getY()/16, vertex.pos.getZ()/16)
					.color(red, green, blue, alpha).texture(vertex.u, vertex.v)
					.light(light).normal(n, side.direction.getX(), side.direction.getY(), side.direction.getZ()).next();
			}
			buffer.end();
			BufferRenderer.draw(buffer);
		}
	}

}