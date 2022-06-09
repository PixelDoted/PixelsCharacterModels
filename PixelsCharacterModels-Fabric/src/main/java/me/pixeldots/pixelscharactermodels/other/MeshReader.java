package me.pixeldots.pixelscharactermodels.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3f;

public class MeshReader {

    public static Mesh readMesh(String path) {
        return readMesh(Paths.get("./PCM/Models", path).toFile());
    }

    public static Mesh readMesh(File path) {
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(path);
            return readOBJ(new String(reader.readAllBytes()).split("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { reader.close(); } catch (IOException e) {}
        }
        return null;
    }

    public static Mesh readOBJ(String[] lines) {
        Mesh mesh = new Mesh();
        for (String line : lines) {
            String[] args = line.split(" ");
            if (args[0].equals("v")) {
                mesh.vertices.add(new Vec3f(getFloat(args[1]), getFloat(args[2]), getFloat(args[3])));
            } else if (args[0].equals("vt")) {
                mesh.uvs.add(new Vec2f(getFloat(args[1]), 1-getFloat(args[2])));
            } else if (args[0].equals("vn")) {
                mesh.normals.add(new Vec3f(getFloat(args[1]), getFloat(args[2]), getFloat(args[3])));
            } else if (args[0].equals("f")) {
                Point[] points = new Point[args.length-1 == 4 ? args.length-1 : args.length];
                for (int i = 0; i < points.length; i++) {
                    int ind = i+1 >= args.length ? args.length-1 : i+1;
                    points[i] = new Point(args[ind].split("/"));
                }
                mesh.faces.add(points);
            }
        }
        return mesh;
    }

    public static float getFloat(String v) {
        try {
            return Float.parseFloat(v);
        } catch (NumberFormatException e) {}
        return 0;
    }
    public static int getInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {}
        return 0;
    }

    public static class Mesh {
        public List<Vec3f> vertices = new ArrayList<>();
        public List<Vec2f> uvs = new ArrayList<>();
        public List<Vec3f> normals = new ArrayList<>();
        public List<Point[]> faces = new ArrayList<>();
    }

    public static class Point {
        public int vertex;
        public int uv;
        public int normal;

        public Point() {}
        public Point(String[] s) {
            vertex = getInt(s[0]);
            uv = getInt(s[1]);
            normal = getInt(s[2]);
        }
    }
}
