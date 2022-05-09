package me.pixeldots.pixelscharactermodels.model;

import com.google.gson.Gson;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
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
	public String name = "";
	
	public MapVec3 scale = new MapVec3(1,1,1);
	public MapVec3 pos = new MapVec3();
	public MapVec3 rot = new MapVec3();
	
	public ModelPart Parent = null;
	public boolean useRotation = false;
	
	public boolean meshEnabled = false;
	public ObjectList<ModelPartMesh> meshes = new ObjectArrayList<ModelPartMesh>();
	public ObjectList<ModelPartCube> cubes = new ObjectArrayList<ModelPartCube>();
	
	public PlayerEntityModel<?> model = null;
	public PlayerEntity entity = null;
	
	public ModelPart copyFromPart = null;
	//Other
	public boolean activeRotation = false;
	
	public void setCopyFromPart(ModelPart part) {
		copyFromPart = part;
	}
	
	public void setPlayerData(PlayerEntityModel<?> model, PlayerEntity entity) {
		if (model == null) {
			this.model = model;
			this.entity = entity;
		}
	}
	
	public void copyData(PresetPartData data, ModelPart part) {
		this.Show = data.Show;
		this.pos = data.pos;
		this.scale = data.scale;
		this.rot = data.rot;
		
		this.cubes.clear();
		this.meshes.clear();
		
		if (data.cubes != null) {
			for (PresetCubeData cube : data.cubes) {
				createPartHelper.createCuboid(cube.pos, cube.size, new MapVec3(), new MapVec2(64, 64), cube.uvOffset, part, cube.name, cube.textureID);
			}
		}
		if (data.meshes != null) {
			for (PresetMeshData mesh : data.meshes) {
				createPartHelper.createMesh(mesh.meshID, mesh.pos, mesh.size, new MapVec2(64, 64), model, entity, part, mesh.name, mesh.textureID);
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
	
	public ModelPartData() {}
	public ModelPartData(String name) {
		this.name = name;
	}
	
}
