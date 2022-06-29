package me.pixeldots.pixelscharactermodels;

import java.util.HashMap;
import java.util.Map;

import me.pixeldots.pixelscharactermodels.model.ModelPartData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerData {

    public PlayerEntity entity;
    public PlayerEntityModel<?> model;
    public String skinSuffix;

    public Map<ModelPart, ModelPartData> limbs = new HashMap<>();

    public PlayerData() {}
    public PlayerData(String _skinSuffix) {
        this.skinSuffix = _skinSuffix;
    }
    public PlayerData(PlayerEntity _entity, PlayerEntityModel<?> _model) {
        this.entity = _entity;
        this.model = _model;
    }

    public ModelPartData getLimb(ModelPart limb, PlayerEntity player, PlayerEntityModel<?> model) {
        if (!limbs.containsKey(limb))
            limbs.put(limb, new ModelPartData(player.getUuid()));
        
        return limbs.get(limb);
    }
    
}
