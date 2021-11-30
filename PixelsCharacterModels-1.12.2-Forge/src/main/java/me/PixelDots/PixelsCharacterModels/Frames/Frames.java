package me.PixelDots.PixelsCharacterModels.Frames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jline.utils.Log;

import com.google.common.collect.Maps;

import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class Frames 
{
	
	public String id = "";
	public List<String> animNames = new ArrayList<String>();
	public Map<String, Animation> anims = Maps.newHashMap();
	
	public 	float time = 0;
	public boolean Linear = false;
	public boolean Loop = false;
	public boolean resetnextframe = true;
	
	public void fromString(String s) {
		try {
			String[] a = s.split(":");
			String[] b;
			if (a[0].contains(",")) b = a[0].split(",");
			else b = new String[] {a[0]};
			String[] c = a[1].split(",");
			for (int i = 0; i < b.length; i++) {
				animNames.add(b[i].toLowerCase());
			}
			time = Float.valueOf(c[0]);
			Linear = Boolean.valueOf(c[1]);
			Loop = Boolean.valueOf(c[2]);
			resetnextframe = Boolean.valueOf(c[3]);
		} catch (Exception e) {
			Log.info("Frames invalid file:");
			Log.info(s);
			Log.info(":Frames invalid file");
		}
	}
	
	public String toString() {
		String s = "";
		for (int i = 0; i < anims.size(); i++) {
			if (i > 0 && i < anims.size()) s += ",";
			s += animNames.get(i);
		}
		s += ":";
		s += time;
		s += "," + Linear;
		s += "," + Loop;
		s += "," + resetnextframe;
		return s;
	}
	
}
