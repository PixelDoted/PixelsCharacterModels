package me.pixeldots.pixelscharactermodels.other;

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

}
