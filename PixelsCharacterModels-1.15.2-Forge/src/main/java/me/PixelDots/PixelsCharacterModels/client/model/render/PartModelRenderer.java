package me.PixelDots.PixelsCharacterModels.client.model.render;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.client.model.ModelPart;
import me.PixelDots.PixelsCharacterModels.client.model.render.mesh.PositionTextureVertexMesh;
import me.PixelDots.PixelsCharacterModels.client.model.render.mesh.TexturedQuadMesh;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

public class PartModelRenderer extends ModelRenderer 
{
	
	public float textureWidth = 64.0F;
	public float textureHeight = 32.0F;
	public int textureOffsetX;
	public int textureOffsetY;
	
	public UUID uuid = null;
	public String fakeLimbID = "";
	
	public int packedLight = 0;
	public int packedOverlay = 0;
	public boolean showThis = true;
	
	public MapVec3 animRot = new MapVec3(0,0,0);
	public MapVec3 animPoint = new MapVec3(0,0,0);
	public boolean useAnimRot = false;
	public boolean useAnimPoint = false;
	
	public boolean hasParent = false;
	public MapVec3 ParentRotPoint = new MapVec3(0,0,0);
	public MapVec3 ParentRotAngle = new MapVec3(0,0,0);
	
    public MapVec3 anchor = new MapVec3(0,0,0);
    public MapVec3 RotAnchor = new MapVec3(0,0,0);
	
	public ModelPart data = new ModelPart();
	public ModelPart myData = new ModelPart();
	
	public String meshType = "cube";
	public float lerpTime = .975f;
	
	public ModelRenderer renderer = null;
	public ModelRenderer rendererWear = null;
	
	public List<ModelMesh> meshList = new ArrayList<ModelMesh>();
	public List<ModelBoxMesh> boxList = new ArrayList<ModelBoxMesh>();
	public List<PartModelRenderer> childModels = new ArrayList<PartModelRenderer>();
	
	public ResourceLocation texture = null;
	
	public PartModelRenderer(Model model) {
		super(model);
	}
	public PartModelRenderer(Model model, ModelRenderer renderer) {
		this(model);
		this.renderer = renderer;
	}
	public PartModelRenderer(Model model, ModelRenderer renderer, ModelPart data) {
		this(model, renderer);
		this.data = data;
	}
	public PartModelRenderer(Model model, ModelRenderer renderer, ModelRenderer rendererWear, ModelPart data) {
		this(model, renderer, data);
		this.rendererWear = rendererWear;
	}
	public PartModelRenderer(Model model, String type) {
		this(model);
		this.meshType = type;
	}
	public PartModelRenderer(Model model, int TOX, int TOY, int TW, int TH) {
		super(model, TOX, TOY);
		textureWidth = TW;
		textureHeight = TH;
		textureOffsetX = TOX;
		textureOffsetY = TOY;
		this.meshType = "cube";
	}
	public PartModelRenderer(Model model, int TOX, int TOY) {
		super(model, TOX, TOY);
		textureOffsetX = TOX;
		textureOffsetY = TOY;
		this.meshType = "cube";
	}
	
	public void LoadOther() {
		if (renderer != null) {
			renderer.rotateAngleX = this.rotateAngleX;
			renderer.rotateAngleY = this.rotateAngleY;
			renderer.rotateAngleZ = this.rotateAngleZ;
			
			renderer.rotationPointX = this.rotationPointX;
			renderer.rotationPointY = this.rotationPointY;
			renderer.rotationPointZ = this.rotationPointZ;
		}
		if (!(this.childModels.isEmpty())) {
			for (PartModelRenderer child : this.childModels) {
				child.rotateAngleX = this.rotateAngleX;
				child.rotateAngleY = this.rotateAngleY;
				child.rotateAngleZ = this.rotateAngleZ;
				
				child.rotationPointX = this.rotationPointX;
				child.rotationPointY = this.rotationPointY;
				child.rotationPointZ = this.rotationPointZ;
			}
		}
	} 
	
	public void copyParentValues() {
		if (hasParent == false) {
    		this.rotateAngleX = (anchor.X) + (useAnimRot ? animRot.X : 0);
        	this.rotateAngleY = (anchor.Y) + (useAnimRot ? animRot.Y : 0);// + (float)-(ParentRot.Y));// + parent.rotateAngleY);
        	this.rotateAngleZ = (float)Math.PI +(anchor.Z) + (useAnimRot ? animRot.Z : 0);
        	this.rotationPointX = (useAnimPoint ? animPoint.X : 0)+data.RotPointX+0;// + data.X) * (data.Scale == 0 ? 1 : data.Scale);
        	this.rotationPointY = (useAnimPoint ? animPoint.Y : 0)+data.RotPointY+22.5f;// + data.Y) * (data.Scale == 0 ? 1 : data.Scale);
        	this.rotationPointZ = (useAnimPoint ? animPoint.Z : 0)+data.RotPointZ+0;// + data.Z) * (data.Scale == 0 ? 1 : data.Scale);
        	//Log.info(ParentRot.X + ":TRASH:" + ParentRot.Y + ":TRASH:" + ParentRot.Z);
        	return;
    	}
		this.rotateAngleX += anchor.X + (useAnimRot ? animRot.X : 0);
		this.rotateAngleY += anchor.Y + (useAnimRot ? animRot.Y : 0);
		this.rotateAngleZ += anchor.Z + (useAnimRot ? animRot.Z : 0);
		
		this.rotationPointX += (useAnimPoint ? animPoint.X : 0);
		this.rotationPointY += (useAnimPoint ? animPoint.Y : 0);
		this.rotationPointZ += (useAnimPoint ? animPoint.Z : 0);
    }
	
