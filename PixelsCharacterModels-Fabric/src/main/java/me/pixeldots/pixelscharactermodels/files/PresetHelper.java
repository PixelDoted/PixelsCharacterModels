package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.old.OldPresetData;
import me.pixeldots.pixelscharactermodels.network.ClientNetwork;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import me.pixeldots.pixelscharactermodels.other.ModelPartNames.EntityParts;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.script.PostfixOperation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public class PresetHelper {
    
    // Writing Preset
    public static void write_preset(File file, LivingEntity entity, EntityModel<?> model) {
        file.mkdirs(); // create preset folder

        // add settings to the preset folder
        PresetSettings settings = new PresetSettings();
        settings.pehkui_scale = PCMUtils.getPehkuiScale(entity);
        settings.skin_suffix = PCMClient.PlayerSkinList.get(entity.getUuid());
        FileHelper.write(new File(file.getAbsolutePath() + "/preset.json"), settings);

        // add scripts to the preset folder
        String root = ClientHelper.decompile_script(entity.getUuid(), null);
        if (!root.trim().equals("")) FileHelper.write(new File(file.getAbsolutePath() + "/root.sm"), root);

        int index = 0;
        for (ModelPart part : PlatformUtils.getHeadParts(model)) { // decompile modelparts
            String script = ClientHelper.decompile_script(entity.getUuid(), part);
            if (!script.trim().equals("")) { 
                String name = ModelPartNames.getHeadName(entity, index).toLowerCase();
                if (PostfixOperation.isNumeric(name)) name = "" + (Integer.parseInt(name) + 100);

                FileHelper.write(new File(file.getAbsolutePath() + "/" + name + ".sm"), script);
            }
            index++;
        }

        index = 0;
        for (ModelPart part : PlatformUtils.getBodyParts(model)) { // decompile modelparts
            String script = ClientHelper.decompile_script(entity.getUuid(), part);
            if (!script.trim().equals("")) { 
                String name = ModelPartNames.getBodyName(entity, index).toLowerCase();
                FileHelper.write(new File(file.getAbsolutePath() + "/" + name + ".sm"), script);
            }
            index++;
        }
    }

    // Reading Preset
    // read preset
    public static void read_preset(File folder, LivingEntity entity, EntityModel<?> model) {
        if (folder.getName().endsWith(".json"))
            folder = OldPresetData.toNew(folder);

        folder.mkdirs();
        if (model == null) model = PlatformUtils.getModel(entity);
        ClientHelper.reset_entity(entity.getUuid()); // reset entity scripts
        
        File[] files = folder.listFiles();
        for (File file : files) {
            String name = file.getName();

            if (name.equalsIgnoreCase("preset.json")) { // read settings if the file is "preset.json"
                read_settings(file, entity);
                continue;
            }

            if (name.endsWith(".sm")) { // read script if the file ends with ".sm"
                name = name.substring(0, name.length()-3);
                String s = FileHelper.read(file);

                attach_script(name, s, entity, model);
            }
        }
    }

    // read settings from file
    private static void read_settings(File file, LivingEntity entity) {
        PresetSettings settings = (PresetSettings)FileHelper.read(file, PresetSettings.class);
        
        ClientNetwork.send_pehkui_scale(entity, settings.pehkui_scale); // set pehkui sccale

        UUID uuid = entity.getUuid();
        ClientNetwork.send_skin_suffix(uuid, settings.skin_suffix);
        SkinHelper.setSkinSuffix(uuid, settings.skin_suffix); // set skin suffix
        SkinHelper.reloadSkins(); // reload all skins
    }

    // attach script to entity
    private static void attach_script(String part_id, String script, LivingEntity entity, EntityModel<?> model) {
        if (part_id.equalsIgnoreCase("root")) {
            ClientHelper.change_script(entity.getUuid(), null, -1, script);
            return;
        }

        ModelPartItem part = getModelPart(part_id, entity, model);
        if (part == null) return;
        
        ClientHelper.change_script(entity.getUuid(), part.part, part.id, script);
    }

    // get modelpart from part_id
    private static ModelPartItem getModelPart(String part_id, LivingEntity entity, EntityModel<?> model) {
        int part_int = PostfixOperation.isNumeric(part_id) ? Integer.parseInt(part_id) : -1;
        EntityParts parts = null;
        String entity_name = entity.getType().getUntranslatedName();

        if (PCMClient.EntityPartNames.map.containsKey(entity_name))
            parts = PCMClient.EntityPartNames.map.get(entity_name);
        
        int index = 100;
        for (ModelPart mPart : PlatformUtils.getHeadParts(model)) {
            if (part_int == -1 && parts != null && parts.headParts.containsKey(index-100) && parts.headParts.get(index-100).equalsIgnoreCase(part_id)) 
                return new ModelPartItem(mPart, index);
            else if (part_int == index) return new ModelPartItem(mPart, index);
            index++;
        }

        index = 0;
        for (ModelPart mPart : PlatformUtils.getBodyParts(model)) {
            if (part_int == -1 && parts != null && parts.bodyParts.containsKey(index) && parts.bodyParts.get(index).equalsIgnoreCase(part_id)) 
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
