package me.pixeldots.pixelscharactermodels.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3f;

public class MeshReader {

    private static Map<String, MeshTypeReader> meshReaders = new HashMap<>(); // all Mesh Readers

    static { addMeshReader("obj", new OBJMeshReader()); }

    // add custom MeshReader
    public static void addMeshReader(String file_extension, MeshTypeReader reader) {
        meshReaders.put(file_extension.toLowerCase(), reader);
    }

    // read mesh from path
    public static Mesh readMesh(String path) {
        return readMesh(Paths.get("./PCM/Models", path).toFile());
    }

    // read mesh from file
    public static Mesh readMesh(File path) {
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(path);
            return meshReaders.get(getFileExtension(path.getName())).run(reader.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { reader.close(); } catch (IOException e) {}
        }
        return null;
    }

    // get file extension from file name
    public static String getFileExtension(String name) {
        String[] s = name.split("\\.");
        return s.length == 0 ? name : s[s.length-1].toLowerCase();
    }

    public static class Mesh {
        public List<Vec3f> vertices = new ArrayList<>();
        public List<Vec2f> uvs = new ArrayList<>();
        public List<Vec3f> normals = new ArrayList<>();
        public List<Point[]> faces = new ArrayList<>();
    }

    public static class Point {
        public int vertex; // vertex index
        public int uv; // uv index
        public int normal; // normal index

        public Point() {}
        public Point(String[] s) {
            vertex = PCMUtils.getInt(s[0]);
            uv = PCMUtils.getInt(s[1]);
            normal = PCMUtils.getInt(s[2]);
        }
    }

    public static interface MeshTypeReader {
        Mesh run(byte[] mesh);
    }
}
