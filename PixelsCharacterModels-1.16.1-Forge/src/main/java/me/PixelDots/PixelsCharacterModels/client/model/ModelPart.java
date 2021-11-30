package me.PixelDots.PixelsCharacterModels.client.model;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.CreatePart;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraft.nbt.CompoundNBT;

public class ModelPart 
{
	
	public String name;
	public String type = "cube";
	
	public float Scale = 1;
	
	public float X = 0;
	public float Y = 0;
	public float Z = 0;
	
	public float RotX = 0;
	public float RotY = 0;
	public float RotZ = 0;
	
	public MatrixStack matrix = null;
	
	public PartModelRenderer Renderer = null;
	public PartModelRenderer WearRenderer = null;
	public net.minecraft.client.renderer.model.ModelRenderer ModelRenderer = null;
	public boolean Show = true;
	
	public int TextureOffsetX = 0, TextureOffsetY = 0;
	public int TextureSizeX = 0, TextureSizeY = 0;
	
	public boolean looking = false;
	public boolean swinging = false;
	
	public int PreScaleX = 1, PreScaleY = 1, PreScaleZ = 1;
	public float PreX = 0, PreY = 0, PreZ = 0;
	public float RotPointX = 0, RotPointY = 0, RotPointZ = 0;
	public int PreU = 0, PreV = 0;
	
	public int ParentID = -1;
	public boolean ParentisLimb = false;
	public boolean PrePart = false;
	
	public ModelPart() {
		
	}
	
	public ModelPart(int Scale, String name) {
		this.Scale = Scale;
		this.name = name;
	}
	
	public ModelPart(CreatePart create) {
		this(1,create.name);
		setCreate(create);
	}
	
	public void setCreate(CreatePart create) {
		PrePart = true;
		type = create.mesh;
		PreScaleX = create.PreScaleX;
		PreScaleY = create.PreScaleY;
		PreScaleZ = create.PreScaleZ;
		PreX = create.PreX;
		PreY = create.PreY;
		PreZ = create.PreZ;
		RotPointX = create.RotPointX;
		RotPointY = create.RotPointY;
		RotPointZ = create.RotPointZ;
		PreU = create.TextureU;
		PreV = create.TextureV;
		ParentID = create.parentID;
		ParentisLimb = create.parentisLimb;
	}
	
	public ModelPart(MapVec3 pos, int Scale, String name) {
		this(Scale,name);
		X = pos.X / 100;
		Y = pos.Y / 100;
		Z = pos.Z / 100;
	}

	public String toString() {
		//0:name,1:type,2:scale,3:XYZ,4:Show
		String s = "";
		s += name + ":";
		s += (type == "" ? "cube" : type) + ",";
		s += Scale + ",";
		s += X + "," + Y + "," + Z + ",";
		s += Show + ",";
		s += PrePart;
		if (PrePart) {
			s += "," + PreScaleX + "," + PreScaleY + "," + PreScaleZ + ",";
			s += PreX + "," + PreY + "," + PreZ + ",";
			s += RotPointX + "," + RotPointY + "," + RotPointZ + ",";
			s += PreU + "," + PreV + ",";
			s += ParentID + "," + ParentisLimb;
		}
		return s;
	}
	
	public void fromString(String[] s) {
		List<String> a = new ArrayList<String>();
		for (int i = 0; i < s.length; i++) {
			if (s[i].contains(":")) {
				a.add(s[i].split(":")[0]);
				a.add(s[i].split(":")[1]);
			}else {
				a.add(s[i]);
			}
		}
		
		if (a.size() < 8) return;
		name = a.get(0);
		type = a.get(1);
		Scale = Float.parseFloat(a.get(2));
		X = Float.parseFloat(a.get(3));
		Y = Float.parseFloat(a.get(4));
		Z = Float.parseFloat(a.get(5));
		Show = Boolean.parseBoolean(a.get(6));
		PrePart = Boolean.parseBoolean(a.get(7));
		if (PrePart) {
			if (a.size() < 21) return;
			PreScaleX = Integer.parseInt(a.get(8));
			PreScaleY = Integer.parseInt(a.get(9));
			PreScaleZ = Integer.parseInt(a.get(10));
			PreX = Float.parseFloat(a.get(11));
			PreY = Float.parseFloat(a.get(12));
			PreZ = Float.parseFloat(a.get(13));
			RotPointX = Float.parseFloat(a.get(14));
			RotPointY = Float.parseFloat(a.get(15));
			RotPointZ = Float.parseFloat(a.get(16));
			PreU = Integer.valueOf(a.get(17));
			PreV = Integer.valueOf(a.get(18));
			ParentID = Integer.valueOf(a.get(19));
			ParentisLimb = Boolean.valueOf(a.get(20));
		}
	}
	
	public CompoundNBT toNBT() {
		final CompoundNBT tag = new CompoundNBT();
		tag.putFloat(name + "-x", this.X);
		tag.putFloat(name + "-y", this.Y);
		tag.putFloat(name + "-z", this.Z);
		tag.putFloat(name + "-scale", this.Scale);
		tag.putBoolean(name + "-show", this.Show);
		tag.putBoolean(name + "-looking", this.looking);
		tag.putBoolean(name + "-swing", this.swinging);
		tag.putString(name + "-name", this.name);
		tag.putString(name + "-type", this.type);
		tag.putBoolean(name + "-prepart", this.PrePart);
		if (PrePart) {
			tag.putInt(name + "-prescalex", this.PreScaleX);
			tag.putInt(name + "-prescaley", this.PreScaleY);
			tag.putInt(name + "-prescalez", this.PreScaleZ);
			tag.putFloat(name + "-prex", this.PreX);
			tag.putFloat(name + "-prey", this.PreY);
			tag.putFloat(name + "-prez", this.PreZ);
			tag.putFloat(name + "-rotpointx", this.RotPointX);
			tag.putFloat(name + "-rotpointy", this.RotPointY);
			tag.putFloat(name + "-rotpointz", this.RotPointZ);
		}
		return tag;
	}
	
	public void fromNBT(CompoundNBT tag) {
		this.X = tag.getFloat(name + "-x");
		this.Y = tag.getFloat(name + "-y");
		this.Z = tag.getFloat(name + "-z");
		this.Scale = tag.getFloat(name + "-scale");
		this.Show = tag.getBoolean(name + "-show");
		this.looking = tag.getBoolean(name + "-looking");
		this.swinging = tag.getBoolean(name + "-swing");
		this.name = tag.getString(name + "-name");
		this.type = tag.getString(name + "-type");
		this.PrePart = tag.getBoolean(name + "-prepart");
		if (this.PrePart) {
			this.PreScaleX = tag.getInt(name + "-prescalex");
			this.PreScaleY = tag.getInt(name + "-prescaley");
			this.PreScaleZ = tag.getInt(name + "-prescalez");
			this.PreX = tag.getFloat(name + "-prex");
			this.PreY = tag.getFloat(name + "-prey");
			this.PreZ = tag.getFloat(name + "-prez");
			this.RotPointX = tag.getFloat(name + "-rotpointx");
			this.RotPointY = tag.getFloat(name + "-rotpointy");
			this.RotPointZ = tag.getFloat(name + "-rotpointz");
		}
	}
	
	public void cloneDataTo(ModelPart data) {
		this.looking = data.looking;
		this.RotX = data.RotX;
		this.RotY = data.RotY;
		this.RotZ = data.RotZ;
		this.Scale = data.Scale;
		this.Show = data.Show;
		this.swinging = data.swinging;
		this.X = data.X;
		this.Y = data.Y;
		this.Z = data.Z;
	}
	
}
