package me.pixeldots.pixelscharactermodels.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelCubeQuad;
import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelCubeVertex;
import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.model.part.model.mesh.ModelMeshQuad;
import me.pixeldots.pixelscharactermodels.model.part.model.mesh.ModelMeshVertex;
import me.pixeldots.pixelscharactermodels.model.part.model.mesh.ModelPartMesh;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Mixin(ModelPart.class)
public class ModelPartMixin {
	
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", cancellable = true)
	public void renderMainHEAD(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		PixelsCharacterModels.Rendering.renderPartHead(matrices, vertices, (ModelPart)(Object)this, info);
    }
	
	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", cancellable = true)
	public void renderMainTAIL(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		PixelsCharacterModels.Rendering.renderPartTail(matrices, vertices, (ModelPart)(Object)this, info);
    }
	
	@Inject(at = @At("HEAD"), method = "renderCuboids(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V")
	public void renderCuboids(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
		/*ModelPartData data = PixelsCharacterModels.dataPackets.get((ModelPart)(Object)this);
		for (int i = 0; i < data.cubes.size(); i++) {
			data.cubes.get(i).renderCuboid(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		}*/
	}
	
	/*public void renderMesh(MatrixStack.Entry mst, float x, float y, float z, float w, float h, float d, VertexConsumer vertexConsumer, int overlay, int light, ModelPartMesh mesh) {
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder t = tes.getBuffer();
		Matrix4f m = mst.getModel();
		for (int i = 0; i < mesh.sides.length; i++) {
			ModelMeshQuad quad = mesh.sides[i];
			VertexFormat.DrawMode mode = VertexFormat.DrawMode.QUADS;
			if (quad.vertices.length == 3) mode = VertexFormat.DrawMode.TRIANGLES;
			t.begin(mode, VertexFormats.POSITION_TEXTURE);
			for (int j = 0; j < quad.vertices.length; j++) {
				ModelMeshVertex vertex = quad.vertices[j];
				t.vertex(m, x+vertex.pos.getX()*w, x+vertex.pos.getY()*h, x+vertex.pos.getZ()*d).texture(vertex.u, vertex.v).next();
			}
			tes.draw();
		}
	}
	
	public void renderCube(MatrixStack.Entry mst, float x, float y, float z, float w, float h, float d, VertexConsumer vertexConsumer, int overlay, int light, ModelPartCube cube) {
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder t = tes.getBuffer();
		Matrix4f m = mst.getModel();
		for (int i = 0; i < cube.sides.length; i++) {
			ModelCubeQuad quad = cube.sides[i];
			//t.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
			for (int j = 0; j < 4; j++) {
				ModelCubeVertex vertex = quad.vertices[j];
				vertexConsumer.vertex(vertex.pos.getX(), vertex.pos.getY(), vertex.pos.getZ(), 1, 1, 1, 1, vertex.u, vertex.v, overlay, light, quad.direction.getX(), quad.direction.getY(), quad.direction.getZ());
				//t.vertex(m, vertex.pos.getX(), vertex.pos.getY(), vertex.pos.getZ()).texture(vertex.u, vertex.v).next();
			}
			//tes.draw();
		}
	}
	
	public void drawLine(MatrixStack.Entry matrix) {
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		//RenderSystem.lineWidth(4.0F);
		
		Matrix4f model = matrix.getModel();
		bufferBuilder.vertex(model, 15F, 15F, 0F).color(255, 255, 255, 255).normal(1F, 0F, 0F).next();
		bufferBuilder.vertex(model, 200F, 150F, 0F).color(255, 255, 0, 0).normal(1F, 0F, 0F).next();
		bufferBuilder.vertex(model, 200F, 100F, 0F).color(255, 0, 0, 255).normal(1F, 1F, 0F).next();
		bufferBuilder.vertex(model, 250F, 150F, 0F).color(255, 0, 0, 255).normal(1F, 1F, 0F).next();
		tessellator.draw();
	}
	
	public void drawTexturedCube(MatrixStack.Entry mst, float x, float y, float z, float w, float h, float d, VertexConsumer vertex, int overlay, int light) {
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder t = tes.getBuffer();
		Matrix4f m = mst.getModel();
		Matrix3f normal = mst.getNormal();
		t.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		t.vertex(m, x + w, y, z).texture(1, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y, z).texture(0, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y + h, z).texture(0, 0).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y + h, z).texture(1, 0).normal(normal, 0, 0, 0).next();
		tes.draw();

		t.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		t.vertex(m, x, y, z + d).texture(1, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y, z + d).texture(0, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y + h, z + d).texture(0, 0).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y + h, z + d).texture(1, 0).normal(normal, 0, 0, 0).next();
		tes.draw();

		t.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		t.vertex(m, x + w, y, z + d).texture(1, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y, z).texture(0, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y + h, z).texture(0, 0).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y + h, z + d).texture(1, 0).normal(normal, 0, 0, 0).next();
		tes.draw();

		t.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		t.vertex(m, x, y, z).texture(1, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y, z + d).texture(0, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y + h, z + d).texture(0, 0).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y + h, z).texture(1, 0).normal(normal, 0, 0, 0).next();
		tes.draw();

		t.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		t.vertex(m, x + w, y, z).texture(1, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y, z + d).texture(0, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y, z + d).texture(0, 0).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y, z).texture(1, 0).normal(normal, 0, 0, 0).next();
		tes.draw();

		t.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		t.vertex(m, x + w, y + h, z + d).texture(1, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x + w, y + h, z).texture(0, 1).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y + h, z).texture(0, 0).normal(normal, 0, 0, 0).next();
		t.vertex(m, x, y + h, z + d).texture(1, 0).normal(normal, 0, 0, 0).next();
		tes.draw();
	}*/
	
