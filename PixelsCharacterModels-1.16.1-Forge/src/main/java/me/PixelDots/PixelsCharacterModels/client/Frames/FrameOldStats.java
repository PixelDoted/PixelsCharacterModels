package me.PixelDots.PixelsCharacterModels.client.Frames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class FrameOldStats 
{
	
	public List<String> listNames = new ArrayList<String>();
	public Map<String, FrameOldStat> list = Maps.newHashMap();
	public Map<String, MapVec3> DRP = Maps.newHashMap();
	public MapVec3 Translate = new MapVec3(0,0,0);
	public String animString = "";
	public float nextAnim = 0;
	public float ColHeight = 0;
	
	public FrameOldStats() {
		float yOffsetIn = 0;
		DRP.put("head", new MapVec3(0.0F, 0.0F + yOffsetIn, 0.0F));
		DRP.put("body", new MapVec3(0.0F, 0.0F + yOffsetIn, 0.0F));
		DRP.put("rightarm", new MapVec3(-5.0F, 2.0F + yOffsetIn, 0.0F));
		DRP.put("leftarm", new MapVec3(5.0F, 2.0F + yOffsetIn, 0.0F));
		DRP.put("rightleg", new MapVec3(-1.9F, 12.0F + yOffsetIn, 0.0F));
		DRP.put("leftleg", new MapVec3(1.9F, 12.0F + yOffsetIn, 0.0F));
	}
	
	public String AlltoString() {
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			String a = listNames.get(i);
			if (!(list.containsKey(a))) continue;
			s += String.valueOf(list.get(a).RotateAngle.X) + ",";
			s += String.valueOf(list.get(a).RotateAngle.Y) + ",";
			s += String.valueOf(list.get(a).RotateAngle.Z) + ",";
			s += String.valueOf(list.get(a).RotationPoint.X) + ",";
			s += String.valueOf(list.get(a).RotationPoint.Y) + ",";
			s += String.valueOf(list.get(a).RotationPoint.Z);
			if (i > 0 && i < list.size()) s += ";";
		}
		return s;
	}
	
}
