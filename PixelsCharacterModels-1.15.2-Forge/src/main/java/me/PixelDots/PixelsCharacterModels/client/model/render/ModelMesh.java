package me.PixelDots.PixelsCharacterModels.client.model.render;

import java.util.ArrayList;
import java.util.List;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.client.model.render.mesh.MeshFace;
import me.PixelDots.PixelsCharacterModels.client.model.render.mesh.PositionTextureVertexMesh;
import me.PixelDots.PixelsCharacterModels.client.model.render.mesh.TexturedQuadMesh;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec2;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraft.util.Direction;

public class ModelMesh 
{
	public TexturedQuadMesh[] quads;
	public PositionTextureVertexMesh[] vertexPositions;

	public List<Float> Points = new ArrayList<Float>(); 
    
    public List<MapVec3> vertices = new ArrayList<MapVec3>();
    public List<MeshFace> faces = new ArrayList<MeshFace>();
    public List<MapVec2> vertText = new ArrayList<MapVec2>();
    public List<MapVec3> vertNor = new ArrayList<MapVec3>();

    public ModelMesh(PartModelRenderer renderer, String mesh) {
    	this(renderer, mesh, 0, 0, 0, 1, 1, 1, 0, 0, 0);
    }
    
    public ModelMesh(PartModelRenderer renderer, String mesh, float X, float Y, float Z, float scale) {
    	this(renderer, mesh, X, Y, Z, 1, 1, 1, scale, 0, 0);
    }
    
    public ModelMesh(PartModelRenderer renderer, String mesh, float X, float Y, float Z, int SX, int SY, int SZ, float scale, int texU, int texV) {
    	MeshGen(renderer, mesh, X, Y, Z, SX, SY, SZ, scale, texU, texV);
    }
    
