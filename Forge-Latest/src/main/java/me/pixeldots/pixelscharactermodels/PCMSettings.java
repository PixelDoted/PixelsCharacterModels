package me.pixeldots.pixelscharactermodels;

import java.io.File;
import java.nio.file.Path;

import me.pixeldots.pixelscharactermodels.files.FileHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PCMSettings {
    
    @OnlyIn(Dist.CLIENT) public boolean show_block_under_player_ui = true; // if enabled, render a block under the player in the GUI
    @OnlyIn(Dist.CLIENT) public boolean player_faces_cursor_ui = true; // if enabled, the rendered player faces the cursor in the GUI
    @OnlyIn(Dist.CLIENT) public boolean keybinding_opens_editor = false; // if enabled, pressing the openGUI keybinding opens the Editor GUI
    @OnlyIn(Dist.CLIENT) public boolean preview_preset = false; // if enabled, to use a preset you have to press select in the Presets GUI
    @OnlyIn(Dist.CLIENT) public boolean radians_instead_of_degress = false; // if enabled, rotate and angle will show degress instead of radians
    @OnlyIn(Dist.CLIENT) public boolean show_nametags = true; // if disabled, nametags won't be shown
    @OnlyIn(Dist.CLIENT) public boolean show_armor = true; // if disabled, armor won't be shown

    @OnlyIn(Dist.CLIENT) public String[] animations = new String[5];

    public boolean save_entity_scripts = true; // if enabled, saves entity data to the server

    // loads settings from Path
    public static PCMSettings load(Path path) {
        File file = path.toFile();
        if (!file.exists()) return new PCMSettings();

        PCMSettings settings = (PCMSettings)FileHelper.read(file, PCMSettings.class);
        if (settings == null) { 
            settings = new PCMSettings();
            settings.save(path);
        }

        return settings;
    }

    // saves the settings to Path
    public void save(Path path) {
        FileHelper.write(path.toFile(), this);
    }

}
