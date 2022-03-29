package me.edoren.skin_changer.common.models;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class PlayerModel {
    private String name; // Player Name
    private String uuid; // Player UUID

    public PlayerModel(GameProfile profile) {
    	try {
	        this.name = profile.getName();
	        this.uuid = profile.getId().toString();
    	} catch (Exception e) {
    		this.name = null;
    		this.uuid = null;
    	}
    }

    public PlayerModel(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return uuid;
    }

    @Override
    public int hashCode() {
    	if (name != null) {
    		return name.toLowerCase().hashCode();
    	} else {
    		return 0;
    	}
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        PlayerModel other = (PlayerModel) obj;
        if (this.uuid != null && this.name != null) {
        	return this.uuid.toLowerCase().equals(other.uuid.toLowerCase()) || this.name.toLowerCase().equals(other.name.toLowerCase());
        } else {
        	return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", name, uuid);
    }

    public GameProfile toGameProfile() {
        return new GameProfile(UUID.fromString(uuid), name);
    }
}
