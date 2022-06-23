package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.old.OldAnimationData;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineType;
import me.pixeldots.scriptedmodels.script.line.LineUtils;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3f;

public class AnimationHelper {

    // writes the animation file to file
    public static boolean write(File file, AnimationFile animation) {
        //file.mkdir();
        return FileHelper.write(file, animation);
    }

    // reads an animation file from a file
    public static AnimationFile read(File file, boolean read_filler) {
        AnimationFile animation = null;
        if (FileHelper.read(file).contains("name")) animation = OldAnimationData.toNew(file);
        else animation = (AnimationFile)FileHelper.read(file, AnimationFile.class);

        if (read_filler) getInbetweenFrames(animation);
        return animation;
    }

    // plays an animation from file
    public static AnimationFile play(File file, LivingEntity entity, EntityModel<?> model) {
        AnimationFile animation = read(file, true);
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
        lines.addAll(f_lines); // add all animation lines
    }

    // get the currently playing animation
    public static String get_current(LivingEntity entity, EntityModel<?> model) {
        UUID uuid = entity.getUuid();
        if (!ScriptedModels.EntityScript.containsKey(uuid)) return "";

        List<Line> lines = ScriptedModels.EntityScript.get(entity.getUuid()).global; // the entity's global scripts
        for (Line line : lines) {
            if (line.type == LineType.DEFINE && line.data.length >= 4 && LineUtils.getString(line.data, 1).equalsIgnoreCase("animation"))
                return LineUtils.getString(line.data, 3);
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
        String stopped_animation = clean_lines(root_lines);
        scripted.global = root_lines;

        for (ModelPart part : PlatformUtils.getHeadParts(model)) { // clean all modelpart scripts
            if (!scripted.parts.containsKey(part)) continue;

            List<Line> lines = scripted.parts.get(part);
            clean_lines(lines);
            scripted.parts.put(part, lines);
        }
        for (ModelPart part : PlatformUtils.getBodyParts(model)) { // clean all modelpart scripts
            if (!scripted.parts.containsKey(part)) continue;

            List<Line> lines = scripted.parts.get(part);
            clean_lines(lines);
            scripted.parts.put(part, lines);
        }

        return stopped_animation;
    }

    // cleans all the lines with the animation definition
    private static String clean_lines(List<Line> lines) {
        String animation = "";
        int anim_count = 0;
        int index = 0;
        
        for (Line line : lines) {
            if (anim_count > 0) {
                lines.remove(index);
                anim_count--;
                continue;
            } else if (line.type == LineType.DEFINE) {
                if (line.data.length >= 4 && LineUtils.getString(line.data, 1).equalsIgnoreCase("animation")) {
                    lines.remove(index);
                    anim_count = Math.round(LineUtils.getFloat(line.data, 2));
                    animation = LineUtils.getString(line.data, 3);
                    continue;
                }
            }
            index++;
        }
        return animation;
    }

    public static void getInbetweenFrames(AnimationFile file) {
        List<AnimationFile.Frame> new_frames = new ArrayList<>();
        for (int i = 0; i < file.frames.size(); i++) {
            AnimationFile.Frame frame = file.frames.get(i);
            AnimationFile.Frame next = file.frames.get(i+1 >= file.frames.size() ? 0 : i+1);
            new_frames.add(frame);

            List<AnimationFile.Frame> frames = getInbetweenFrames(frame, next);
            for (AnimationFile.Frame f : frames) {
                new_frames.add(f);
            }
        }

        file.frames = new_frames;
    }

    private static List<AnimationFile.Frame> getInbetweenFrames(AnimationFile.Frame A, AnimationFile.Frame B) {
        List<String> root = getInbetweenScripts(A.script.split("\n"), B.script.split("\n"), B.run_frame);
        Map<String, List<String>> parts = new HashMap<>();
        for (String key : A.parts.keySet()) {
            parts.put(key, getInbetweenScripts(A.parts.get(key).split("\n"), B.parts.get(key).split("\n"), B.run_frame));
        }

        List<AnimationFile.Frame> frames = new ArrayList<>();
        for (int i = 0; i < root.size(); i++) {
            AnimationFile.Frame frame = new AnimationFile.Frame();
            frame.run_frame = i+1;
            frame.script = root.get(i);
            frame.is_filler = true;

            for (String key : parts.keySet()) {
                frame.parts.put(key, parts.get(key).get(i));
            }
            
            frames.add(frame);
        }

        return frames;
    }

    private static List<String> getInbetweenScripts(String[] A, String[] B, int frame_difference) {
        Map<String, List<Vec3f>> values_a = new HashMap<>();
        for (String s : A) {
            String[] split = s.split(" ");
            if (split.length != 4) continue;

            Vec3f value = new Vec3f(PCMUtils.getFloat(split[1]), PCMUtils.getFloat(split[2]), PCMUtils.getFloat(split[3]));
            if (!values_a.containsKey(split[0])) values_a.put(split[0], new ArrayList<>());

            values_a.get(split[0]).add(value);
        }

        List<String> scripts = new ArrayList<>();
        for (int i = 1; i < frame_difference; i++) {
            String script = "";
            Map<String, Integer> value_index = new HashMap<>();
            for (String s : B) {
                String[] split = s.split(" ");
                if (!value_index.containsKey(split[0])) value_index.put(split[0], 0);
                if (split.length != 4 || !values_a.containsKey(split[0])) {
                    script += s + "\n";
                    continue;
                }

                Vec3f a = values_a.get(split[0]).get(value_index.get(split[0]));
                Vec3f b = new Vec3f(PCMUtils.getFloat(split[1]), PCMUtils.getFloat(split[2]), PCMUtils.getFloat(split[3]));
                
                float diff = i/(float)frame_difference;
                Vec3f v = new Vec3f(a.getX()+(b.getX()-a.getX())*diff, a.getY()+(b.getY()-a.getY())*diff, a.getZ()+(b.getZ()-a.getZ())*diff);

                script += split[0] + " " + v.getX() + " " + v.getY() + " " + v.getZ() + "\n";
                value_index.put(split[0], value_index.get(split[0])+1);
            }

            scripts.add(script);
        }

        return scripts;
    }

}