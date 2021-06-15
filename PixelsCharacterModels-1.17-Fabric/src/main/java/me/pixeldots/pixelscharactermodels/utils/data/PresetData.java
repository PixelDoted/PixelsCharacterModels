package me.pixeldots.pixelscharactermodels.utils.data;

import java.util.HashMap;
import java.util.Map;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import me.pixeldots.pixelscharactermodels.utils.MapVec3;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

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
	
	public void convertToModel(PlayerEntity player, PlayerEntityModel<?> model) {
		ScaleData data = ScaleType.BASE.getScaleData(player);
		data.setTargetScale(GlobalScale);
		FabricOfflineSkins.skinSuffix = skinSuffix;
		FabricOfflineSkins.ReloadSkins(PixelsCharacterModels.client);
		
		PixelsCharacterModels.dataPackets.get(model.head).copyData(partData.get("head"));
		PixelsCharacterModels.dataPackets.get(model.body).copyData(partData.get("body"));
		PixelsCharacterModels.dataPackets.get(model.leftLeg).copyData(partData.get("leftleg"));
		PixelsCharacterModels.dataPackets.get(model.rightLeg).copyData(partData.get("rightleg"));
		PixelsCharacterModels.dataPackets.get(model.leftArm).copyData(partData.get("leftarm"));
		PixelsCharacterModels.dataPackets.get(model.rightArm).copyData(partData.get("rightarm"));
	}
	
	public class PresetPartData {
		public boolean Show = true;
		public MapVec3 scale = new MapVec3(1,1,1);
		public MapVec3 pos = new MapVec3(0,0,0);
		public MapVec3 rot = new MapVec3(0,0,0);
		
		public PresetPartData(ModelPartData data) {
			this.Show = data.Show;
			this.scale = data.scale;
			this.pos = data.pos;
			this.rot = data.rot;
		}
	}
	
}
