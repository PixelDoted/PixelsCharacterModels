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
import me.pixeldots.pixelscharactermodels.utils.data.AnimationData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class AnimationsSaveData {

	public String AnimationsPath = "{mcdir}\\PCM\\Animations";
	
	public void Initialize() {
		System.out.println("Checking Animations Folder");
		AnimationsPath = AnimationsPath.replace("{mcdir}", MinecraftClient.getInstance().runDirectory.toString());
		File folder = new File(AnimationsPath);
		if (!folder.exists()) {
			try {
				Files.createDirectories(Paths.get(folder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean loadAnimation(String name, PlayerEntity player, PlayerEntityModel<?> model) {
		File[] files = getAnimations();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().replace(".json", "").startsWith(name)) {
				return loadAnimation(i, player, model);
			}
		}
		return false;
	}
	
	public boolean loadAnimation(int id, PlayerEntity player, PlayerEntityModel<?> model) {
		File file = getAnimation(id);
		if (file == null) return false;
		if (!file.exists()) return false;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			Gson gson = new Gson();
			AnimationData data = gson.fromJson(reader, AnimationData.class);
			PixelsCharacterModels.PCMClient.currentStoredAnimation = data.convertToAnimation(player, model);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try { reader.close(); } catch (IOException e) { }
		}
		return true;
	}
	
	public File[] getAnimations() {
		File folder = new File(AnimationsPath);
		if (!folder.exists()) return new File[0];
		return folder.listFiles();
	}
	
	public File getAnimation(int id) {
		File[] files = getAnimations();
		if (files.length <= id) return null;
		return files[id];
	}
	
	public void writeAnimationFile(AnimationData data, String name) {
		String file = AnimationsPath + "/" + name + ".json";
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
