package me.pixeldots.pixelscharactermodels.model.part;

import java.util.ArrayList;
import java.util.List;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.model.part.model.mesh.ModelPartMesh;
import me.pixeldots.pixelscharactermodels.utils.MapModelVectors;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class createPartHelper {

	public static void createMesh(String meshName, MapVec3 Pos, MapVec3 Size, MapVec2 textureOffset, PlayerEntityModel<?> model, PlayerEntity entity, ModelPart parent, String name) {
		MapModelVectors meshData = PixelsCharacterModels.saveData.getMeshData(meshName);
		if (meshData == null) return;
		meshData.meshID = meshName;
		
		for (int i = 0; i < meshData.Faces.size(); i++) {
			String[] face = meshData.Faces.get(i).split(" ");
			List<String> array = new ArrayList<String>();
			array.add(face[1]); array.add(face[2]); array.add(face[3]);
			meshData.parsedFaces.add(array);
		}
		for (int i = 0; i < meshData.VertexUVs.size(); i++) {
			MapVec2 uv = new MapVec2(meshData.VertexUVs.get(i).split(" ")[1], meshData.VertexUVs.get(i).split(" ")[2]);
			MapVec2 finalUV = new MapVec2(0, 1).minus(-uv.X, uv.Y).add(textureOffset.X, textureOffset.Y);
			meshData.parsedUVs.add(finalUV);
		}
		
		ModelPartMesh mesh = new ModelPartMesh(meshData, Pos, Size, textureOffset, name);
		PixelsCharacterModels.dataPackets.get(parent).meshes.add(mesh);
	}
	
	public static void createCuboid(MapVec3 Pos, MapVec3 Size, MapVec3 Pivot, MapVec2 textureSize, MapVec2 textureOffset, ModelPart parent, String name) {
		ModelPartCube cube = new ModelPartCube(Math.round(textureOffset.X), Math.round(textureOffset.Y), Pos.X, Pos.Y, Pos.Z, Size.X, Size.Y, Size.Z, textureSize.X, textureSize.Y, name);
		PixelsCharacterModels.dataPackets.get(parent).cubes.add(cube);
	}
}