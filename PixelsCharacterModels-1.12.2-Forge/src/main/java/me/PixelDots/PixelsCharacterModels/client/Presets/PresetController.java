package me.PixelDots.PixelsCharacterModels.client.Presets;

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

import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import net.minecraft.client.Minecraft;

public class PresetController 
{
	
	public String path = Minecraft.getInstance().gameDir + "\\PCM\\Presets";
	
	public List<Preset> list = new ArrayList<Preset>();
	public static PresetController instance;
	public int lockedin;
	public int selected;
	
	
	public PresetController() {
        PresetController.instance = this;
        LoadPresets();
    }
	
	public void CreatePreset(GlobalModelData data, String name) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name == name) {
				return;
			}
		}
		Preset preset = new Preset();
		preset.data = data;
		preset.name = name;
		list.add(preset);
		SavePreset(list.size() - 1);
		LoadPresets();
	}
	
	public void SetPreset(int ID, GlobalModelData data) {
		list.get(ID).data = data;
		SavePreset(ID);
	}
	
	public void RemovePreset(int ID) {
		File folder = new File(path);
		@SuppressWarnings("unused")
		File[] files = folder.listFiles();
		String file = path + "/" + list.get(ID).name + ".txt";
		if (!folder.exists()) return;
		File fileF = new File(file);
		fileF.delete();
		list.remove(ID);
 	}
	
	public void SavePreset(int ID) {
		File folder = new File(path);
		@SuppressWarnings("unused")
		File[] files = folder.listFiles();
		String file = path + "/" + list.get(ID).name + ".txt";
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
		    GlobalModelData data = list.get(ID).data;
		    writer.write(data.toPreString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
		   try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void LoadPresets() {
		list.clear();
		File folder = new File(path);
		if (!(folder.exists())) return;
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(files[i]));
				String s = reader.readLine();
				GlobalModelData data = new GlobalModelData();
				data.fromPreString(s);
				Preset preset = new Preset(); preset.name = files[i].getName().replace(".txt", ""); preset.data = data;
				list.add(preset);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
			}
		}
	}
	
	public int getPresetIDfromName(String id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equalsIgnoreCase(id)) {
				return i;
			}
		}
		return -1;
	}
	
	public Preset getPresetfromName(String id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equalsIgnoreCase(id)) {
				return list.get(i);
			}
		}
		return null;
	}
	
	public Preset getLockedIn() {
		for (int i = 0; i < list.size(); i++) {
			if (i == lockedin) {
				return list.get(i);
			}
		}
		return null;
	}
	
	public Preset getSelected(String s) {
		for (int i = 0; i < list.size(); i++) {
			if (i == selected) {
				return list.get(i);
			}
		}
		return null;
	}
	
}
