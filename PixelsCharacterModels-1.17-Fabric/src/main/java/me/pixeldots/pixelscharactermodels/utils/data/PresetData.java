package me.pixeldots.pixelscharactermodels.utils.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import me.pixeldots.pixelscharactermodels.main.MainClientHandler;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PresetData {

	public Map<String, PresetPartData> partData = new HashMap<String, PresetPartData>();
	public float GlobalScale = 1;
	public String skinSuffix = "";
	
	public void convertModelData(PlayerEntityModel<?> model) {
		partData.put("head", new PresetPartData(PixelsCharacterModels.dataPackets.get(model.head)));
		partData.put("body", new PresetPartData(PixelsCharacterModels.dataPackets.get(model.body)));
		partData.put("leftleg", new PresetPartData(PixelsCharacterModels.dataPackets.get(model.leftLeg)));
		partData.put("rightleg", new PresetPartData(PixelsCharacterModels.dataPackets.get(model.rightLeg)));
		partData.put("leftarm", new PresetPartData(PixelsCharacterModels.dataPackets.get(model.leftArm)));
		partData.put("rightarm", new PresetPartData(PixelsCharacterModels.dataPackets.get(model.rightArm)));
	}
	
	public void convertToModel(PlayerEntity player, PlayerEntityModel<?> model, boolean isPacket) {
		if (player == null) return;
		if (isPacket == false) MainClientHandler.changePlayerScale(GlobalScale);
		
		PixelsCharacterModels.client.setSkinSuffix(player.getGameProfile().getId(), skinSuffix);
		PixelsCharacterModels.client.ReloadSkins();
		
		PixelsCharacterModels.dataPackets.get(model.head).copyData(partData.get("head"), model.head);
		PixelsCharacterModels.dataPackets.get(model.body).copyData(partData.get("body"), model.body);
		PixelsCharacterModels.dataPackets.get(model.leftLeg).copyData(partData.get("leftleg"), model.leftLeg);
		PixelsCharacterModels.dataPackets.get(model.rightLeg).copyData(partData.get("rightleg"), model.rightLeg);
		PixelsCharacterModels.dataPackets.get(model.leftArm).copyData(partData.get("leftarm"), model.leftArm);
		PixelsCharacterModels.dataPackets.get(model.rightArm).copyData(partData.get("rightarm"), model.rightArm);
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
			for (int i = 0; i < data.cubes.size(); i++) {
				PresetCubeData cube = new PresetCubeData();
				cube.name = data.cubes.get(i).name;
				cube.pos = data.cubes.get(i).pos;
				cube.size = data.cubes.get(i).size;
				cube.uvOffset = data.cubes.get(i).uv;

				if (data.cubes.get(i).textureFile != null && !data.cubes.get(i).textureFile.equals(""))
					cube.textureID = data.cubes.get(i).textureFile;

				cubes.add(cube);
			}
			for (int i = 0; i < data.meshes.size(); i++) {
				PresetMeshData mesh = new PresetMeshData();
				mesh.name = data.meshes.get(i).name;
				mesh.pos = data.meshes.get(i).pos;
				mesh.size = data.meshes.get(i).size;
				mesh.uvOffset = data.meshes.get(i).uv;
				mesh.meshID = data.meshes.get(i).meshID;

				if (data.meshes.get(i).textureFile != null && !data.meshes.get(i).textureFile.equals(""))
					mesh.textureID = data.meshes.get(i).textureFile;

				meshes.add(mesh);
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
