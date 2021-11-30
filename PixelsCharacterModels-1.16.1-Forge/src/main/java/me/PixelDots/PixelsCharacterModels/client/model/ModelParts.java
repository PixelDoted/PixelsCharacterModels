package me.PixelDots.PixelsCharacterModels.client.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class ModelParts 
{
	
	public List<ModelPart> Limb = null;
	public List<ModelPart> Part = null;
	public int PartsCount = 0;
	
	public ModelParts() {
		Limb = new ArrayList<ModelPart>();
		Part = new ArrayList<ModelPart>();
		PartsCount = 0;
		Limb.add(new ModelPart(1, "Head"));
		Limb.add(new ModelPart(1, "Body"));
		Limb.add(new ModelPart(1, "RightArm"));
		Limb.add(new ModelPart(1, "LeftArm"));
		Limb.add(new ModelPart(1, "RightLeg"));
		Limb.add(new ModelPart(1, "LeftLeg"));
		Limb.add(new ModelPart(1, "ItemRight"));
		Limb.add(new ModelPart(1, "ItemLeft"));
	}
	
	public void removePart(int ID) {
		if (Part.size() - 1 >= ID) { Part.remove(ID); }
	}
	
	//Server
	public String LimbstoString() {
		String s = "";
		for (int i = 0; i < Limb.size(); i++) {
			String a = "";
			//if (i != 0) a += ",";
			a += Limb.get(i).toString();
			//a += "]";
			if (i != Limb.size() - 1) a += ";";
			s += a;
		}
		return s;
	}
	
	public String PartstoString() {
		String s = "";
		for (int i = 0; i < Part.size(); i++) {
			String a = "";
			//if (i != 0) a += ",";
			a += Part.get(i).toString();
			//a += "]";
			if (i != Part.size() - 1) a += ";";
			s += a;
		}
		return s;
	}
	
	public void StringtoLimbs(String s) {
		String[] a = s.split(";");
		for (int i = 0; i < Limb.size(); i++) {
			String[] b = a[i].split(",");
			Limb.get(i).fromString(b);
		}
	}
	
	public void StringtoParts(String s) {
		if (s == "") return;
		String[] a = s.split(";");
		for (int i = 0; i < a.length; i++) {
			String[] b = a[i].split(",");
			if (Part.size() - 1 < i) Part.add(new ModelPart());
			Part.get(i).fromString(b);
		}
	}
	//Server
	
	public int getLimbIDfromString(String name) {
		for (int i = 0; i < Limb.size(); i++) {
			if (Limb.get(i).name.equalsIgnoreCase(name.toLowerCase())) {
				return i;
			}
		}
		return 0;
	}

	public ModelPart getLimbfromName(String string) {
		return Limb.get(getLimbIDfromString(string));
	}
	
	public boolean ParthasID(String name) {
		for (int i = 0; i < Part.size(); i++) {
			if (Part.get(i).name.equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	
}
