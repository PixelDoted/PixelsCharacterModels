package me.pixeldots.pixelscharactermodels.utils.Load;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.model.LocalData;
import net.minecraft.client.MinecraftClient;

public class OtherSaveData {
	
	public String path = MinecraftClient.getInstance().runDirectory + "\\PCM";
	public List<Integer> Meshes = new ArrayList<Integer>();
	public List<String> MeshNames = new ArrayList<String>();
	public Map<String, Integer> MeshesfromName = Maps.newHashMap();
	
	public void Save() {
		File folder = new File(path);
		String file = path + "/Data.json";
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
		   try { writer.close(); } catch (IOException e) { }
		}
	}
	
	public void Load() {
		System.out.println("Loading Data");
		File folder = new File(path);
		if (!(folder.exists())) return;
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = null;
			if (files[i].getName().matches("Data.json")) {
				try {
					reader = new BufferedReader(new FileReader(files[i]));
					formatData(reader);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					try { reader.close(); } catch (IOException e) { }
				}
			}
		}
	}
	
	public String formatString() {
		Gson gson = new Gson();
		return gson.toJson(PixelsCharacterModels.localData);
	}
	
	public void formatData(BufferedReader reader) {
		if (reader == null) return;
		Gson gson = new Gson();
		PixelsCharacterModels.localData = gson.fromJson(reader, LocalData.class);
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
