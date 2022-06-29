package me.pixeldots.pixelscharactermodels.model;

import java.util.UUID;

import com.google.gson.Gson;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import me.pixeldots.pixelscharactermodels.model.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.model.mesh.ModelPartMesh;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData.PresetCubeData;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData.PresetMeshData;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData.PresetPartData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class ModelPartData {
	
	public boolean Show = true;
	
	public MapVec3 scale = new MapVec3(1,1,1);
	public MapVec3 pos = new MapVec3();
	public MapVec3 rot = new MapVec3();
	
	public ModelPart Parent = null;
	public boolean useRotation = false;
	
	public boolean meshEnabled = false;
	public ObjectList<ModelPartMesh> meshes = new ObjectArrayList<ModelPartMesh>();
	public ObjectList<ModelPartCube> cubes = new ObjectArrayList<ModelPartCube>();
	
	public UUID player = null;
	
	public ModelPart copyFromPart = null;
	//Other
	public boolean activeRotation = false;
	
	public void setCopyFromPart(ModelPart part) {
		copyFromPart = part;
	}
	
	public void setPlayerData(UUID uuid) {
		if (player == null) {
			player = uuid;
		}
	}
	
	public void copyData(PresetPartData data, ModelPart part) {
		this.Show = data.Show;
		this.pos = data.pos;
		this.scale = data.scale;
		this.rot = data.rot;
		
		this.cubes.clear();
		this.meshes.clear();
		
		if (data.cubes != null || data.meshes != null) {
			PlayerData playerdata = PixelsCharacterModels.PlayerDataList.get(player);

			for (PresetCubeData cube : data.cubes) {
				createPartHelper.createCuboid(cube.pos, cube.size, new MapVec3(), new MapVec2(64, 64), cube.uvOffset, part, cube.name, cube.textureID, playerdata.entity);
			}
			for (PresetMeshData mesh : data.meshes) {
				createPartHelper.createMesh(mesh.meshID, mesh.pos, mesh.size, new MapVec2(64, 64), playerdata.model, playerdata.entity, part, mesh.name, mesh.textureID);
			}
		}
	}
	
	public void fromJsonString(String s) {
		ModelPartData data = new Gson().fromJson(s, ModelPartData.class);
		this.pos = data.pos;
		this.scale = data.scale;
		this.Show = data.Show;
	}
	public String toJsonString() {
		return new Gson().toJson(this);
	}

	public PlayerEntity getEntity() {
		return PixelsCharacterModels.PlayerDataList.get(player).entity;
	}
	public PlayerEntityModel<?> getModel() {
		return PixelsCharacterModels.PlayerDataList.get(player).model;
	}
	
	public ModelPartData() {}
	public ModelPartData(UUID uuid) {
		this.player = uuid;
	}
	
}
