package me.pixeldots.pixelscharactermodels.other;

public class NodeHelper {
    
    public static String cubeToString(Node node) {
        // uv
        String[] A = new String[] { getArgFloat(node.args, 0), getArgFloat(node.args, 1), getArgFloat(node.args, 2), getArgFloat(node.args, 3) };
        String[] B = new String[] { getArgFloat(node.args, 4), getArgFloat(node.args, 5), getArgFloat(node.args, 6), getArgFloat(node.args, 7) };
        String[] C = new String[] { getArgFloat(node.args, 8), getArgFloat(node.args, 9), getArgFloat(node.args, 10), getArgFloat(node.args, 11) };
        String[] D = new String[] { getArgFloat(node.args, 12), getArgFloat(node.args, 13), getArgFloat(node.args, 14), getArgFloat(node.args, 15) };
        String[] E = new String[] { getArgFloat(node.args, 16), getArgFloat(node.args, 17), getArgFloat(node.args, 18), getArgFloat(node.args, 19) };
        String[] F = new String[] { getArgFloat(node.args, 20), getArgFloat(node.args, 21), getArgFloat(node.args, 22), getArgFloat(node.args, 23) };

        return PCMUtils.cubeToString(new String[][] {A, B, C, D, E, F});
    }
    public static String meshToString(Node node) {
        String meshID = getArgFloat(node.args, 0);
        String tu = getArgFloat(node.args, 1);
        String tv = getArgFloat(node.args, 2);
        String tw = getArgFloat(node.args, 3);
        String th = getArgFloat(node.args, 4);

        return PCMUtils.meshToString(meshID, tu, tv, tw, th);
    }

    private static String getArgFloat(String[] array, int index) {
        if (index < array.length && index >= 0) {
            return array[index];
        }
        return "0";
    }

}
