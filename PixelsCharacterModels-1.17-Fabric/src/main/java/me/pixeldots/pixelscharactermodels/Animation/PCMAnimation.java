package me.pixeldots.pixelscharactermodels.Animation;

import java.util.HashMap;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.model.ModelPart;

public class PCMAnimation {
	
	public String name = "";
	
	public MapVec3 playerTransform = new MapVec3(0,0,0);
	public Map<ModelPart, MapVec3> LimbRotations = new HashMap<ModelPart, MapVec3>();
	public Map<ModelPart, MapVec3> LimbPivots = new HashMap<ModelPart, MapVec3>();
	
}
