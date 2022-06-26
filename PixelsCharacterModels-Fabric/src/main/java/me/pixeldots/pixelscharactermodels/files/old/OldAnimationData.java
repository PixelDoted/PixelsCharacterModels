package me.pixeldots.pixelscharactermodels.files.old;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.pixeldots.pixelscharactermodels.files.AnimationFile;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.files.FileHelper;

@Deprecated
public class OldAnimationData {

	public String name = "";
	
	public OldMapVec3 playerTransform = new OldMapVec3();
	public List<OldMapVec3> LimbRotations = new ArrayList<OldMapVec3>();
	public List<OldMapVec3> LimbPivots = new ArrayList<OldMapVec3>();
	public List<String> LimbIDs = new ArrayList<String>();

	public static AnimationFile toNew(File file) {
		OldAnimationData data = (OldAnimationData)FileHelper.read(file, OldAnimationData.class);
		AnimationFile anim = new AnimationFile();
		
		anim.lerp = false;
		anim.loop = false;
		AnimationFile.Frame frame = anim.frames.get(0);
		frame.script = data.playerTransform != null ? ("translate " + data.playerTransform.X + " -" + data.playerTransform.Y + " " + data.playerTransform.Z) : "";

		for (int i = 0; i < data.LimbIDs.size(); i++) {
			OldMapVec3 rot = data.LimbRotations.size() > i ? data.LimbRotations.get(i) : new OldMapVec3();
			OldMapVec3 pos = data.LimbPivots.size() > i ? data.LimbPivots.get(i) : new OldMapVec3();

			String script = pos != null ? ("translate " + pos.X + " -" + pos.Y + " " + pos.Z + "\n") : "";
			if (rot != null) script += "angle " + rot.X + " " + rot.Y + " " + rot.Z;

			frame.parts.put(data.LimbIDs.get(i).toLowerCase(), script);
		}

		AnimationHelper.write(new File(file.getParent() + File.separator + "conv_" + file.getName()), anim);
		return anim;
	}
	
}
