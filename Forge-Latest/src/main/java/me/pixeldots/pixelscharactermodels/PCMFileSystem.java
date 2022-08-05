package me.pixeldots.pixelscharactermodels;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PCMFileSystem {

    public static String Presets_Path = "PCM" + File.separator + "Presets";
    public static String Animations_Path = "PCM" + File.separator + "Animations";
    public static String Models_Path = "PCM" + File.separator + "Models";
    
    public static void init(String path) {
        Path Presets = Path.of(path + File.separator + Presets_Path);
        Path Animations = Path.of(path + File.separator + Animations_Path);
        Path Models = Path.of(path + File.separator + Models_Path);

        try {
            if (Files.exists(Presets))
                Files.createDirectories(Presets);

            if (Files.exists(Animations))
                Files.createDirectories(Animations);
                
            if (Files.exists(Models))
                Files.createDirectories(Models);
        } catch (IOException e) {}
    }

}
