package me.PixelDots.PixelsCharacterModels.util;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.utils.Log;

import com.google.common.collect.Maps;

import me.PixelDots.PixelsCharacterModels.Main;
import net.minecraft.client.Minecraft;

public class OtherSaveData 
{
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
	public String path = Minecraft.getInstance().gameDir + "\\PCM";
	public List<Integer> Meshes = new ArrayList<Integer>();
	public List<String> MeshNames = new ArrayList<String>();
	public Map<String, Integer> MeshesfromName = Maps.newHashMap();
	
	public boolean sliders = false;
	public int maxURLlength = 32;
	public boolean showNameTags = true;
	public boolean showLoadingDatainChat = true;

	public OtherSaveData() {
		Load();
		getMeshes();
	}
	
	public void Save() {
		File folder = new File(path);
		@SuppressWarnings("unused")
		File[] files = folder.listFiles();
		String file = path + "/Data.txt";
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
		    writer.write(formatString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
		   try { writer.close(); } catch (IOException e) { /*ignore*/ }
		}
	}
	
	public void Load() {
		File folder = new File(path);
		if (!(folder.exists())) return;
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = null;
			if (files[i].getName().matches("Data.txt")) {
				try {
					reader = new BufferedReader(new FileReader(files[i]));
					String s = reader.readLine();
					formatData(s);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					try { reader.close(); } catch (IOException e) { /*ignore*/ }
				}
			}
		}
	}
	
	public String formatString() {
		String s = "";
		s += "animkey1:" + Main.animations.AnimKey1 + ",";
		s += "animkey2:" + Main.animations.AnimKey2 + ",";
		s += "animkey3:" + Main.animations.AnimKey3 + ",";
		s += "animkey4:" + Main.animations.AnimKey4 + ",";
		s += "animkey5:" + Main.animations.AnimKey5 + ",";
		s += "animFrame1:" + Main.animations.Anim1isFrames + ",";
		s += "animFrame2:" + Main.animations.Anim2isFrames + ",";
		s += "animFrame3:" + Main.animations.Anim3isFrames + ",";
		s += "animFrame4:" + Main.animations.Anim4isFrames + ",";
		s += "animFrame5:" + Main.animations.Anim5isFrames + ",";
		s += "Sliders:" + this.sliders + ",";
		s += "UrlMaxLength:" + this.maxURLlength + ",";
		s += "showNameTags:" + this.showNameTags + ",";
		s += "showLoadingDatainChat" + this.showLoadingDatainChat;
		return s;
	}
	
	public void formatData(String s) {
		if (s == "") return;
		try {
			String[] a = s.split(",");
			Main.animations.AnimKey1 = a[0].split(":").length >= 2 ? a[0].split(":")[1] : "";
			Main.animations.AnimKey2 = a[1].split(":").length >= 2 ? a[1].split(":")[1] : "";
			Main.animations.AnimKey3 = a[2].split(":").length >= 2 ? a[2].split(":")[1] : "";
			Main.animations.AnimKey4 = a[3].split(":").length >= 2 ? a[3].split(":")[1] : "";
			Main.animations.AnimKey5 = a[4].split(":").length >= 2 ? a[4].split(":")[1] : "";
			Main.animations.Anim1isFrames = Boolean.valueOf(a[5].split(":").length >= 2 ? a[5].split(":")[1] : "false");
			Main.animations.Anim2isFrames = Boolean.valueOf(a[6].split(":").length >= 2 ? a[6].split(":")[1] : "false");
			Main.animations.Anim3isFrames = Boolean.valueOf(a[7].split(":").length >= 2 ? a[7].split(":")[1] : "false");
			Main.animations.Anim4isFrames = Boolean.valueOf(a[8].split(":").length >= 2 ? a[8].split(":")[1] : "false");
			Main.animations.Anim5isFrames = Boolean.valueOf(a[9].split(":").length >= 2 ? a[9].split(":")[1] : "false");
			this.sliders = Boolean.valueOf(a[10].split(":").length >= 2 ? a[10].split(":")[1] : "false");
			this.maxURLlength = Integer.valueOf((a[11].split(":").length >= 2 ? a[11].split(":")[1] : "32"));
			this.showNameTags = Boolean.valueOf((a[12].split(":").length >= 2 ? a[12].split(":")[1] : "true"));
			this.showLoadingDatainChat = Boolean.valueOf((a[13].split(":").length >= 2 ? a[13].split(":")[1] : "true"));
		}catch (Exception e){
			Log.error("PCM: Data.txt is invalid or outdated");
			e.printStackTrace();
			Log.error("PCM: Data.txt is invalid or outdated");
		}
	}
	//Mesh
	public void getMeshes() {
		File folder = new File(path + "\\Models");
		if (!folder.exists()) {
			try {
				Files.createDirectories(Paths.get(folder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith(".obj")) {
				Meshes.add(i);
				MeshesfromName.put(files[i].getName().replace(".obj", ""), i);
				MeshNames.add(files[i].getName().replace(".obj", ""));
			}
		}
	}
	
	public String ReadMesh(int id) {
		File folder = new File(path + "\\Models");
		File file = folder.listFiles()[id];
		String s = "";
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			s = reader.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {reader.close();} catch (IOException e) {}
		}
		
		return s;
	}
	
	public String ParseFile(Object[] text) {
		String parsed = "";
		String verts = "";
		String faces = "";
		String uvs = "";
		String normals = "";
		for (int i = 0; i < text.length; i++) {
			if (((String)text[i]).startsWith("v ")) {
				String s = (String)text[i]; 
				if (text.length - 1 > i) if (((String)text[i]).startsWith("v "))  s += ";";
				verts += s; 
			}
			else if (((String)text[i]).startsWith("vt ")) { 
				String s = (String)text[i]; 
				if (text.length - 1 > i) if (((String)text[i]).startsWith("vt "))  s += ";";
				uvs += s; 
			}
			else if (((String)text[i]).startsWith("vn ")) {
				String s = (String)text[i];
				if (text.length - 1 > i) if (((String)text[i]).startsWith("vn ")) s += ";";
				normals += s;
			}
			else if (((String)text[i]).startsWith("f ")) { 
				String s = (String)text[i]; 
				if (text.length - 1 > i) if (((String)text[i]).startsWith("f "))  s += ";";
				faces += s; 
			}
 		}
		parsed = verts + "#" + faces + "#" + uvs + "#" + normals;
		return parsed;
	}
	
	public String getMeshData(String name) {
		String s = "";
		if (name == "" || name == "cube") return s;
		File folder = new File(path + "\\Models");
		File[] files = folder.listFiles();
		File file = files[MeshesfromName.get(name)];
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			s = ParseFile(reader.lines().toArray());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try { if (reader != null) reader.close(); } catch (IOException e) { }
		}
		return s;
	}
}