	public void isFakeLimb() {
		if (uuid != null && fakeLimbID != "") {
			if (Main.Data.playerData.containsKey(uuid)) {
				if (fakeLimbID.equalsIgnoreCase("Head")) Main.Data.playerData.get(uuid).FakeHead = this;
				if (fakeLimbID.equalsIgnoreCase("Body")) Main.Data.playerData.get(uuid).FakeBody = this;
				if (fakeLimbID.equalsIgnoreCase("LeftLeg")) Main.Data.playerData.get(uuid).FakeLeftLeg = this;
				if (fakeLimbID.equalsIgnoreCase("LeftArm")) Main.Data.playerData.get(uuid).FakeLeftArm = this;
				if (fakeLimbID.equalsIgnoreCase("RightLeg")) Main.Data.playerData.get(uuid).FakeRightLeg = this;
				if (fakeLimbID.equalsIgnoreCase("RightArm")) Main.Data.playerData.get(uuid).FakeRightArm = this;
			}
		}
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
		this.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		LoadOther();
		copyParentValues();
		matrixStackIn.push();
		if (showThis) {
			if (data != null) {
				if (data.Show) {
					if (!(myData.toString() == data.toString())) {
						matrixStackIn.translate(data.X, data.Y, data.Z);
						matrixStackIn.scale(data.Scale, data.Scale, data.Scale);
						
						if (renderer != null) renderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						if (rendererWear != null)
							rendererWear.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						
						translateRotateMesh(matrixStackIn);
						this.doRender(matrixStackIn.getLast(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						this.doRenderMesh(matrixStackIn.getLast(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						for(PartModelRenderer modelrenderer : this.childModels) {
							modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				        }
						matrixStackIn.scale(-data.Scale, -data.Scale, -data.Scale);
						matrixStackIn.translate(-data.X, -data.Y, -data.Z);
						myData = data;
					}
					else {
						if (renderer != null) renderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						if (rendererWear != null)
							rendererWear.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						
						translateRotateMesh(matrixStackIn);
						this.doRender(matrixStackIn.getLast(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						this.doRenderMesh(matrixStackIn.getLast(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
						for(PartModelRenderer modelrenderer : this.childModels) {
							modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				        }
					}
				}
			}
			else {
				renderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				if (rendererWear != null)
					rendererWear.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				
				for(PartModelRenderer modelrenderer : this.childModels) {
					modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		        }
			}
		}
		matrixStackIn.pop();
		data.Renderer = this;
	}
	
	public void translateRotateMesh(MatrixStack matrixStackIn) {
	      matrixStackIn.translate((double)(this.rotationPointX / 16.0F), (double)(this.rotationPointY / 16.0F), (double)(this.rotationPointZ / 16.0F));
	      if (this.rotateAngleZ != 0.0F) {
	         matrixStackIn.rotate(Vector3f.ZP.rotation(this.rotateAngleZ));
	      }

	      if (this.rotateAngleY != 0.0F) {
	    	  matrixStackIn.rotate(Vector3f.YP.rotation(this.rotateAngleY));
	      }

	      if (this.rotateAngleX != 0.0F) {
	    	  matrixStackIn.rotate(Vector3f.XP.rotation(this.rotateAngleX));
	      }
	      matrixStackIn.translate(RotAnchor.X, RotAnchor.Y, RotAnchor.Z);
	}
	
	public void addChild(PartModelRenderer renderer) {
		this.childModels.add(renderer);
	}
	
	private void doRenderMesh(MatrixStack.Entry matrixEntryIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		Matrix4f matrix4f = matrixEntryIn.getMatrix();
	    Matrix3f matrix3f = matrixEntryIn.getNormal();
	    
	    Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder bufferbuilder = tessellator.getBuffer();
	    
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableLighting();
	    
        VertexFormat formats = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(DefaultVertexFormats.POSITION_3F).add(DefaultVertexFormats.TEX_2F).add(DefaultVertexFormats.NORMAL_3B).add(DefaultVertexFormats.PADDING_1B).build());
        bufferbuilder.begin(4, formats);
        
        if (texture != null) Minecraft.getInstance().textureManager.bindTexture(texture);
        //bufferIn.addQuad(matrixEntryIn, quad, red, green, blue, packedLightIn, packedOverlayIn);
        /*int vertices = 1;
        BakedQuadBuilder builder = new BakedQuadBuilder(null);*/
	    for(ModelMesh modelmesh : this.meshList) {
	       for(TexturedQuadMesh texturedquad : modelmesh.quads) {

	          for(int i = 0; i < texturedquad.vertexPositions.length; ++i) {
	             PositionTextureVertexMesh positiontexturevertex = texturedquad.vertexPositions[i];
	             float f3 = positiontexturevertex.position.getX() / 16.0F;
	             float f4 = positiontexturevertex.position.getY() / 16.0F;
	             float f5 = positiontexturevertex.position.getZ() / 16.0F;
	             Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
	             vector4f.transform(matrix4f);
	             
	             Vector3f nor = texturedquad.normals[i].toVec();
	             nor.transform(matrix3f);
	             
	             float f = nor.getX();
	             float f1 = nor.getY();
	             float f2 = nor.getZ();
	             
	             /*builder.put(0, new float[] {f3,f4,f5});
	             
	             Vector4f tintedColor = new Vector4f(0,0,0,0);
	             putVertexData(builder, vector4f, new Vec2f(positiontexturevertex.textureU, positiontexturevertex.textureV), nor, tintedColor, new Vec2f(positiontexturevertex.textureU, positiontexturevertex.textureV), null, false);
	     	     builder.setQuadOrientation(Direction.getFacingFromVector(f, f1,f2));
	     	     if (i == 4 * vertices) {
	     	    	 BakedQuad quad = builder.build();
	     	    	 vertices++;
	     	    	 builder = new BakedQuadBuilder(null);
	     	     }*/
	             
	     	     bufferbuilder.addVertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, positiontexturevertex.textureU, positiontexturevertex.textureV, packedOverlayIn, packedLightIn, f, f1, f2);
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
	}
	
	private void doRender(MatrixStack.Entry matrixEntryIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	      Matrix4f matrix4f = matrixEntryIn.getMatrix();
	      Matrix3f matrix3f = matrixEntryIn.getNormal();

	      for(ModelBoxMesh modelbox : this.boxList) {
		       for(TexturedQuadMesh texturedquad : modelbox.quads) {
		          for(int i = 0; i < texturedquad.vertexPositions.length; ++i) {
		             PositionTextureVertexMesh positiontexturevertex = texturedquad.vertexPositions[i];
		             float f3 = positiontexturevertex.position.getX() / 16.0F;
		             float f4 = positiontexturevertex.position.getY() / 16.0F;
		             float f5 = positiontexturevertex.position.getZ() / 16.0F;
		             Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
		             vector4f.transform(matrix4f);
		             
		             Vector3f nor = texturedquad.normals[i].toVec();
		             nor.transform(matrix3f);
		             
		             float f = nor.getX();
		             float f1 = nor.getY();
		             float f2 = nor.getZ();
		             bufferIn.addVertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, positiontexturevertex.textureU, positiontexturevertex.textureV, packedOverlayIn, packedLightIn, f, f1, f2);
		          }
		       }
		    }

	   }
	
	public void addMesh(float scale, float X, float Y, float Z) {
    	this.meshList.add(new ModelMesh(this, meshType, X, Y, Z, scale));
    }
    
    public void addMesh(float scale, float X, float Y, float Z, int SX, int SY, int SZ) {
    	this.meshList.add(new ModelMesh(this, meshType, X, Y, Z, SX, SY, SZ, scale, 0, 0));
    }
    
    public void addMesh(float scale, float X, float Y, float Z, int SX, int SY, int SZ, int texU, int texV) {
    	this.meshList.add(new ModelMesh(this, meshType, X, Y, Z, SX, SY, SZ, scale, texU, texV));
    }
    
    public ModelRenderer addBox(String partName, float x, float y, float z, int width, int height, int depth, float delta, int texX, int texY) {
        this.setTextureOffset(texX, texY);
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, (float)width, (float)height, (float)depth, delta, delta, delta, this.mirror, false);
        return this;
     }
    

     public ModelRenderer addBox(float x, float y, float z, float width, float height, float depth) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, this.mirror, false);
        return this;
     }

     public ModelRenderer addBox(float x, float y, float z, float width, float height, float depth, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, mirrorIn, false);
        return this;
     }

     public void addBox(float x, float y, float z, float width, float height, float depth, float delta) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror, false);
     }

     public void addBox(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror, false);
     }

     public void addBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, mirrorIn, false);
     }

     private void addBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, boolean p_228305_13_) {
        this.boxList.add(new ModelBoxMesh(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, this.textureWidth, this.textureHeight));
     }
	
}
