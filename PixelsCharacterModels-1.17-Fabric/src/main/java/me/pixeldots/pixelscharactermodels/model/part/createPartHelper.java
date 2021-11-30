package me.pixeldots.pixelscharactermodels.model.part;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.hash.Hashing;

import org.apache.commons.lang3.RandomStringUtils;

import lain.mods.skins.impl.Shared;
import lain.mods.skins.impl.fabric.ImageUtils;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.model.part.model.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.model.part.model.mesh.ModelPartMesh;
import me.pixeldots.pixelscharactermodels.utils.CustomPartTexture;
import me.pixeldots.pixelscharactermodels.utils.MapModelVectors;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class createPartHelper {

	public static void createMesh(String meshName, MapVec3 Pos, MapVec3 Size, MapVec2 textureOffset, PlayerEntityModel<?> model, PlayerEntity entity, ModelPart parent, String name, String textureID) {
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
		mesh.texture = loadTexture(textureID);
		mesh.textureFile = textureID;
		
		PixelsCharacterModels.dataPackets.get(parent).meshes.add(mesh);
	}
	
	public static void createCuboid(MapVec3 Pos, MapVec3 Size, MapVec3 Pivot, MapVec2 textureSize, MapVec2 textureOffset, ModelPart parent, String name, String textureID) {
		ModelPartCube cube = new ModelPartCube(Math.round(textureOffset.X), Math.round(textureOffset.Y), Pos.X, Pos.Y, Pos.Z, Size.X, Size.Y, Size.Z, textureSize.X, textureSize.Y, name);
		cube.texture = loadTexture(textureID);
		cube.textureFile = textureID;

		PixelsCharacterModels.dataPackets.get(parent).cubes.add(cube);
	}

	public static Identifier loadTexture(String filename) {
		if (filename == null || filename.equals("")) return null;
		if (!filename.contains(".")) filename = filename + ".png";
		try {
			File file = new File(MinecraftClient.getInstance().runDirectory.getAbsolutePath() + File.separator + "PCM" + File.separator + "Textures" + File.separator + filename);
			if (!file.exists()) return null;

			byte[] data = Shared.blockyReadFile(file, null, null);
			if (data == null && !ImageUtils.validateData(data)) return null;
			
			ByteBuffer buf = ByteBuffer.allocateDirect(data.length).order(ByteOrder.nativeOrder());
			buf.put(data);
			buf.position(0);
			CustomPartTexture texture = new CustomPartTexture(generateRandomLocation(), buf);
			
			MinecraftClient.getInstance().getTextureManager().registerTexture(texture.getLocation(), texture);
			return texture.getLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Identifier generateRandomLocation() {
        return new Identifier("pcmtextures", String.format("textures/generated/%s", UUID.randomUUID().toString()));
    }
}