package me.PixelDots.PixelsCharacterModels.Frames;

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

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;
import net.minecraft.client.Minecraft;

public class FramesController 
{
	
	public String path = Minecraft.getInstance().gameDir + "\\PCM\\Frames";
	public List<Frames> list = new ArrayList<Frames>();
	
	public FramesController() {
	}
	
	public void LoadFrames() {
		File folder = new File(path);
		if (!(folder.exists())) return;
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(files[i]));
				String s = reader.readLine();
				Frames anim = new Frames();
				anim.fromString(s);
				anim.id = files[i].getName().replace(".txt", "");
				list.add(anim);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try { reader.close(); } catch (IOException e) { /*ignore*/ }
			}
		}
	}
	
	public void SaveFrames(int ID) {
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
		    writer.write(list.get(ID).toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
		   try { writer.close(); } catch (IOException e) { /*ignore*/ }
		}
	}
	
	public void removeFrames(int ID) {
		File folder = new File(path);
		@SuppressWarnings("unused")
		File[] files = folder.listFiles();
		String file = path + "/" + list.get(ID).id + ".txt";
		if (!folder.exists()) return;
		File fileF = new File(file);
		fileF.delete();
		list.remove(ID);
	}
	
	//Animations
	public void LoadFrameAnims() {
		for (int i = 0; i < list.size(); i++) {
			for (int x = 0; x < list.get(i).animNames.size(); x++) {
				String name = list.get(i).animNames.get(x);
				Animation anim = Main.animations.listN.get(name);
				list.get(i).anims.put(name, anim);
			}
		}
	}
	//Animations
	
}
