package me.pixeldots.pixelscharactermodels.files;

import me.pixeldots.pixelscharactermodels.files.MeshReader.Mesh;
import me.pixeldots.pixelscharactermodels.files.MeshReader.MeshTypeReader;
import me.pixeldots.pixelscharactermodels.files.MeshReader.Point;
import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3f;

public class OBJMeshReader implements MeshTypeReader {
    
    @Override
    public Mesh run(byte[] bytes) {
        String[] lines = new String(bytes).split("\n");
        Mesh mesh = new Mesh();
        for (String line : lines) {
            String[] args = line.split(" ");
            if (args[0].equals("v")) { // add vertex
                mesh.vertices.add(new Vec3f(PCMUtils.getFloat(args[1]), PCMUtils.getFloat(args[2]), PCMUtils.getFloat(args[3])));
            } else if (args[0].equals("vt")) { // add vertex uv
                mesh.uvs.add(new Vec2f(PCMUtils.getFloat(args[1]), 1-PCMUtils.getFloat(args[2])));
            } else if (args[0].equals("vn")) { // add vertex normal
                mesh.normals.add(new Vec3f(PCMUtils.getFloat(args[1]), PCMUtils.getFloat(args[2]), PCMUtils.getFloat(args[3])));
            } else if (args[0].equals("f")) { // add face/quad
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

}
