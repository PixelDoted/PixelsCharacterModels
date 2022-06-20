package me.pixeldots.pixelscharactermodels.other;

import me.pixeldots.pixelscharactermodels.PCMMain;

public class PCMUtils {
    
    // get Float from String
    public static float getFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {}
        return 0;
    }
    
    // get Integer from String
    public static int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {}
        return 0;
    }

    public static float RadiansOrDegress(float input) {
        return PCMMain.settings.radians_instead_of_degress ? input : (float)Math.toDegrees(input);
    }

    public static String RadiansOrDegressToString(float input) {
        return String.valueOf(PCMMain.settings.radians_instead_of_degress ? input : (float)Math.toRadians(input));
    }

}
