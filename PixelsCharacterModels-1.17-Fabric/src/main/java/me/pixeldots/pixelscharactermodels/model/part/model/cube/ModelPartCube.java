package me.pixeldots.pixelscharactermodels.model.part.model.cube;

import java.io.BufferedReader;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.accessors.WorldRendererAccessor;
import me.pixeldots.pixelscharactermodels.accessors.MinecraftClientAccessor;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.client.MinecraftClient;

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
   
	public void render(MatrixStack.Entry entry, int light, int overlay, float red, float green, float blue, float alpha, PlayerEntity entity) {
		if (texture != null) {
			RenderSystem.setShaderTexture(0, texture);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.enableDepthTest();
		}
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();
		Matrix4f m = entry.getModel();
		Matrix3f n = entry.getNormal();
		int myLight = 15;//getLighting();

		for (int i = 0; i < sides.length; i++) {
			buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
			for (int j = 0; j < sides[i].vertices.length; j++) {
				buffer.vertex(m, sides[i].vertices[j].pos.getX()/16, sides[i].vertices[j].pos.getY()/16, sides[i].vertices[j].pos.getZ()/16).texture(
						sides[i].vertices[j].u, sides[i].vertices[j].v).color(red, green, blue, alpha).light(myLight).next();//.normal(n, sides[i].direction.getX(), sides[i].direction.getY(), sides[i].direction.getZ()).next();
			}
			buffer.end();
			BufferRenderer.draw(buffer);
		}
    }

	public int getLighting() {
		MinecraftClientAccessor client = ((MinecraftClientAccessor)(Object)MinecraftClient.getInstance());
		return MinecraftClient.getInstance().getEntityRenderDispatcher().getLight(
			MinecraftClient.getInstance().player, client.getPaused() ? client.getPausedTickDelta() : client.getRenderTickCounter().tickDelta);
	}
}