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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.model.LocalData;
import me.pixeldots.pixelscharactermodels.utils.MapModelVectors;
import net.minecraft.client.MinecraftClient;

public class OtherSaveData {
	
	public String path = MinecraftClient.getInstance().runDirectory + "\\PCM";
	
	public void Initialize() {
		Load();
		System.out.println("Checking Models Folder");
		File meshFolder = new File(path + "\\Models");
		if (!meshFolder.exists()) {
			try {
				Files.createDirectories(Paths.get(meshFolder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
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
				} finally {
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
	public File[] getMeshes() {
		File folder = new File(path + "\\Models");
		return folder.listFiles();
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
	
	public MapModelVectors ParseFile(Object[] text) {
		MapModelVectors parsed = new MapModelVectors();
		for (int i = 0; i < text.length; i++) {
			if (((String)text[i]).startsWith("v ")) {
				String s = (String)text[i]; 
				parsed.Vertices.add(s);
			}
			else if (((String)text[i]).startsWith("vt ")) { 
				String s = (String)text[i]; 
				parsed.VertexUVs.add(s);
			}
			else if (((String)text[i]).startsWith("vn ")) {
				String s = (String)text[i];
				parsed.VertexNormals.add(s);
			}
			else if (((String)text[i]).startsWith("f ")) { 
				String s = (String)text[i]; 
				parsed.Faces.add(s); 
			}
 		}
		return parsed;
	}
	
	public MapModelVectors getMeshData(String name) {
		if (name == "" || name == "cube") return null;
		File folder = new File(path + "\\Models");
		File[] files = folder.listFiles();
		BufferedReader reader = null;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().replace(".obj", "").startsWith(name)) {
				try {
					reader = new BufferedReader(new FileReader(files[i]));
					return ParseFile(reader.lines().toArray());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try { if (reader != null) reader.close(); } catch (IOException e) { }
				}
			}
		}
		return null;
	}
}
