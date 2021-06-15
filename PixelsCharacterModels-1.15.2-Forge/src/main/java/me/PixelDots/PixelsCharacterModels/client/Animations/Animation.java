package me.PixelDots.PixelsCharacterModels.client.Animations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import me.PixelDots.PixelsCharacterModels.util.Maps.MapBool;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class Animation 
{

	public String id;
	public MapVec3 Translate = new MapVec3(0,0,0);
	public Map<String, MapVec3> AnglePoses = Maps.newHashMap();
	public Map<String, MapVec3> AngleRotPoint = Maps.newHashMap();
	public Map<String, MapBool> AngleLimb = Maps.newHashMap();
	public List<String> AngleNames = new ArrayList<String>();
	public List<String> AngleFakes = new ArrayList<String>();
	public float ColHeight = 0F;
	public boolean pre = false;
	public boolean stoponwalk = true;
	
	public Animation() {
	}
	
	public void setAngle(String id, MapVec3 vec, MapBool bool) {
		if (!(AnglePoses.containsKey(id))) {
			AnglePoses.put(id, vec);
			AngleLimb.put(id, bool);
			AngleNames.add(id);
			AngleRotPoint.put(id, new MapVec3(0,0,0));
		}
		else {
			AnglePoses.get(id).copy(vec);
			AngleLimb.get(id).v = bool.v;
		}
	}
	
	public void setAngle(String id, MapBool bool, MapVec3 rot) {
		if (!(AnglePoses.containsKey(id))) {
			AnglePoses.put(id, new MapVec3(0,0,0));
			AngleLimb.put(id, bool);
			AngleNames.add(id);
			AngleRotPoint.put(id, rot);
		}
		else {
			AngleRotPoint.get(id).copy(rot);
			AngleLimb.get(id).v = bool.v;
		}
	}
	
	public void setAngleFake(String id, MapVec3 vec, MapBool bool) {
		if (!(AnglePoses.containsKey(id))) {
			AnglePoses.put(id, vec);
			AngleLimb.put(id, bool);
			AngleNames.add(id);
			AngleRotPoint.put(id, new MapVec3(0,0,0));
			AngleFakes.add(id);
		}
		else {
			AnglePoses.get(id).copy(vec);
			AngleLimb.get(id).v = bool.v;
		}
	}
	
	public void setAngleFake(String id, MapBool bool, MapVec3 rot) {
		if (!(AnglePoses.containsKey(id))) {
			AnglePoses.put(id, new MapVec3(0,0,0));
			AngleLimb.put(id, bool);
			AngleNames.add(id);
			AngleRotPoint.put(id, rot);
			AngleFakes.add(id);
		}
		else {
			AngleRotPoint.get(id).copy(rot);
			AngleLimb.get(id).v = bool.v;
		}
	}
	
	public Animation removeAngle(String id) {
		if (AngleFakes.contains(id)) AngleFakes.remove(id);
		AnglePoses.remove(id);
		AngleLimb.remove(id);
		AngleNames.remove(id);
		AngleRotPoint.remove(id);
		return this;
	}
	
	public String toString(int ID) {
		String s = "";
		if (ID >= AngleNames.size()) return "";
		String a = AngleNames.get(ID);
		s += String.valueOf(AnglePoses.get(a).X) + ",";
		s += String.valueOf(AnglePoses.get(a).Y) + ",";
		s += String.valueOf(AnglePoses.get(a).Z) + ",";
		s += String.valueOf(AngleLimb.get(a).v) + ",";
		s += String.valueOf(AngleRotPoint.get(a).X) + ",";
		s += String.valueOf(AngleRotPoint.get(a).Y) + ",";
		s += String.valueOf(AngleRotPoint.get(a).Z);
		return s;
	}
	
	public Animation copy(Animation anim) {
		this.id = anim.id;
		this.Translate = anim.Translate;
		/*this.AnglePoses = anim.AnglePoses;
		this.AngleRotPoint = anim.AngleRotPoint;
		this.AngleLimb = anim.AngleLimb;
		this.AngleNames = anim.AngleNames;
		this.AngleFakes = anim.AngleFakes;*/
		copyAngles(anim);
		this.ColHeight = anim.ColHeight;
		this.pre = anim.pre;
		this.stoponwalk = anim.stoponwalk;
		return this;
	}
	
	public Animation copyAngles(Animation anim) {
		for (int i = 0; i < anim.AnglePoses.size(); i++) {
			String id = anim.AngleNames.get(i);
			this.AnglePoses.put(id, anim.AnglePoses.get(id));
		}
		for (int i = 0; i < anim.AngleRotPoint.size(); i++) {
			String id = anim.AngleNames.get(i);
			this.AngleRotPoint.put(id, anim.AngleRotPoint.get(id));
		}
		for (int i = 0; i < anim.AngleLimb.size(); i++) {
			String id = anim.AngleNames.get(i);
			this.AngleLimb.put(id, anim.AngleLimb.get(id));
		}
		for (int i = 0; i < anim.AngleNames.size(); i++) {
			String id = anim.AngleNames.get(i);
			this.AngleNames.add(id);
		}
		for (int i = 0; i < anim.AngleFakes.size(); i++) {
			this.AngleFakes.add(anim.AngleFakes.get(i));
		}
		return this;
	}
	
	public Animation removeFakes() {
		for (int i = AngleFakes.size(); i > 0; i--) {
			removeAngle(AngleFakes.get(i));
		}
		return this;
	}
	
}
