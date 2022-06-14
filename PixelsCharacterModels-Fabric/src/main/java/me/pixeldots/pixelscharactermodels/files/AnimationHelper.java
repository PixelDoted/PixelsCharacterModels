package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class AnimationHelper {

    public static boolean write(File file, AnimationFile animation) {
        FileWriter writer = null;
        Gson gson = new Gson();

        try {
            writer = new FileWriter(file);
            gson.toJson(animation, writer);
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { writer.close(); } catch (IOException e) {}
        }

        return true;
    }

    public static AnimationFile read(File file) {
        FileReader reader = null;
        Gson gson = new Gson();

        try {
            reader = new FileReader(file);
            return gson.fromJson(reader, AnimationFile.class);
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        } finally {
            try { reader.close(); } catch (IOException e) {}
        }
        return null;
    }

}