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
import me.pixeldots.pixelscharactermodels.utils.MapModelVectors;
import me.pixeldots.pixelscharactermodels.utils.data.LocalData;

public class OtherSaveData {
	
	public String SavePath = "{mcdir}/PCM";
	
	public void Initialize() {
		SavePath.replace("/", File.separator);
		SavePath = SavePath.replace("{mcdir}", PixelsCharacterModels.client.minecraft.runDirectory.toString());
		Load();
		System.out.println("Checking Models Folder");
		
		File meshFolder = new File(SavePath+File.separator+"Models");
		if (!meshFolder.exists()) {
			try {
				Files.createDirectories(Paths.get(meshFolder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Save() {
		File folder = new File(SavePath+File.separator+"Models");
		String file = SavePath + File.separator + "Data.json";
		Path Directories = Paths.get(SavePath);
			
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
		    writer.write(new Gson().toJson(PixelsCharacterModels.localData));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
		   try { writer.close(); } catch (IOException e) { }
		}
	}
	
	public void Load() {
		File file = new File(SavePath + File.separator + "Data.json");
		if (!(file.exists())) return;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			PixelsCharacterModels.localData = new Gson().fromJson(reader, LocalData.class);
			System.out.println("Loaded Data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try { reader.close(); } catch (IOException e) {}
		}
	}
	
	//Mesh
	public File[] getMeshes() {
		File folder = new File(SavePath+File.separator+"Models");
		return folder.listFiles();
	}
	
	public String ReadMesh(int id) {
		File folder = new File(SavePath+File.separator+"Models");
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
		for (Object obj_text : text) {
			String txt = (String)obj_text;
			if (txt.startsWith("v ")) {
				parsed.Vertices.add(txt);
			} else if (txt.startsWith("vt ")) { 
				parsed.VertexUVs.add(txt);
			} else if (txt.startsWith("vn ")) {
				parsed.VertexNormals.add(txt);
			} else if (txt.startsWith("f ")) { 
				parsed.Faces.add(txt); 
			}
 		}
		return parsed;
	}
	
	public MapModelVectors getMeshData(String name) {
		if (name == "" || name == "cube") return null;
		File folder = new File(SavePath+File.separator+"Models");
		File[] files = folder.listFiles();
		BufferedReader reader = null;
		for (File file : files) {
			if (file.getName().replace(".obj", "").startsWith(name)) {
				try {
					reader = new BufferedReader(new FileReader(file));
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
