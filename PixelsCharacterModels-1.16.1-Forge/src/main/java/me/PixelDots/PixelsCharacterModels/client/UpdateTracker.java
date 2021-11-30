package me.PixelDots.PixelsCharacterModels.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import me.PixelDots.PixelsCharacterModels.util.Reference;
import me.PixelDots.PixelsCharacterModels.util.Utillities;

public class UpdateTracker 
{
	
	private String DownloadLink = "https://www.curseforge.com/minecraft/mc-mods/pixels-character-models/files";
	private String TrackerIdentifyer = "1.15.2";
	private String Version = "Update Tracker is disabled";
	
	public boolean hasUpdate() {
		String foundVersion = getTrackerVersion();
		if (foundVersion != "") {
			if (!(Utillities.isNumeric(foundVersion.replace(".", "")))) return false;
			if (!(Utillities.isNumeric(Reference.VERSION.replace("v", "").replace(".", "")))) return false;
			if (Float.parseFloat(foundVersion) > Float.parseFloat(Reference.VERSION.replace("v", ""))) {
				Version = foundVersion;
				return true;
			}
		}
		return false;
	}
	
	public String getVersion() {
		return Version;
	}
	
	public String getTrackerVersion() {
		URL url = null;
		try {
            url = new URL("https://raw.githubusercontent.com/PixelDoted/MyUpdateTrackers/master/PCM");
            URLConnection uc;
            uc = url.openConnection();

            @SuppressWarnings("unused")
			List<String> list = new ArrayList<String>();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line = null;
           	Object[] s = reader.lines().toArray();
           	for (int i = 0; i < s.length; i++) {
           		if (s[i].toString().contains(TrackerIdentifyer)) {
           			line = s[i].toString();
           			break;
           		}
           	}
            line = line.replace(TrackerIdentifyer + ": ", "");
            return line;

        } catch (IOException e) {
            System.out.println("There was a problem well checking for an update");
            return "";

        }
	}
	
	public String getLink() {
		return DownloadLink;
	}
	
}
