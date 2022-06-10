package me.pixeldots.pixelscharactermodels.other;

import java.util.HashMap;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.PCMClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class ModelPartNames {

    public Map<String, EntityParts> map = new HashMap<>();

    public ModelPartNames() {
        EntityParts data = new EntityParts();
        data.addHead(0, "Head"); data.addBody(0, "Body");
        data.addBody(1, "RightArm"); data.addBody(2, "LeftArm");
        data.addBody(3, "RightLeg"); data.addBody(4, "LeftLeg");
        map.put(EntityType.PLAYER.getUntranslatedName(), data);
    }

    public static String getHeadName(Entity entity, int part) {
        return getHeadName(entity.getType().getUntranslatedName(), part);
    }
    public static String getHeadName(String name, int part) {
        EntityParts parts = getMap(name);
        if (parts == null || !parts.headParts.containsKey(part)) return "" + part;
        return parts.headParts.get(part);
    }

    public static String getBodyName(Entity entity, int part) {
        return getBodyName(entity.getType().getUntranslatedName(), part);
    }
    public static String getBodyName(String name, int part) {
        EntityParts parts = getMap(name);
        if (parts == null || !parts.bodyParts.containsKey(part)) return "" + part;
        return parts.bodyParts.get(part);
    }

    private static EntityParts getMap(String name) {
        if (!PCMClient.EntityPartNames.map.containsKey(name)) return null;
        return PCMClient.EntityPartNames.map.get(name);
    }

    public class EntityParts {
        public Map<Integer, String> headParts = new HashMap<>();
        public Map<Integer, String> bodyParts = new HashMap<>();

        public EntityParts addHead(int id, String name) {
            this.headParts.put(id, name);
            return this;
        }
        public EntityParts addBody(int id, String name) {
            this.bodyParts.put(id, name);
            return this;
        }
    }

}
