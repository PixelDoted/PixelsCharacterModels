package me.pixeldots.pixelscharactermodels.model.mesh;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.utils.MapModelVectors;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3f;

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
			for (String face : faces) {
				String[] slash_split = face.split("/");
				String vertex = meshData.Vertices.get(Integer.parseInt(slash_split[0])-1);
				MapVec2 vertexUV = meshData.parsedUVs.get(Integer.parseInt(slash_split[1])-1);
				String vertexNormal = meshData.VertexNormals.get(Integer.parseInt(slash_split[2])-1);

				String[] SSVertex = vertex.split(" ");
				String[] SSVertexNormal = vertexNormal.split(" ");
				ModelMeshVertex vert = new ModelMeshVertex(SSVertex[1], SSVertex[2], SSVertex[3],
						SSVertexNormal[1], SSVertexNormal[2], SSVertexNormal[3]
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

			renderTextured(entry,vc,light,overlay,red,green,blue,alpha, entity);
			return;
		}

		Matrix4f m = entry.getModel();
		Matrix3f n = entry.getNormal();
		for (ModelMeshQuad side : sides) {
			int length = side.vertices.length;
			for (int j = 0; j < 4; j++) {
				ModelMeshVertex vertex = j >= length ? null : side.vertices[j];
				if (vertex == null) {
					ModelMeshVertex a = side.vertices[length-1];
					ModelMeshVertex b = side.vertices[0];
					Vec3f pos = new Vec3f((a.pos.getX()+b.pos.getX())/2f,(a.pos.getY()+b.pos.getY())/2f,(a.pos.getZ()+b.pos.getZ())/2f);
					Vec3f normal = new Vec3f((a.normal.getX()+b.normal.getX())/2f,(a.normal.getY()+b.normal.getY())/2f,(a.normal.getZ()+b.normal.getZ())/2f);
					Vec2f uv = new Vec2f((a.u+b.u)/2f,(a.v+b.v)/2f);
					vertex = new ModelMeshVertex(pos, normal, uv.x, uv.y);
				}


				vc.vertex(m, vertex.pos.getX()/16, vertex.pos.getY()/16, vertex.pos.getZ()/16).color(red, green, blue, alpha)
					.texture(vertex.u, vertex.v).overlay(overlay).light(light).normal(n, vertex.normal.getX(), vertex.normal.getY(), vertex.normal.getZ()).next();
			}
		}
	}

	public void renderTextured(MatrixStack.Entry entry, VertexConsumer vc, int light, int overlay, float red, float green, float blue, float alpha, PlayerEntity entity) {		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();
		Matrix4f m = entry.getModel();
		Matrix3f n = entry.getNormal();
		
		for (ModelMeshQuad side : sides) {
			buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
			int length = side.vertices.length;
			for (int j = 0; j < 4; j++) {
				ModelMeshVertex vertex = j >= length ? null : side.vertices[j];
				if (vertex == null) {
					ModelMeshVertex a = side.vertices[length-1];
					ModelMeshVertex b = side.vertices[0];
					Vec3f pos = new Vec3f((a.pos.getX()+b.pos.getX())/2f,(a.pos.getY()+b.pos.getY())/2f,(a.pos.getZ()+b.pos.getZ())/2f);
					Vec3f normal = new Vec3f((a.normal.getX()+b.normal.getX())/2f,(a.normal.getY()+b.normal.getY())/2f,(a.normal.getZ()+b.normal.getZ())/2f);
					Vec2f uv = new Vec2f((a.u+b.u)/2f,(a.v+b.v)/2f);
					vertex = new ModelMeshVertex(pos, normal, uv.x, uv.y);
				}


				buffer.vertex(m, vertex.pos.getX()/16, vertex.pos.getY()/16, vertex.pos.getZ()/16).color(red, green, blue, alpha)
					.texture(vertex.u, vertex.v).overlay(overlay).light(light).normal(n, vertex.normal.getX(), vertex.normal.getY(), vertex.normal.getZ()).next();
			}
			buffer.end();
			BufferRenderer.draw(buffer);
		}
	}
	
}