package me.pixeldots.pixelscharactermodels;

import java.nio.file.Path;

import me.pixeldots.pixelscharactermodels.files.FileHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class PCMSettings {
    
    @Environment(EnvType.CLIENT) public boolean show_block_under_player_ui = true; // if enabled, render a block under the player in the GUI
    @Environment(EnvType.CLIENT) public boolean player_faces_cursor_ui = true; // if enabled, the rendered player faces the cursor in the GUI
    @Environment(EnvType.CLIENT) public boolean keybinding_opens_editor = false; // if enabled, pressing the openGUI keybinding opens the Editor GUI
    @Environment(EnvType.CLIENT) public boolean preview_preset = true; // if enabled, to use a preset you have to press select in the Presets GUI
    @Environment(EnvType.CLIENT) public boolean radians_instead_of_degress = false; // if enabled, rotate and angle will show degress instead of radians
    @Environment(EnvType.CLIENT) public boolean show_nametags = true; // if disabled, nametags won't be shown
    @Environment(EnvType.CLIENT) public boolean show_armor = true; // is disabled, armor won't be shown

    public boolean save_entity_scripts = false; // if enabled, saves entity data to the server

    // loads settings from Path
    public static PCMSettings load(Path path) {

        PCMSettings settings = (PCMSettings)FileHelper.read(path.toFile(), PCMSettings.class);
        if (settings == null) settings = new PCMSettings();

        settings.save(path);
        return settings;
    }

    // saves the settings to Path
    public void save(Path path) {
        FileHelper.write(path.toFile(), this);
    }

}
