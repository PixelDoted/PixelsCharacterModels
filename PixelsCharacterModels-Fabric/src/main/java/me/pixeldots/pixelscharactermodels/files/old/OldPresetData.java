package me.pixeldots.pixelscharactermodels.files.old;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.FileHelper;
import me.pixeldots.pixelscharactermodels.files.PresetSettings;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.text.Text;

@Deprecated
public class OldPresetData {

	public Map<String, PresetPartData> partData = new HashMap<String, PresetPartData>();
	public float GlobalScale = 1;
	public String skinSuffix = "";
	
	public static File toNew(File path) {
		Text warning_text = Text.of("Warning: Converting from Old Preset's can be inaccurate");
		PCMClient.minecraft.player.sendMessage(warning_text, false);
		OldPresetData data = (OldPresetData)FileHelper.read(path, OldPresetData.class);

		String absolute_path = path.getAbsolutePath();
		absolute_path = absolute_path.substring(0,absolute_path.length()-5);
		path = new File(absolute_path);
		path.mkdir();
		
		PresetSettings settings = new PresetSettings();
		settings.pehkui_scale = data.GlobalScale;
		settings.skin_suffix = data.skinSuffix;
		FileHelper.write(new File(absolute_path + File.separator + "preset.json"), settings);

		for (String key : data.partData.keySet()) {
			PresetPartData part = data.partData.get(key);
			FileHelper.write(new File(absolute_path + File.separator + key.toLowerCase() + ".sm"), part.getScript());
		}

		return path;
	}
	
	public class PresetPartData {
		public boolean Show;
		public OldMapVec3 scale;
		public OldMapVec3 pos;
		public OldMapVec3 rot;
		public List<PresetCubeData> cubes;
		public List<PresetMeshData> meshes;

		public String getScript() {
			StringBuilder script = new StringBuilder();
			script.append(Show ? "" : "cancel\n");
			if (pos != null) script.append("translate " + pos.X + " " + pos.Y + " " + pos.Z + "\n");
			//if (rot != null) script.append("rotate " + Math.toRadians(rot.X) + " " + Math.toRadians(rot.Y) + " " + Math.toRadians(rot.Z) + "\n");
			if (scale != null) script.append("scale " + scale.X + " " + scale.Y + " " + scale.Z + "\n");

			for (PresetCubeData cube : cubes) {
				cube.toScript(script);
			}
			for (PresetMeshData mesh : meshes) {
				mesh.toScript(script);
			}

			return script.toString();
		}
	}
	
	public class PresetCubeData {
		public String name;
		public OldMapVec3 pos;
		public OldMapVec3 size;
		public OldMapVec2 uvOffset;
		public String textureID;

		public void toScript(StringBuilder script) {
			if (pos != null) script.append("translate " + (pos.X/16) + " " + (pos.Y/16) + " " + (pos.Z/16) + "\n");
			if (size != null) script.append("scale " + (size.X/8) + " " + (size.Y/8) + " " + (size.Z/8) + "\n");

			script.append(PCMUtils.cubeToString(String.valueOf(uvOffset.X/64), String.valueOf(uvOffset.Y/64), "1", "1"));

			if (size != null) script.append("scale " + (1/(size.X/8)) + " " + (1/(size.Y/8)) + " " + (1/(size.Z/8)) + "\n");
			if (pos != null) script.append("translate " + -(pos.X/16) + " " + -(pos.Y/16) + " " + -(pos.Z/16) + "\n");
		}
	}
	
	public class PresetMeshData {
		public String name;
		public OldMapVec3 pos;
		public OldMapVec3 size;
		public OldMapVec2 uvOffset;
		public String meshID;
		public String textureID;

		public void toScript(StringBuilder script) {
			if (pos != null) script.append("translate " + -(pos.X/16) + " " + -(pos.Z/16) + " " + -(pos.Y/16) + "\n");
			if (size != null) script.append("scale " + size.X + " " + size.Y + " " + size.Z + "\n");
			script.append("rotate 0 3.14 3.14\n");

			script.append(PCMUtils.meshToString(meshID.replace(" ", "_") + ".obj", String.valueOf(uvOffset.X/64), String.valueOf(uvOffset.Y/64), "1", "1"));

			script.append("\nrotate 0 3.14 3.14\n");
			if (size != null) script.append("scale " + (1/size.X) + " " + (1/size.Y) + " " + (1/size.Z) + "\n");
			if (pos != null) script.append("translate " + (pos.X/16) + " " + (pos.Z/16) + " " + (pos.Y/16) + "\n");
		}
	}
	
}
