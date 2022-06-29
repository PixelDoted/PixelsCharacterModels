package me.pixeldots.pixelscharactermodels.utils.Load;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.main.PixelsCharacterModelsMain;
import me.pixeldots.pixelscharactermodels.utils.data.PresetData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PresetsSaveData {
	
	public String PresetsPath = "{mcdir}/PCM/Presets";

	public void Initialize() {
		System.out.println("Checking Presets Folder");
		PresetsPath.replace("/", File.separator);
		PresetsPath = PresetsPath.replace("{mcdir}", PixelsCharacterModels.client.minecraft.runDirectory.toString());
		
		File folder = new File(PresetsPath);
		if (!folder.exists()) {
			try {
				Files.createDirectories(Paths.get(folder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean loadPreset(String path, PlayerEntity player, PlayerEntityModel<?> model) {
		File file = getPreset(path);
		if (file == null) return false;
		if (!file.exists()) return false;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			Gson gson = new Gson();
			PresetData data = gson.fromJson(reader, PresetData.class);
			
			data.convertToModel(player, model, false);
			PixelsCharacterModelsMain.clientHandler.sendModelData(gson.toJson(data));
		} catch (Exception e) {
			e.printStackTrace();
			PixelsCharacterModels.client.sendClientMessage("An error occurred while loading that preset");
			PixelsCharacterModels.client.sendClientMessage("This could be caused by the .json file being corrupted or invalid");
		} finally {
			try { if (reader != null) reader.close(); } catch(IOException e) { }
		}
		return true;
	}
	
	public File[] getPresets(String path) {
		File folder = new File(PresetsPath);
		if (path != null && !path.equals("")) folder = new File(path);
		if (!folder.exists()) return new File[0];
		return folder.listFiles();
	}
	
	public File getPreset(String path) {
		return new File(PresetsPath + File.separator + path);
		/*File[] files = getPresets();
		if (files.length <= id) return null;
		return files[id];*/
	}
	
	public void writePresetFile(PresetData data, String path) {
		String file = (PresetsPath + File.separator + path);
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
		    Gson gson = new Gson();
		    writer.write(gson.toJson(data));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
		   try { if (writer != null) writer.close(); } catch(IOException e) { }
		}
	}
	
}
