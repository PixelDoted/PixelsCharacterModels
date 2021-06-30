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
import java.nio.file.Paths;

import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PresetsSaveData {
	
	public String PresetsPath = "{mcdir}\\PCM\\Presets";
	
	public void Initialize() {
		System.out.println("Checking Presets Folder");
		PresetsPath = PresetsPath.replace("{mcdir}", PixelsCharacterModels.client.runDirectory.toString());
		File folder = new File(PresetsPath);
		if (!folder.exists()) {
			try {
				Files.createDirectories(Paths.get(folder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean loadPreset(int id, PlayerEntity player, PlayerEntityModel<?> model) {
		File file = getPreset(id);
		if (file == null) return false;
		if (!file.exists()) return false;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			Gson gson = new Gson();
			PresetData data = gson.fromJson(reader, PresetData.class);
			data.convertToModel(player, model);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try { reader.close(); } catch (IOException e) { }
		}
		return true;
	}
	
	public File[] getPresets() {
		File folder = new File(PresetsPath);
		if (!folder.exists()) return new File[0];
		return folder.listFiles();
	}
	
	public File getPreset(int id) {
		File[] files = getPresets();
		if (files.length <= id) return null;
		return files[id];
	}
	
	public void writePresetFile(PresetData data, String name) {
		String file = PresetsPath + "/" + name + ".json";
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
		    Gson gson = new Gson();
		    writer.write(gson.toJson(data));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
		   try { writer.close(); } catch (IOException e) { }
		}
	}
	
}
