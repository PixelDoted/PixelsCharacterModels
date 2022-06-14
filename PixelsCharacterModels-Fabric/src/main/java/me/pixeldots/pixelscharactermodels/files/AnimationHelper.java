package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

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

    public static AnimationFile play(File file, LivingEntity entity, EntityModel<?> model) {
        AnimationFile animation = read(file);
        play(file.getName(), animation, entity, model);
        return animation;
    }

    public static void play(String name, AnimationFile animation, LivingEntity entity, EntityModel<?> model) {
        AnimationFile.Frame frame = animation.frames.get(0);
        ScriptedEntity scripted = ScriptedModels.EntityScript.get(entity.getUuid());

        add_lines(name, scripted.global, frame.script);
        for (ModelPart part : PlatformUtils.getHeadParts(model)) {
            if (!scripted.parts.containsKey(part)) continue;
            add_lines(name, scripted.parts.get(part), frame.script);
        }
        for (ModelPart part : PlatformUtils.getBodyParts(model)) {
            if (!scripted.parts.containsKey(part)) continue;
            add_lines(name, scripted.parts.get(part), frame.script);
        }
    }

    private static void add_lines(String name, List<Line> lines, String script) {
        List<Line> f_lines = Interpreter.compile(script.split("\n"));
        int length = f_lines.size();
        Line define = Interpreter.compile_line("define " + length + " animation " + length + " " + name);
        lines.add(define);

        for (Line line : f_lines) {
            lines.add(line);
        }
    }

    public static String get_current(LivingEntity entity, EntityModel<?> model) {
        List<Line> lines = ScriptedModels.EntityScript.get(entity.getUuid()).global;
        for (String line : Interpreter.decompile(lines).split("\n")) {
            if (line.toLowerCase().startsWith("define")) {
                String[] define = line.split(" ");
                if (define.length >= 4 && define[2].equalsIgnoreCase("animation")) {
                    return define[4];
                }
            }
        }
        
        return "";
    }

    public static String stop(LivingEntity entity, EntityModel<?> model) {
        String stopped_animation = "";

        ScriptedEntity scripted = ScriptedModels.EntityScript.get(entity.getUuid());
        List<Line> root_lines = scripted.global;
        stopped_animation = clean_lines(root_lines, Interpreter.decompile(root_lines).split("\n"));
        scripted.global = root_lines;

        for (ModelPart part : PlatformUtils.getHeadParts(model)) {
            if (!scripted.parts.containsKey(part)) continue;

            List<Line> lines = scripted.parts.get(part);
            clean_lines(lines, Interpreter.decompile(lines).split("\n"));
            scripted.parts.put(part, lines);
        }
        for (ModelPart part : PlatformUtils.getBodyParts(model)) {
            if (!scripted.parts.containsKey(part)) continue;

            List<Line> lines = scripted.parts.get(part);
            clean_lines(lines, Interpreter.decompile(lines).split("\n"));
            scripted.parts.put(part, lines);
        }
        return stopped_animation;
    }

    private static String clean_lines(List<Line> lines, String[] s_lines) {
        String animation = "";
        int anim_count = 0;
        int index = 0;

        for (String line : s_lines) {
            if (anim_count > 0) {
                lines.remove(index);
                anim_count--;
                index--;
            } else if (line.toLowerCase().startsWith("define")) {
                String[] define = line.split(" ");
                if (define.length >= 4 && define[2].equalsIgnoreCase("animation")) {
                    lines.remove(index);
                    anim_count = Math.round(Float.parseFloat(define[3]));
                    animation = define[4];
                    index--;
                }
            }
            index++;
        }

        return animation;
    }

}