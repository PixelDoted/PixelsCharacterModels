package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class FileHelper {

    // read from a file
    public static String read(File file) {
        String output = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            output = new String(fis.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if (fis != null) fis.close(); } catch (IOException e) {}
        }

        return output;
    }

    // read from a file
    public static Object read(File file, Class<?> c) {
        Object output = null;
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            output = new Gson().fromJson(reader, c);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if (reader != null) reader.close(); } catch (IOException e) {}
        }

        return output;
    }

    // write to a file
    public static boolean write(File file, String script) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(script);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if (writer != null) writer.close(); } catch (IOException e) {}
        }
        return false;
    }

    // write to a file
    public static boolean write(File file, Object object) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            new Gson().toJson(object, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if (writer != null) writer.close(); } catch (IOException e) {}
        }
        return false;
    }

}