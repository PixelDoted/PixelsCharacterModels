package me.pixeldots.pixelscharactermodels;

import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerData {

    public PlayerEntity entity;
    public PlayerEntityModel<?> model;
    public String skinSuffix;

    public PlayerData() {}
    public PlayerData(String _skinSuffix) {
        this.skinSuffix = _skinSuffix;
    }
    public PlayerData(PlayerEntity _entity, PlayerEntityModel<?> _model) {
        this.entity = _entity;
        this.model = _model;
    }
    
}