    public void TestGen(PartModelRenderer renderer, String mesh, float X, float Y, float Z, int SX, int SY, int SZ, float scale, int texU, int texV) {
    	if (scale == 0) scale = 1;
    	
    	float deltaX = X - SX;
    	float deltaY = Y - SY;
    	float deltaZ = Z - SZ;
        this.quads = new TexturedQuadMesh[6];
        float f = X + SX;
        float f1 = Y + SY;
        float f2 = Z + SZ;
        X = X - deltaX;
        Y = Y - deltaY;
        Z = Z - deltaZ;
        f = f + deltaX;
        f1 = f1 + deltaY;
        f2 = f2 + deltaZ;
        if (renderer.mirror) {
           float f3 = f;
           f = X;
           X = f3;
        }

        PositionTextureVertexMesh positiontexturevertex7 = new PositionTextureVertexMesh(X, Y, Z, 0.0F, 0.0F);
        PositionTextureVertexMesh positiontexturevertex = new PositionTextureVertexMesh(f, Y, Z, 0.0F, 8.0F);
        PositionTextureVertexMesh positiontexturevertex1 = new PositionTextureVertexMesh(f, f1, Z, 8.0F, 8.0F);
        PositionTextureVertexMesh positiontexturevertex2 = new PositionTextureVertexMesh(X, f1, Z, 8.0F, 0.0F);
        PositionTextureVertexMesh positiontexturevertex3 = new PositionTextureVertexMesh(X, Y, f2, 0.0F, 0.0F);
        PositionTextureVertexMesh positiontexturevertex4 = new PositionTextureVertexMesh(f, Y, f2, 0.0F, 8.0F);
        PositionTextureVertexMesh positiontexturevertex5 = new PositionTextureVertexMesh(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertexMesh positiontexturevertex6 = new PositionTextureVertexMesh(X, f1, f2, 8.0F, 0.0F);
        float f4 = (float)X;
        float f5 = (float)X + SZ;
        float f6 = (float)X + SZ + SX;
        float f7 = (float)X + SZ + SX + SX;
        float f8 = (float)X + SZ + SX + SZ;
        float f9 = (float)X + SZ + SX + SZ + SX;
        float f10 = (float)Y;
        float f11 = (float)Y + SZ;
        float f12 = (float)Y + SZ + SY;
        this.quads[2] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, f5, f10, f6, f11, renderer.textureWidth, renderer.textureHeight, renderer.mirror, Direction.DOWN);
        this.quads[3] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, f6, f11, f7, f10, renderer.textureWidth, renderer.textureHeight, renderer.mirror, Direction.UP);
        this.quads[1] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, f4, f11, f5, f12, renderer.textureWidth, renderer.textureHeight, renderer.mirror, Direction.WEST);
        this.quads[4] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, f5, f11, f6, f12, renderer.textureWidth, renderer.textureHeight, renderer.mirror, Direction.NORTH);
        this.quads[0] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, f6, f11, f8, f12, renderer.textureWidth, renderer.textureHeight, renderer.mirror, Direction.EAST);
        this.quads[5] = new TexturedQuadMesh(new PositionTextureVertexMesh[]{positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, f8, f11, f9, f12, renderer.textureWidth, renderer.textureHeight, renderer.mirror, Direction.SOUTH);
    }
       
    public void MeshGen(PartModelRenderer renderer, String mesh, float X, float Y, float Z, float SX, float SY, float SZ, float scale, int texU, int texV)
    {
    	if (scale == 0) scale = 1;
    	String meshdata = Main.OtherSaveData.getMeshData(mesh);
    	if (meshdata == "" || meshdata == " ") return;
    	String[] s = meshdata.split("#");
    	String[] sv = s[0].split(";");
    	String[] sf = s[1].split(";");
    	String[] svt = s[2].split(";");
    	String[] sn = s[3].split(";");
    	
    	vertices.clear();
    	faces.clear();
    	vertText.clear();
    	vertNor.clear();
    	
    	for (int i = 0; i < sv.length; i++) {
    		MapVec3 vec = new MapVec3(sv[i].split(" ")[1], sv[i].split(" ")[2], sv[i].split(" ")[3]).minus(scale, scale, scale).add(X, Y, Z).times(SX, SY, SZ);
    		vertices.add(vec);
    	}
    	for (int i = 0; i < sn.length; i++) {
    		MapVec3 vec = new MapVec3(sn[i].split(" ")[1], sn[i].split(" ")[2], sn[i].split(" ")[3]);
    		vertNor.add(vec);
    	}
    	for (int i = 0; i < sf.length; i++) {
    		MeshFace face = new MeshFace();
    		for (int v = 1; v < sf[i].split(" ").length; v++) {
    			face.addVertex(sf[i].split(" ")[v].split("/")[0]);
    			face.addVertexTexture(sf[i].split(" ")[v].split("/")[1]);
    			face.addVertexNormal(sf[i].split(" ")[v].split("/")[2]);
    		}
    		faces.add(face);
    	}
    	for (int i = 0; i < svt.length; i++) {
    		MapVec2 uv = new MapVec2(svt[i].split(" ")[1], svt[i].split(" ")[2]);
    		vertText.add( new MapVec2(0, 1).minus(uv.X, uv.Y).times(-1, 1).times(64f, 32f).times(scale, scale).add(texU, texV) );
    	}
    	
    	this.vertexPositions = new PositionTextureVertexMesh[vertices.size()];
    	
    	for (int i = 0; i < vertices.size(); i++) {
    		this.vertexPositions[i] = new PositionTextureVertexMesh(vertices.get(i).toVec(), vertText.get(i).X, vertText.get(i).Y);
    	}
    	this.quads = new TexturedQuadMesh[faces.size()];
    	for (int i = 0; i < faces.size(); i++) {
    		PositionTextureVertexMesh[] facevertices = new PositionTextureVertexMesh[faces.get(i).vertices.size()];
    		for (int x = 0; x < faces.get(i).vertices.size(); x++) {
    			if (this.vertexPositions.length - 1 >= faces.get(i).vertices.get(x) - 1) {
    				facevertices[x] = this.vertexPositions[faces.get(i).vertices.get(x) - 1];
    			}
    		}
    		MapVec2[] faceVTs = new MapVec2[faces.get(i).vertexTexture.size()];
    		for (int x = 0; x < faces.get(i).vertexTexture.size(); x++) {
    			faceVTs[x] = this.vertText.get(faces.get(i).vertexTexture.get(x) - 1);
    		}
    		MapVec3[] faceVNs = new MapVec3[faces.get(i).vertexNormal.size()];
    		for (int x = 0; x < faces.get(i).vertexNormal.size(); x++) {
    			faceVNs[x] = this.vertNor.get(faces.get(i).vertexNormal.get(x) - 1);
    		}
    		this.quads[i] = new TexturedQuadMesh(facevertices, faceVTs, faceVNs, renderer.textureWidth, renderer.textureHeight, renderer.mirror);
    	}
    }
    
    /*public ModelMesh(ModelRenderer renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror)
    {
        this.posX1 = x;
        this.posY1 = y;
        this.posZ1 = z;
        this.posX2 = x + (float)dx;
        this.posY2 = y + (float)dy;
        this.posZ2 = z + (float)dz;
        this.vertexPositions = new PositionTextureVertexMesh[8];
        this.quadList = new TexturedQuad[6];
        float f = x + (float)dx;
        float f1 = y + (float)dy;
        float f2 = z + (float)dz;
        x = x - delta;
        y = y - delta;
        z = z - delta;
        f = f + delta;
        f1 = f1 + delta;
        f2 = f2 + delta;

        if (mirror)
        {
            float f3 = f;
            f = x;
            x = f3;
        }

        PositionTextureVertexMesh PositionTextureVertexMesh7 = new PositionTextureVertexMesh(x, y, z, 0.0F, 0.0F);
        PositionTextureVertexMesh PositionTextureVertexMesh = new PositionTextureVertexMesh(f, y, z, 0.0F, 8.0F);
        PositionTextureVertexMesh PositionTextureVertexMesh1 = new PositionTextureVertexMesh(f, f1, z, 8.0F, 8.0F);
        PositionTextureVertexMesh PositionTextureVertexMesh2 = new PositionTextureVertexMesh(x, f1, z, 8.0F, 0.0F);
        PositionTextureVertexMesh PositionTextureVertexMesh3 = new PositionTextureVertexMesh(x, y, f2, 0.0F, 0.0F);
        PositionTextureVertexMesh PositionTextureVertexMesh4 = new PositionTextureVertexMesh(f, y, f2, 0.0F, 8.0F);
        PositionTextureVertexMesh PositionTextureVertexMesh5 = new PositionTextureVertexMesh(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertexMesh PositionTextureVertexMesh6 = new PositionTextureVertexMesh(x, f1, f2, 8.0F, 0.0F);
        this.vertexPositions[0] = PositionTextureVertexMesh7;
        this.vertexPositions[1] = PositionTextureVertexMesh;
        this.vertexPositions[2] = PositionTextureVertexMesh1;
        this.vertexPositions[3] = PositionTextureVertexMesh2;
        this.vertexPositions[4] = PositionTextureVertexMesh3;
        this.vertexPositions[5] = PositionTextureVertexMesh4;
        this.vertexPositions[6] = PositionTextureVertexMesh5;
        this.vertexPositions[7] = PositionTextureVertexMesh6;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertexMesh[] {PositionTextureVertexMesh4, PositionTextureVertexMesh, PositionTextureVertexMesh1, PositionTextureVertexMesh5}, texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertexMesh[] {PositionTextureVertexMesh7, PositionTextureVertexMesh3, PositionTextureVertexMesh6, PositionTextureVertexMesh2}, texU, texV + dz, texU + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertexMesh[] {PositionTextureVertexMesh4, PositionTextureVertexMesh3, PositionTextureVertexMesh7, PositionTextureVertexMesh}, texU + dz, texV, texU + dz + dx, texV + dz, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertexMesh[] {PositionTextureVertexMesh1, PositionTextureVertexMesh2, PositionTextureVertexMesh6, PositionTextureVertexMesh5}, texU + dz + dx, texV + dz, texU + dz + dx + dx, texV, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertexMesh[] {PositionTextureVertexMesh, PositionTextureVertexMesh7, PositionTextureVertexMesh2, PositionTextureVertexMesh1}, texU + dz, texV + dz, texU + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertexMesh[] {PositionTextureVertexMesh3, PositionTextureVertexMesh4, PositionTextureVertexMesh5, PositionTextureVertexMesh6}, texU + dz + dx + dz, texV + dz, texU + dz + dx + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);

        if (mirror)
        {
            for (TexturedQuad texturedquad : this.quadList)
            {
                texturedquad.flipFace();
            }
        }
    }*/
}
