package me.pixeldots.pixelscharactermodels.model.part.mesh;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import me.pixeldots.pixelscharactermodels.utils.MapModelVectors;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class ModelPartMesh {
	public final ModelMeshQuad[] sides;
	
	public String name;
	public MapVec3 pos;
	public MapVec3 size;
	public MapVec2 uv;
	public String meshID;
	public Identifier texture = null;
	public String textureFile = "";

	public ModelPartMesh(MapModelVectors meshData, MapVec3 Pos, MapVec3 Size, MapVec2 textureOffset, String name) {
		this.pos = Pos;
		this.size = Size;
		this.uv = textureOffset;
		this.meshID = meshData.meshID;
		this.name = name;
		
		this.sides = new ModelMeshQuad[meshData.parsedFaces.size()];
		for (int i = 0; i < this.sides.length; i++) {
			List<String> faces = meshData.parsedFaces.get(i);
			List<ModelMeshVertex> vertices = new ArrayList<ModelMeshVertex>();
			for (int j = 0; j < faces.size(); j++) {
				String vertex = meshData.Vertices.get(Integer.parseInt(faces.get(j).split("/")[0])-1);
				MapVec2 vertexUV = meshData.parsedUVs.get(Integer.parseInt(faces.get(j).split("/")[1])-1);
				String vertexNormal = meshData.VertexNormals.get(Integer.parseInt(faces.get(j).split("/")[2])-1);
				ModelMeshVertex vert = new ModelMeshVertex(vertex.split(" ")[1], vertex.split(" ")[2], vertex.split(" ")[3],
						vertexNormal.split(" ")[1], vertexNormal.split(" ")[2], vertexNormal.split(" ")[3]
						, vertexUV.X, vertexUV.Y).setPosSize(Size, Pos);
				vertices.add(vert);
			}
			this.sides[i] = new ModelMeshQuad(vertices, 0, 0);
		}
	}
	
	public void render(MatrixStack.Entry entry, VertexConsumer vc, int light, int overlay, float red, float green, float blue, float alpha, PlayerEntity entity) {		
		if (texture != null) {
			RenderSystem.setShaderTexture(0, texture);
			RenderSystem.setShaderColor(red, green, blue, alpha);
			RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader);
			RenderSystem.enableDepthTest();
		}
		// Set RenderSystem lighting

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();
		Matrix4f m = entry.getModel();
		Matrix3f n = entry.getNormal();
		int lightUV = LightmapTextureManager.getBlockLightCoordinates(light);
		
		for (int i = 0; i < sides.length; i++) {
			buffer.begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
			for (int j = 0; j < sides[i].vertices.length; j++) {
				ModelMeshVertex vertex = sides[i].vertices[j];
				buffer.vertex(m, vertex.pos.getX()/16, vertex.pos.getY()/16, vertex.pos.getZ()/16).color(red, green, blue, alpha)
					.texture(vertex.u, vertex.v).light(light).normal(n, vertex.normal.getX(), vertex.normal.getY(), vertex.normal.getZ()).next();
			}
			buffer.end();
			BufferRenderer.draw(buffer);
		}
	}
	
}