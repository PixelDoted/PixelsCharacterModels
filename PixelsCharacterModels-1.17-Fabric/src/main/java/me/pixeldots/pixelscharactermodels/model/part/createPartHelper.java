package me.pixeldots.pixelscharactermodels.model.part;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class createPartHelper {

	public static void createMesh(String meshName, MapVec3 Pos, MapVec3 Size, MapVec2 textureSize, PlayerEntityModel<?> model, PlayerEntity entity, ModelPart parent, String name) {
		/*String meshData = PixelsCharacterModels.OtherSaveData.getMeshData(meshName);
		String[] verts = meshData.split("#")[0].split(";");
		String[] vertexTexture = meshData.split("#")[1].split(";");
		String[] vertexNormal = meshData.split("#")[2].split(";");
		String[] faces = meshData.split("#")[3].split(";");
		
		MapVec3[] v = new MapVec3[verts.length];
		MapVecAny[] f = new MapVecAny[faces.length];
		MapVec2[] uv = new MapVec2[vertexTexture.length];
		MapVec3[] vn = new MapVec3[vertexNormal.length];
		
		for (int i = 0; i < v.length; i++) {
			String[] s = verts[i].split(" ");
			v[i] = new MapVec3(s[0], s[1], s[2]);
		}
		for (int i = 0; i < f.length; i++) {
			String[] s = faces[i].split(" ");
			f[i] = new MapVecAny(s);
		}
		for (int i = 0; i < uv.length; i++) {
			String[] s = vertexTexture[i].split(" ");
			uv[i] = new MapVec2(s[0], s[1]);
		}
		for (int i = 0; i < vn.length; i++) {
			String[] s = vertexNormal[i].split(" ");
			vn[i] = new MapVec3(s[0], s[1], s[3]);
		}
		
		ModelPartMesh mesh = new ModelPartMesh(v, f, uv, vn, Pos, Size, false, textureSize);
		PixelsCharacterModels.dataPackets.get(parent).meshes.add(mesh);*/
	}
	
	public static void createCuboid(MapVec3 Pos, MapVec3 Size, MapVec3 Pivot, MapVec2 textureSize, MapVec2 textureOffset, PlayerEntityModel<?> model, PlayerEntity entity, ModelPart parent, String name) {
		ModelPartCube cube = new ModelPartCube(Math.round(textureOffset.X), Math.round(textureOffset.Y), Pos.X+(Size.X*-.5f), Pos.Y+(Size.Y*-1f), Pos.Z+(Size.Z*-.5f), (float)Size.X, (float)Size.Y, (float)Size.Z, 0, 0, 0, false, textureSize.X, textureSize.Y);
		//ModelPart.Cuboid cube = ModelPartBuilder.create().cuboid(name, Pos.X, Pos.Y, Pos.Z, Size.X, Size.Y, Size.Z).build().get(0).createCuboid(64, 64);
		PixelsCharacterModels.dataPackets.get(parent).cubes.add(cube);
	}
}