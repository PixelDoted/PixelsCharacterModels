package me.pixeldots.pixelscharactermodels.model.part;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import me.pixeldots.pixelscharactermodels.model.part.model.mesh.ModelPartMesh;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData.PresetPartData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class ModelPartData {
	
	public boolean Show = true;
	public String name = "";
	
	public MapVec3 scale = new MapVec3(1,1,1);
	public MapVec3 pos = new MapVec3(0,0,0);
	public MapVec3 rot = new MapVec3(0,0,0);
	
	public ModelPart Parent = null;
	public boolean useRotation = false;
	
	public boolean meshEnabled = false;
	public ObjectList<ModelPartMesh> meshes = new ObjectArrayList<ModelPartMesh>();
	public ObjectList<ModelPart.Cuboid> cubes = new ObjectArrayList<ModelPart.Cuboid>();
	
	public PlayerEntityModel<?> model = null;
	public PlayerEntity entity = null;
	
	public ModelPart copyFromPart = null;
	
	public void setCopyFromPart(ModelPart part) {
		if (copyFromPart == null) copyFromPart = part;
	}
	
	public void setPlayerData(PlayerEntityModel<?> model, PlayerEntity entity) {
		if (model == null) {
			this.model = model;
			this.entity = entity;
		}
	}
	
	public void copyData(PresetPartData data) {
		this.Show = data.Show;
		this.pos = data.pos;
		this.scale = data.scale;
		this.rot = data.rot;
	}
	
	public ModelPartData() {}
	public ModelPartData(String name) {
		this.name = name;
	}
	
}
