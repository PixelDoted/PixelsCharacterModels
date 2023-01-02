package me.pixeldots.pixelscharactermodels;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PCMFileSystem {

    public static String Presets_Path = "PCM" + File.separator + "Presets";
    public static String Animations_Path = "PCM" + File.separator + "Animations";
    public static String Models_Path = "PCM" + File.separator + "Models";
    
    public static void init(String path) {
        Path Presets = Path.of(path + File.separator + Presets_Path);
        Path Animations = Path.of(path + File.separator + Animations_Path);
        Path Models = Path.of(path + File.separator + Models_Path);

		IOException presets_err = init_directory(Presets);
		IOException animations_err = init_directory(Animations);
		IOException models_err = init_directory(Models);
		
		if (presets_err != null) {
			System.out.println("-- Presets File Error Start --");
			presets_err.printStackTrace();
			System.out.println("-- Presets File Error End");
		}
		
		if (animations_err != null) {
			System.out.println("-- Animations File Error Start --");
			animations_err.printStackTrace();
			System.out.println("-- Animations File Error End --");
		}
		
		if (models_err != null) {
			System.out.println("-- Models File Error Start --");
			models_err.printStackTrace();
			System.out.println("-- Models File Error End --");
		}
    }
    
    static IOException init_directory(Path path) {
    	try {
    		if (!Files.exists(path))
    			Files.createDirectories(path);
    	} catch (IOException e) {
    		return e;
    	}
    	
    	return null;
    }

}
