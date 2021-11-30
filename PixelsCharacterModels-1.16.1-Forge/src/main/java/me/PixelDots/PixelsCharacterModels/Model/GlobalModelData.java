package me.PixelDots.PixelsCharacterModels.Model;

import java.util.ArrayList;
import java.util.List;

import me.PixelDots.PixelsCharacterModels.Frames.Frames;
import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;
import me.PixelDots.PixelsCharacterModels.client.Animations.playerAnimations;
import me.PixelDots.PixelsCharacterModels.client.Frames.FrameOldStats;
import me.PixelDots.PixelsCharacterModels.client.model.ModelParts;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class GlobalModelData 
{
	
	public boolean ConnectionPreset = false;
	public PlayerEntity player;
	public float GlobalScale = 1;
	
	public ModelParts parts = null;
	public boolean slimArms = false;
	
	//Data
	public Animation PlayingAnim = null;
	public String PlayingString = "";
	
	public FrameOldStats OldFrameStats = null;
	public String FrameString = "";
	public Frames Frame = null;
	
	public List<PartModelRenderer> renderParts = null;
	
	//Client Memory
	public String ClientMemorySkinTexture = "";
	public String ClientMemoryElytraTexture = "";
	
	public GlobalModelData() {
		ConnectionPreset = false;
		player = null;
		GlobalScale = 1;
		parts = new ModelParts();
		PlayingAnim = null;
		PlayingString = "";
		OldFrameStats = new FrameOldStats();
		FrameString = "";
		Frame = null;
		renderParts = new ArrayList<PartModelRenderer>();
		ClientMemorySkinTexture = "";
		ClientMemoryElytraTexture = "";
	}
	
	public String toString() {
		String s = "GlobalData:[scale:" + GlobalScale + "]#LimbData:[" + 
				parts.LimbstoString() + "]#PartData:[" + parts.PartstoString() + "]" + ( PlayingAnim != null ?
				"#AnimData:[" + PlayingAnim.id + "/" + PlayingString + "]" + (Frame != null ?
						"#FrameData:[" + Frame.id + "/" + FrameString + "]" : "") : "");
		return s;
	}
	
	public void removePart(int ID) {
		if (renderParts.size() - 1 >= ID) { renderParts.remove(ID); }
		parts.removePart(ID);
	}
	
	public void setAnimation(Animation a, String s) {
		Animation anim = new Animation();
		if (a != null) {
			a.removeFakes();
			anim.copy(a);
		}
		else anim = null;
		PlayingAnim = anim;
		PlayingString = s;
		this.Frame = null;
		this.FrameString = "";
	}
	
	public void setAnimation(Animation a, String s, String FrameS, Frames Frame) {
		Animation anim = new Animation();
		if (a != null) anim.copy(a);
		else anim = null;
		PlayingAnim = anim;
		PlayingString = s;
		this.FrameString = FrameS;
		this.Frame = Frame;
	}
	
	public void fromString(String s) {
		String[] a = s.split("#");
		if (s == "" || a.length < 3) return;
		a[0] = a[0].replace("GlobalData:[", ""); a[0] = a[0].replace("]", "");
		a[1] = a[1].replace("LimbData:[", ""); a[1] = a[1].replace("]", "");
		a[2] = a[2].replace("PartData:[", ""); a[2] = a[2].replace("]", "");
		if (a.length == 4) {
			a[3] = a[3].replace("AnimData:[", ""); a[3] = a[3].replace("]", "");
			String[] b = a[3].split("/");
			this.PlayingAnim = new playerAnimations().REfromString(b[1], b[2]);
		} else {
			this.PlayingAnim = null;
			this.PlayingString = "";
		}
		if (a.length == 5) {
			a[4] = a[4].replace("FrameData:[", ""); a[4] = a[4].replace("]", "");
			Frames frames = new Frames();
			frames.fromString(a[4]);
			this.Frame = frames;
		} else {
			this.Frame = null;
			this.FrameString = "";
		}
		a[1] = ReplaceString(a[1]);
		a[2] = ReplaceString(a[2]);
		parts.StringtoLimbs(a[1]);
		parts.StringtoParts(a[2]);
		
		//Main
		this.GlobalScale = Float.parseFloat(OnlyDigits(a[0].split(",")[0]));
	}
	
	public String OnlyDigits(CharSequence cs) {
		String s = "";
		for (int i = 0; i < cs.length(); i++) {
			if (Character.isDigit(cs.charAt(i)) || cs.charAt(i) == '.' || cs.charAt(i) == '-') {
				s += cs.charAt(i);
			}
		}
		return s;
	}
	
	public String ReplaceString(CharSequence cs) {
		String s = "";
		boolean change = false;
		int num = 0;
		for (int i = 0; i < cs.length(); i++) {
			if (cs.charAt(i) == '{' && change == false) {
				if (num == 0) num++;
				else change = true;
			}
			if (cs.charAt(i) == ',' && change == true) {
				s += ";";
				change = false;
				num = 0;
			}
			else s += cs.charAt(i);
		}
		return s;
	}
	
	//Preset
	public String toPreString() {
		String s = "GlobalData:[scale:" + GlobalScale + "]#LimbData:[" + 
				parts.LimbstoString() + "]#PartData:[" + parts.PartstoString() + "]#TextureData:[Skin:" 
				+ ClientMemorySkinTexture + ",Elytra:" + ClientMemoryElytraTexture + "]";
		//Log.info(s);
		return s;
	}
	
	public void fromPreString(String s) {
		String[] a = s.split("#");
		if (s == "" || a.length < 3) return;
		a[0] = a[0].replace("GlobalData:[", ""); a[0] = a[0].replace("]", "");
		a[1] = a[1].replace("LimbData:[", ""); a[1] = a[1].replace("]", "");
		a[2] = a[2].replace("PartData:[", ""); a[2] = a[2].replace("]", "");
		if (a.length >= 4) {
			a[3] = a[3].replace("TextureData:[", ""); a[3] = a[3].replace("]", "");
		}
		a[1] = ReplaceString(a[1]);
		a[2] = ReplaceString(a[2]);
		parts.StringtoLimbs(a[1]);
		parts.StringtoParts(a[2]);
		
		//Main
		String scale = a[0].contains(",") ? a[0].split(",")[0] : a[0];
		this.GlobalScale = Float.parseFloat(OnlyDigits(scale));
		if (a.length >= 4) {
			if (!(a[3].split(",")[0].endsWith("Skin:") && a[3].split(",")[1].endsWith("Elytra:"))) {
				if (a[3].split(",")[0].endsWith("Skin:")) this.ClientMemoryElytraTexture = a[3].split(",")[1].replace("Elytra:", "");
				else if (a[3].split(",")[1].endsWith("Elytra:")) this.ClientMemorySkinTexture = a[3].split(",")[0].replace("Skin:", "");
				else {
					this.ClientMemorySkinTexture = a[3].split(",")[0].replace("Skin:", "");
					this.ClientMemoryElytraTexture = a[3].split(",")[1].replace("Elytra:", "");
				}
			}
		}
	}
	//Preset
	
}
