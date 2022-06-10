package me.pixeldots.pixelscharactermodels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class PCMSettings {
    
    public boolean show_block_under_player_ui = true;
    public boolean player_faces_cursor_ui = true;
    public boolean keybinding_opens_editor = false;
    public boolean preview_preset = true;

    public static PCMSettings load(Path path) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(new FileReader(path.toFile()), PCMSettings.class);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }

        PCMSettings settings = new PCMSettings();
        settings.save(path);
        return settings;
    }

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
