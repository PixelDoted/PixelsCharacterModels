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
import me.pixeldots.pixelscharactermodels.utils.data.FramesData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class FramesSaveData {

	public String FramesPath = "{mcdir}/PCM/Frames";
	
	public void Initialize() {
		System.out.println("Checking Frames Folder");
		FramesPath.replace("/", File.separator);
		FramesPath = FramesPath.replace("{mcdir}", PixelsCharacterModels.client.minecraft.runDirectory.toString());
		File folder = new File(FramesPath);
		if (!folder.exists()) {
			try {
				Files.createDirectories(Paths.get(folder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean loadFrame(String name, PlayerEntity player, PlayerEntityModel<?> model) {
		File[] files = getFrames();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().replace(".json", "").startsWith(name)) {
				return loadFrame(i, player, model);
			}
		}
		return false;
	}
	
	public boolean loadFrame(int id, PlayerEntity player, PlayerEntityModel<?> model) {
		File file = getFrame(id);
		if (file == null) return false;
		if (!file.exists()) return false;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			Gson gson = new Gson();
			FramesData data = gson.fromJson(reader, FramesData.class);
			PixelsCharacterModels.client.currentStoredFrames = data.convertToFrames(player, model);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try { reader.close(); } catch (IOException e) { }
		}
		return true;
	}
	
	public File[] getFrames() {
		File folder = new File(FramesPath);
		if (!folder.exists()) return new File[0];
		return folder.listFiles();
	}
	
	public File getFrame(int id) {
		File[] files = getFrames();
		if (files.length <= id) return null;
		return files[id];
	}
	
	public void writeFramesFile(FramesData data, String name) {
		String file = FramesPath + "/" + name + ".json";
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
