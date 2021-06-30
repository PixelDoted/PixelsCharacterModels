package me.pixeldots.pixelscharactermodels.model.part.model.mesh;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.utils.MapModelVectors;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
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

	public ModelPartMesh(MapModelVectors meshData, MapVec3 Pos, MapVec3 Size, MapVec2 textureSize) {
		this.pos = Pos;
		this.size = Size;
		this.uv = new MapVec2(0,0);
		this.meshID = meshData.meshID;
		
		ModelMeshVertex[] verts = new ModelMeshVertex[meshData.Vertices.size()];
		for (int i = 0; i < verts.length; i++) {
			MapVec3 vert = new MapVec3(meshData.Vertices.get(i).split(" ")[1], meshData.Vertices.get(i).split(" ")[2], meshData.Vertices.get(i).split(" ")[3]);
			verts[i] = new ModelMeshVertex(Pos.X+(vert.X*Size.X), Pos.Y+(vert.Y*Size.Y), Pos.Z+(vert.Z*Size.Z), meshData.VertexUVs.get(i).split(" ")[1], meshData.VertexUVs.get(i).split(" ")[2]);
		}
		List<ModelMeshQuad> quads = new ArrayList<ModelMeshQuad>();
		for (int i = 0; i < meshData.parsedFaces.size(); i++) {
			List<ModelMeshVertex> FaceVerts = new ArrayList<ModelMeshVertex>();
			for (int v = 0; v < meshData.parsedFaces.get(i).size(); v++) {
				String[] vertexIDs = meshData.parsedFaces.get(i).get(v).split("/");
				int vertexID = Math.round(Float.parseFloat(vertexIDs[0]))-1;
				int uvID = Math.round(Float.parseFloat(vertexIDs[1]))-1;
				MapVec3 vertex = new MapVec3(Math.round(Float.parseFloat(meshData.Vertices.get(vertexID).split(" ")[1])), Math.round(Float.parseFloat(meshData.Vertices.get(vertexID).split(" ")[2])), Math.round(Float.parseFloat(meshData.Vertices.get(vertexID).split(" ")[3])));
				MapVec2 uv = new MapVec2(Math.round(Float.parseFloat(meshData.VertexUVs.get(uvID).split(" ")[1])), Math.round(Float.parseFloat(meshData.VertexUVs.get(uvID).split(" ")[2])));
				FaceVerts.add(new ModelMeshVertex(vertex.X*Size.X-Pos.X, vertex.Y*Size.Y-Pos.Y, vertex.Z*Size.Z-Pos.Z, uv.X, uv.Y));
			}
			quads.add(new ModelMeshQuad(FaceVerts, textureSize.X, textureSize.Y, new MapVec3()));
		}
		this.sides = new ModelMeshQuad[quads.size()];
		for (int i = 0; i < quads.size(); i++) {
			this.sides[i] = quads.get(i);
		}
	}
   
	public void render(MatrixStack.Entry entry, int light, int overlay, float red, float green, float blue, float alpha, PlayerEntity entity) {
		RenderSystem.setShaderTexture(0, ((AbstractClientPlayerEntity)entity).getSkinTexture());
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.enableDepthTest();
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();
		Matrix4f m = entry.getModel();
		Matrix3f n = entry.getNormal();
		for (int i = 0; i < sides.length; i++) {
			buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
			for (int j = 0; j < sides[i].vertices.length; j++) {
				buffer.vertex(m, sides[i].vertices[j].pos.getX()/16, sides[i].vertices[j].pos.getY()/16, sides[i].vertices[j].pos.getZ()/16).texture(
						sides[i].vertices[j].u, sides[i].vertices[j].v).color(red, green, blue, alpha).normal(n, sides[i].direction.getX(), sides[i].direction.getY(), sides[i].direction.getZ()).next();
			}
			buffer.end();
			BufferRenderer.draw(buffer);
		}
	}
}