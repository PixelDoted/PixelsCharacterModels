package me.pixeldots.pixelscharactermodels.utils.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import me.pixeldots.pixelscharactermodels.main.MainClientHandler;
import me.pixeldots.pixelscharactermodels.model.ModelPartData;
import me.pixeldots.pixelscharactermodels.model.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.model.mesh.ModelPartMesh;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PresetData {

	public Map<String, PresetPartData> partData = new HashMap<String, PresetPartData>();
	public float GlobalScale = 1;
	public String skinSuffix = "";
	
	public void convertModelData(PlayerEntity player, PlayerEntityModel<?> model) {
		PlayerData data = PixelsCharacterModels.PlayerDataList.get(player.getUuid());
		partData.put("head", new PresetPartData(data.limbs.get(model.head)));
		partData.put("body", new PresetPartData(data.limbs.get(model.body)));
		partData.put("leftleg", new PresetPartData(data.limbs.get(model.leftLeg)));
		partData.put("rightleg", new PresetPartData(data.limbs.get(model.rightLeg)));
		partData.put("leftarm", new PresetPartData(data.limbs.get(model.leftArm)));
		partData.put("rightarm", new PresetPartData(data.limbs.get(model.rightArm)));
	}
	
	public void convertToModel(PlayerEntity player, PlayerEntityModel<?> model, boolean isPacket) {
		if (player == null) return;
		if (isPacket == false) MainClientHandler.changePlayerScale(GlobalScale);
		PlayerData data = PixelsCharacterModels.PlayerDataList.get(player.getUuid());
		
		PixelsCharacterModels.client.setSkinSuffix(player.getGameProfile().getId(), skinSuffix);
		PixelsCharacterModels.client.ReloadSkins();
		
		data.getLimb(model.head, player, model).copyData(partData.get("head"), model.head);
		data.getLimb(model.body, player, model).copyData(partData.get("body"), model.body);
		data.getLimb(model.leftLeg, player, model).copyData(partData.get("leftleg"), model.leftLeg);
		data.getLimb(model.rightLeg, player, model).copyData(partData.get("rightleg"), model.rightLeg);
		data.getLimb(model.leftArm, player, model).copyData(partData.get("leftarm"), model.leftArm);
		data.getLimb(model.rightArm, player, model).copyData(partData.get("rightarm"), model.rightArm);
	}
	
	public class PresetPartData {
		public boolean Show = true;
		public MapVec3 scale = new MapVec3(1,1,1);
		public MapVec3 pos = new MapVec3();
		public MapVec3 rot = new MapVec3();
		public List<PresetCubeData> cubes = new ArrayList<PresetCubeData>();
		public List<PresetMeshData> meshes = new ArrayList<PresetMeshData>();
		
		public PresetPartData(ModelPartData data) {
			this.Show = data.Show;
			this.scale = data.scale;
			this.pos = data.pos;
			this.rot = data.rot;
			for (ModelPartCube cube : data.cubes) {
				PresetCubeData preset_cube = new PresetCubeData();
				preset_cube.name = cube.name;
				preset_cube.pos = cube.pos;
				preset_cube.size = cube.size;
				preset_cube.uvOffset = cube.uv;

				if (cube.textureFile != null && !cube.textureFile.equals(""))
					preset_cube.textureID = cube.textureFile;

				cubes.add(preset_cube);
			}
			for (ModelPartMesh mesh : data.meshes) {
				PresetMeshData preset_mesh = new PresetMeshData();
				preset_mesh.name = mesh.name;
				preset_mesh.pos = mesh.pos;
				preset_mesh.size = mesh.size;
				preset_mesh.uvOffset = mesh.uv;
				preset_mesh.meshID = mesh.meshID;

				if (mesh.textureFile != null && !mesh.textureFile.equals(""))
					preset_mesh.textureID = mesh.textureFile;

				meshes.add(preset_mesh);
			}
		}
	}
	
	public class PresetCubeData {
		public String name;
		public MapVec3 pos = new MapVec3();
		public MapVec3 size = new MapVec3();
		public MapVec2 uvOffset = new MapVec2();
		public String textureID;
	}
	
	public class PresetMeshData {
		public String name;
		public MapVec3 pos = new MapVec3();
		public MapVec3 size = new MapVec3();
		public MapVec2 uvOffset = new MapVec2();
		public String meshID;
		public String textureID;
	}
	
}