	/*public void renderMeshes(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		ModelPartData data = PixelsCharacterModels.dataPackets.get((ModelPart)(Object)this);
		if (data.meshes.size() == 0) return;
		
		Matrix4f matrix4f = matrices.getModel();
		Matrix3f matrix3f = matrices.getNormal();
	    ObjectListIterator var11 = data.meshes.iterator();

	    while(var11.hasNext()) {
	       ModelPartMesh cuboid = (ModelPartMesh)var11.next();
	       ModelMeshQuad[] var13 = cuboid.sides;
	       int var14 = var13.length;

	       for(int var15 = 0; var15 < var14; ++var15) {
	          ModelMeshQuad quad = var13[var15];
	          Vector3f vector3f = quad.direction.copy();
	          vector3f.transform(matrix3f);
	          float f = vector3f.getX();
	          float g = vector3f.getY();
	          float h = vector3f.getZ();

	          for(int i = 0; i < quad.vertices.length; ++i) {
	             ModelMeshVertex vertex = quad.vertices[i];
	             float j = vertex.pos.getX() / 16.0F;
	             float k = vertex.pos.getY() / 16.0F;
	             float l = vertex.pos.getZ() / 16.0F;
	             Vector4f vector4f = new Vector4f(j, k, l, 1.0F);
	             vector4f.transform(matrix4f);
	             vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, f, g, h);
	          }
	       }
	    }
	}
	
	private void renderCubes(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {		
		Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder bufferbuilder = tessellator.getBuffer();
	    
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableLighting();
	    
        VertexFormat formats = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(VertexFormats.POSITION_ELEMENT).add(VertexFormats.TEXTURE_ELEMENT).add(VertexFormats.NORMAL_ELEMENT).add(VertexFormats.PADDING_ELEMENT).build());
        bufferbuilder.begin(4, formats);
		
		//Matrix4f m = matrices.getModel();
		Vector4f a = new Vector4f(16,16,0, 1); a.transform(m);
		Vector4f b = new Vector4f(16,16,16, 1); b.transform(m);
		Vector4f c = new Vector4f(0,16,-16, 1); c.transform(m);
		Vector4f d = new Vector4f(-16,16,-16, 1); d.transform(m);
		bufferbuilder.vertex(a.getX(), a.getY(), a.getZ(), red, green, blue, alpha, 1, 0, overlay, light, 0, 1, 0);
		bufferbuilder.vertex(b.getX(), b.getY(), b.getZ(), red, green, blue, alpha, 1, 1, overlay, light, 0, 1, 0);
		bufferbuilder.vertex(c.getX(), c.getY(), c.getZ(), red, green, blue, alpha, 0, -1, overlay, light, 0, 1, 0);
		bufferbuilder.vertex(d.getX(), d.getY(), d.getZ(), red, green, blue, alpha, -1, -1, overlay, light, 0, 1, 0);//
		
		ModelPartData data = PixelsCharacterModels.dataPackets.get((ModelPart)(Object)this);
		
		Matrix4f matrix4f = matrices.getModel();
		Matrix3f matrix3f = matrices.getNormal();
	    ObjectListIterator var11 = data.cubes.iterator();
	    
	    while(var11.hasNext()) {
	    	ModelPartCube cuboid = (ModelPartCube)var11.next();
	        ModelCubeQuad[] var13 = cuboid.sides;
	        int var14 = var13.length;

	        for(int var15 = 0; var15 < var14; ++var15) {
	           ModelCubeQuad quad = var13[var15];
	           Vector3f vector3f = quad.direction.copy();
	           vector3f.transform(matrix3f);
	           float f = vector3f.getX();
	           float g = vector3f.getY();
	           float h = vector3f.getZ();
	           
	           for(int i = 0; i < 4; ++i) {
	              ModelCubeVertex vertex = quad.vertices[i];
	              float j = vertex.pos.getX() / 16.0F;
	              float k = vertex.pos.getY() / 16.0F;
	              float l = vertex.pos.getZ() / 16.0F;
	              Vector4f vector4f = new Vector4f(j, k, l, 1.0F);
	              vector4f.transform(matrix4f); // 0, -1.001f, 0, 1, 1, 1
		     	  
	              bufferbuilder.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, f, g, h);
	              //vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, f, g, h);
	           }
	           
	        }
	    }
	    tessellator.draw();
	    RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableLighting();
	}*/
	
}
