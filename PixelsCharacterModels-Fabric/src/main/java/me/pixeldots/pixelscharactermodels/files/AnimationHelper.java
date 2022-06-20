package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public class AnimationHelper {

    // writes the animation file to file
    public static boolean write(File file, AnimationFile animation) {
        file.mkdirs();
        return FileHelper.write(file, animation);
    }

    // reads an animation file from a file
    public static AnimationFile read(File file) {
        return (AnimationFile)FileHelper.read(file, AnimationFile.class);
    }

    // plays an animation from file
    public static AnimationFile play(File file, LivingEntity entity, EntityModel<?> model) {
        AnimationFile animation = read(file);
        if (animation == null) return null;
        if (animation.frames.size() > 1)
            PCMClient.EntityAnimationList.put(entity.getUuid(), new AnimationPlayer(animation, file.getName()));

        play(file.getName(), null, animation.frames.get(0), entity, model);
        return animation;
    }

    // plays an animation frame
    public static void play(String name, AnimationPlayer player, AnimationFile.Frame frame, LivingEntity entity, EntityModel<?> model) {
        UUID uuid = entity.getUuid();
        if (!ScriptedModels.EntityScript.containsKey(uuid))
            ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());

        ScriptedEntity scripted = ScriptedModels.EntityScript.get(uuid); // the entity's scripts

        add_lines(name, scripted.global, frame.script); // add lines to global
        int index = 0;
        for (ModelPart part : PlatformUtils.getHeadParts(model)) { // add lines to ModelParts
            String part_name = ModelPartNames.getHeadName(entity, index).toLowerCase();
            index++;

            if (!frame.parts.containsKey(part_name)) continue;
            if (!scripted.parts.containsKey(part)) scripted.parts.put(part, new ArrayList<>());
            add_lines(name, scripted.parts.get(part), frame.parts.get(part_name));
        }
        index = 0;
        for (ModelPart part : PlatformUtils.getBodyParts(model)) { // add lines to ModelParts
            String part_name = ModelPartNames.getBodyName(entity, index).toLowerCase();
            index++;

            if (!frame.parts.containsKey(part_name)) continue;
            if (!scripted.parts.containsKey(part)) scripted.parts.put(part, new ArrayList<>());
            add_lines(name, scripted.parts.get(part), frame.parts.get(part_name));
        }
    }

    // adds lines to an entity/modelpart script
    private static void add_lines(String name, List<Line> lines, String script) {
        List<Line> f_lines = Interpreter.compile(script.split("\n"));
        int length = f_lines.size();
        Line define = Interpreter.compile_line("define " + length + " animation " + length + " " + name);
        lines.add(define); // add the define line

        for (Line line : f_lines) {
            lines.add(line);
        }
    }

    // get the currently playing animation
    public static String get_current(LivingEntity entity, EntityModel<?> model) {
        UUID uuid = entity.getUuid();
        if (!ScriptedModels.EntityScript.containsKey(uuid)) return "";

        List<Line> lines = ScriptedModels.EntityScript.get(entity.getUuid()).global; // the entity's global scripts
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

    // stops an animation
    public static String stop(LivingEntity entity, EntityModel<?> model, boolean can_remove_entityanim) {
        UUID uuid = entity.getUuid();
        if (!ScriptedModels.EntityScript.containsKey(uuid)) return "";
        if (can_remove_entityanim && PCMClient.EntityAnimationList.containsKey(entity.getUuid())) 
            PCMClient.EntityAnimationList.remove(uuid);

        ScriptedEntity scripted = ScriptedModels.EntityScript.get(uuid);
        List<Line> root_lines = scripted.global;
        String stopped_animation = clean_lines(root_lines, Interpreter.decompile(root_lines).split("\n"));
        scripted.global = root_lines;

        for (ModelPart part : PlatformUtils.getHeadParts(model)) { // clean all modelpart scripts
            if (!scripted.parts.containsKey(part)) continue;

            List<Line> lines = scripted.parts.get(part);
            clean_lines(lines, Interpreter.decompile(lines).split("\n"));
            scripted.parts.put(part, lines);
        }
        for (ModelPart part : PlatformUtils.getBodyParts(model)) { // clean all modelpart scripts
            if (!scripted.parts.containsKey(part)) continue;

            List<Line> lines = scripted.parts.get(part);
            clean_lines(lines, Interpreter.decompile(lines).split("\n"));
            scripted.parts.put(part, lines);
        }

        return stopped_animation;
    }

    // cleans all the lines with the animation definition
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
                    anim_count = Math.round(PCMUtils.getFloat(define[3]));
                    animation = define[4];
                    index--;
                }
            }
            index++;
        }

        return animation;
    }

}