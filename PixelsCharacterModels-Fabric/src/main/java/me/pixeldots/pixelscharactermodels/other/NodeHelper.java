package me.pixeldots.pixelscharactermodels.other;

public class NodeHelper {
    
    public static String cubeToString(Node node) {
        // uv
        String tu = getArgFloat(node.args, 0);
        String tv = getArgFloat(node.args, 1);
        String tw = getArgFloat(node.args, 2);
        String th = getArgFloat(node.args, 3);

        return PCMUtils.cubeToString(tu, tv, tw, th);
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
