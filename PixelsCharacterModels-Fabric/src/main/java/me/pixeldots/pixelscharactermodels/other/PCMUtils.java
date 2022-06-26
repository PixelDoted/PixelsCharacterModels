package me.pixeldots.pixelscharactermodels.other;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.MeshReader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3f;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class PCMUtils {

    public static float getPehkuiScale(LivingEntity entity) {
		return ScaleTypes.BASE.getScaleData(entity).getBaseScale();
	}

    public static void setPehkuiScale(LivingEntity entity, float scale) {
		ScaleData data = ScaleTypes.BASE.getScaleData(entity);
        data.setScale(scale); data.setBaseScale(scale);
	}
    
    public static float divide(float a, float b) {
        return a == 0 || b == 0 ? a : a/b;
    }

    // check if a String can be converted to a Float
    public static boolean isFloat(String s) {
        if (s == null) return false;

        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {}
        return false;
    }
    
    // get Float from String
    public static float getFloat(String s) {
        if (s == null) return 0;

        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {}
        return 0;
    }

    // check if a String can be converted to a Integer
    public static boolean isInt(String s) {
        if (s == null) return false;

        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {}
        return false;
    }
    
    // get Integer from String
    public static int getInt(String s) {
        if (s == null) return 0;

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

    public static String cubeToString(String[][] A) {
        // vertices
        String v1 = "v -4 -4 -4 n u 255 255 255 255";
        String v2 = "v 4 -4 -4 n u 255 255 255 255";
        String v3 = "v 4 4 -4 n u 255 255 255 255";
        String v4 = "v -4 4 -4 n u 255 255 255 255";
        String v5 = "v -4 -4 4 n u 255 255 255 255";
        String v6 = "v 4 -4 4 n u 255 255 255 255";
        String v7 = "v 4 4 4 n u 255 255 255 255";
        String v8 = "v -4 4 4 n u 255 255 255 255";

        // faces
        String f1 = (v3.replace("u", A[0][0] + " " + A[0][3]) + "\n" + v4.replace("u", A[0][0] + " " + A[0][1]) + "\n" + v1.replace("u", A[0][2] + " " + A[0][1]) + "\n" + v2.replace("u", A[0][2] + " " + A[0][3])).replace("n", "0 0 -1");
        String f2 = (v6.replace("u", A[1][2] + " " + A[1][3]) + "\n" + v5.replace("u", A[1][2] + " " + A[1][1]) + "\n" + v1.replace("u", A[1][0] + " " + A[1][1]) + "\n" + v2.replace("u", A[1][0] + " " + A[1][3])).replace("n", "0 -1 0");
        String f3 = (v7.replace("u", A[2][2] + " " + A[2][3]) + "\n" + v8.replace("u", A[2][2] + " " + A[2][1]) + "\n" + v5.replace("u", A[2][0] + " " + A[2][1]) + "\n" + v6.replace("u", A[2][0] + " " + A[2][3])).replace("n", "0 0 1");
        String f4 = (v7.replace("u", A[3][0] + " " + A[3][3]) + "\n" + v8.replace("u", A[3][0] + " " + A[3][1]) + "\n" + v4.replace("u", A[3][2] + " " + A[3][1]) + "\n" + v3.replace("u", A[3][2] + " " + A[3][3])).replace("n", "0 1 0");
        String f5 = (v5.replace("u", A[4][0] + " " + A[4][3]) + "\n" + v8.replace("u", A[4][0] + " " + A[4][1]) + "\n" + v4.replace("u", A[4][2] + " " + A[4][1]) + "\n" + v1.replace("u", A[4][2] + " " + A[4][3])).replace("n", "-1 0 0");
        String f6 = (v6.replace("u", A[5][2] + " " + A[5][1]) + "\n" + v7.replace("u", A[5][0] + " " + A[5][1]) + "\n" + v3.replace("u", A[5][0] + " " + A[5][3]) + "\n" + v2.replace("u", A[5][2] + " " + A[5][3])).replace("n", "1 0 0");
        
        String define = "define " + 6*4 + " cube uvs\n";
        String uvs = "";
        for (String[] s : A) {
            uvs += " " + s[0] + " " + s[1] + " " + s[2] + " " + s[3];
        }

        return define.replace("uvs", uvs.trim()) + f1 + "\n" + f2 + "\n" + f3 + "\n" + f4 + "\n" + f5 + "\n" + f6;
    }
    public static String meshToString(String meshID, String tu, String tv, String tw, String th) {
        MeshReader.Mesh mesh = MeshReader.readMesh(meshID);
        String output = "";
        int line_count = 0;
        if (mesh != null) {
            for (MeshReader.Point[] points : mesh.faces) {
                for (MeshReader.Point point : points) {
                    Vec3f vertex = mesh.vertices.get(point.vertex-1);
                    Vec2f uv = mesh.uvs.get(point.uv-1);
                    Vec3f normal = mesh.normals.get(point.normal-1);

                    output += "vertex " + vertex.getX() + " " + vertex.getY() + " " + vertex.getZ() + " ";
                    output += normal.getX() + " " + normal.getY() + " " + normal.getZ() + " ";
                    output += uv.x + " " + uv.y + " 255 255 255 255\n";
                    line_count++;
                }
            }
        }

        String define = "define " + line_count + " mesh \"" + meshID + "\" " + tu + " " + tv + " " + tw + " " + th + "\n";
        return define + output.trim();
    }

    public static Identifier getEntityTexture(LivingEntity entity) {
        return PCMClient.minecraft.getEntityRenderDispatcher().getRenderer(entity).getTexture(entity);
    }

}
