package me.pixeldots.pixelscharactermodels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class PCMSettings {
    
    @Environment(EnvType.CLIENT) public boolean show_block_under_player_ui = true; // if enabled, render a block under the player in the GUI
    @Environment(EnvType.CLIENT) public boolean player_faces_cursor_ui = true; // if enabled, the rendered player faces the cursor in the GUI
    @Environment(EnvType.CLIENT) public boolean keybinding_opens_editor = false; // if enabled, pressing the openGUI keybinding opens the Editor GUI
    @Environment(EnvType.CLIENT) public boolean preview_preset = true; // if enabled, to use a preset you have to press select in the Presets GUI
    
    public boolean save_entity_scripts = false; // if enabled, saves entity data to the server

    // loads settings from Path
    public static PCMSettings load(Path path) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(new FileReader(path.toFile()), PCMSettings.class);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }

        // creates a new settings file if it fails to read the settings
        PCMSettings settings = new PCMSettings();
        settings.save(path);
        return settings;
    }

    // saves the settings to Path
    public void save(Path path) {
        Gson gson = new Gson();
        FileWriter writer = null;
        try {
            writer = new FileWriter(path.toFile());
            gson.toJson(this, writer);
        } catch (JsonIOException | IOException e) {
            e.printStackTrace();
        } finally {
            try { writer.close(); } catch (IOException e) {}
        }
    }

}
