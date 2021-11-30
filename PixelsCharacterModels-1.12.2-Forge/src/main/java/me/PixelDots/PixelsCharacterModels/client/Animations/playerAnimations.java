package me.PixelDots.PixelsCharacterModels.client.Animations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.util.Reference;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapBool;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraft.client.Minecraft;

public class playerAnimations 
{
	
	public String path = Minecraft.getInstance().gameDir + "\\PCM\\Animations";
	
	public String Playing = "";
	public Animation PlayingAnim = null;
	public String PlayingData = "";
	
	public String AnimKey1 = "sit"; public boolean Anim1isFrames = false;
	public String AnimKey2 = ""; public boolean Anim2isFrames = false;
	public String AnimKey3 = ""; public boolean Anim3isFrames = false;
	public String AnimKey4 = ""; public boolean Anim4isFrames = false;
	public String AnimKey5 = ""; public boolean Anim5isFrames = false;
 	
	public List<Animation> list = new ArrayList<Animation>();
	public Map<String, Animation> listN = Maps.newHashMap();
	
	public playerAnimations() {
		Animation animSit = new Animation();
		animSit.id = "sit";
		animSit.setAngle("leftleg", new MapVec3(-1.5F, -.25F, 0), new MapBool(true));// -1.5F -.25F 0
		animSit.setAngle("rightleg", new MapVec3(-1.5F, .25F, 0), new MapBool(true));
		animSit.setAngle("leftarm", new MapVec3(-.5F, 0, 0), new MapBool(true));
		animSit.setAngle("rightarm", new MapVec3(-.5F, 0, 0), new MapBool(true));
		animSit.ColHeight = 1.4F;
		animSit.Translate = new MapVec3(0F, -.575F, 0F);
		animSit.pre = true;
		addAnim(animSit);
	}
	
	public void SaveAnimation(int ID) {
		if (list.get(ID).pre == false) {
			File folder = new File(path);
			@SuppressWarnings("unused")
			File[] files = folder.listFiles();
			String file = path + "/" + list.get(ID).id + ".txt";
			Path Directories = Paths.get(path);
			
			if (!folder.exists()) {
				try {
					Files.createDirectories(Directories);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Writer writer = null;
			try {
			    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			    writer.write(toString(ID));
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
			   try { writer.close(); } catch (IOException e) { /*ignore*/ }
			}
		}
	}
	
	public void LoadAnimations() {
		File folder = new File(path);
		if (!(folder.exists())) return;
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(files[i]));
				String s = reader.readLine();
				Animation anim = REfromString(s.split("/")[0], s.split("/")[1]); anim.id = files[i].getName().replace(".txt", "");
				addAnim(anim);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try { reader.close(); } catch (IOException e) { /*ignore*/ }
			}
		}
	}
	
	public String toString(int ID) {
		String s = "";
		Animation anim = list.get(ID);
		for (int i = 0; i < anim.AngleNames.size(); i++) {
			String a = "";
			String b = anim.AngleNames.get(i);
			a += b + ":";
			a += anim.toString(i);
			if (i < anim.AnglePoses.size() - 1) a += ";";
			s += a;
		}
		s += "/transform:";
		s += anim.Translate.X + ",";
		s += anim.Translate.Y + ",";
		s += anim.Translate.Z + ";colheight:";
		s += anim.ColHeight + ";stoponwalk:";
		s += anim.stoponwalk;
		return s;
	}
	
	public Animation REfromString(String s, String trans) {
		Animation anim = new Animation();
		String[] a = s.contains(";") ? s.split(";") : new String[] { s };
		String[] aa = trans.contains(";") ? trans.split(";") : new String[] { trans };
		for (int i = 0; i < a.length; i++) {
			String[] b = a[i].split(":");
			String[] c = b[1].split(",");
			anim.setAngle(b[0], new MapVec3(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2])), new MapBool(Boolean.parseBoolean(c[3])));
			if (c.length > 4) {
				anim.AngleRotPoint.get(b[0]).X = Float.parseFloat(c[4]);
				anim.AngleRotPoint.get(b[0]).Y = Float.parseFloat(c[5]);
				anim.AngleRotPoint.get(b[0]).Z = Float.parseFloat(c[6]);
			}
		}
		{
			String[] b = aa[0].split(":");
			String[] c = b[1].split(",");
			anim.Translate = new MapVec3(c[0], c[1], c[2]);
			String[] bb = aa[1].split(":");
			anim.ColHeight = Float.parseFloat(bb[1]);
			if (aa.length >= 3) {
				String[] bbb = aa[2].split(":");
				anim.stoponwalk = Boolean.valueOf(bbb[1]);
			}
		}
		return anim;
	}
	
	public void addAnim(Animation anim) {
		list.add(anim);
		listN.put(anim.id, anim);
	}
	
	public void removeAnim(int anim) {
		if (list.get(anim).pre == false) {
			File folder = new File(path);
			@SuppressWarnings("unused")
			File[] files = folder.listFiles();
			String file = path + "/" + list.get(anim).id + ".txt";
			Path Directories = Paths.get(path);
			
			if (!folder.exists()) {
				try {
					Files.createDirectories(Directories);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			
			Path path = new File(file).toPath();
			try {
				Files.delete(path);
			} catch (IOException e) {
				Main.LOGGER.info("[" + Reference.MOD_ID + "]Failed to remove animation file");
				e.printStackTrace();
				Main.LOGGER.info("[" + Reference.MOD_ID + "]Failed to remove animation file");
			}
		}
		listN.remove(list.get(anim).id);
		list.remove(anim);
	}
	
	public int getAnimIDfromName(String name) {
		int id = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).id.equalsIgnoreCase(name)) {
				id = i;
				break;
			}
		}
		return id;
	}
	
}
