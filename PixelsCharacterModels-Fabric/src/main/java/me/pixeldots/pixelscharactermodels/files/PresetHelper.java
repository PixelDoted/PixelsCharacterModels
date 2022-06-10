package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.spongepowered.include.com.google.gson.Gson;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames.EntityParts;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import me.pixeldots.scriptedmodels.script.PostfixOperation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleTypes;

public class PresetHelper {
    
    // Writing Preset
    public static void write_preset(File file, LivingEntity entity, IAnimalModelMixin model) {
        file.mkdir(); // create preset folder

        // add settings to the preset folder
        PresetSettings settings = new PresetSettings();
        settings.pehkui_scale = ScaleTypes.BASE.getScaleData(entity).getBaseScale();
        settings.skin_suffix = PCMClient.PlayerSkinList.get(entity.getUuid());
        write_settings(new File(file.getAbsolutePath() + "/preset.json"), settings);

        // add scripts to the preset folder
        String root = ClientHelper.decompile_script(entity.getUuid(), null);
        if (!root.trim().equals("")) write_script(new File(file.getAbsolutePath() + "/root.sm"), root);

        int index = 0;
        for (ModelPart part : model.getHeadParts()) {
            String script = ClientHelper.decompile_script(entity.getUuid(), part);
            if (!script.trim().equals("")) { 
                String name = ModelPartNames.getHeadName(entity, index).toLowerCase();
                if (PostfixOperation.isNumeric(name)) name = "" + (Integer.parseInt(name) + 100);

                write_script(new File(file.getAbsolutePath() + "/" + name + ".sm"), script);
            }
            index++;
        }

        index = 0;
        for (ModelPart part : model.getBodyParts()) {
            String script = ClientHelper.decompile_script(entity.getUuid(), part);
            if (!script.trim().equals("")) { 
                String name = ModelPartNames.getBodyName(entity, index).toLowerCase();
                write_script(new File(file.getAbsolutePath() + "/" + name + ".sm"), script);
            }
            index++;
        }
    }

    private static void write_script(File file, String script) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(script);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { writer.close(); } catch (IOException e) {}
        }
    }

    private static void write_settings(File file, PresetSettings settings) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            new Gson().toJson(settings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { writer.close(); } catch (IOException e) {}
        }
    }

    // Reading Preset
    public static void read_preset(File folder, LivingEntity entity, IAnimalModelMixin model) {
        if (model == null) model = (IAnimalModelMixin)PlatformUtils.getModel(entity);
        ClientHelper.reset_entity(entity.getUuid());
        
        File[] files = folder.listFiles();
        for (File file : files) {
            String name = file.getName();

            if (name.equalsIgnoreCase("preset.json")) {
                read_settings(file, entity);
                continue;
            }

            if (name.endsWith(".sm")) {
                name = name.substring(0, name.length()-3);
                String s = read_script(file);

                attach_script(name, s, entity, model);
            }
        }
    }

    private static void read_settings(File file, LivingEntity entity) {
        Gson gson = new Gson();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            PresetSettings settings = gson.fromJson(reader, PresetSettings.class);
            
            ScaleTypes.BASE.getScaleData(entity).setScale(settings.pehkui_scale);

            SkinHelper.setSkinSuffix(entity.getUuid(), settings.skin_suffix);
            SkinHelper.reloadSkins();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try { reader.close(); } catch (IOException e) {}
        }
    }

    private static String read_script(File file) {
        String output = "";

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            output = new String(fis.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { fis.close(); } catch (IOException e) {}
        }

        return output;
    }

    private static void attach_script(String part_id, String script, LivingEntity entity, IAnimalModelMixin model) {
        if (part_id.equalsIgnoreCase("root")) {
            ClientHelper.change_script(entity.getUuid(), null, -1, script);
            return;
        }

        ModelPartItem part = getModelPart(part_id, entity, model);
        ClientHelper.change_script(entity.getUuid(), part.part, part.id, script);
    }

    private static ModelPartItem getModelPart(String part_id, LivingEntity entity, IAnimalModelMixin model) {
        int part_int = PostfixOperation.isNumeric(part_id) ? Integer.parseInt(part_id) : -1;
        EntityParts parts = PCMClient.EntityPartNames.map.get(entity.getType().getUntranslatedName());
        
        int index = 100;
        for (ModelPart mPart : model.getHeadParts()) {
            if (part_int == -1 && parts.headParts.containsKey(index-100) && parts.headParts.get(index-100).equalsIgnoreCase(part_id)) 
                return new ModelPartItem(mPart, index);
            else if (part_int == index) return new ModelPartItem(mPart, index);
            index++;
        }

        index = 0;
        for (ModelPart mPart : model.getBodyParts()) {
            if (part_int == -1 && parts.bodyParts.containsKey(index) && parts.bodyParts.get(index).equalsIgnoreCase(part_id)) 
                return new ModelPartItem(mPart, index);
            else if (part_int == index) return new ModelPartItem(mPart, index);
            index++;
        }

        return null;
    }

    private static class ModelPartItem {
        public ModelPart part;
        public int id;

        public ModelPartItem(ModelPart _part, int _id) {
            this.part = _part; this.id = _id;
        }
    }

}
