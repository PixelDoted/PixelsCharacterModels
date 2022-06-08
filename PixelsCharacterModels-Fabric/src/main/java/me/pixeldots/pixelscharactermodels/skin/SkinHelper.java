package me.pixeldots.pixelscharactermodels.skin;

import java.util.UUID;

import lain.mods.skins.api.SkinProviderAPI;
import lain.mods.skins.impl.PlayerProfile;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.entity.player.PlayerEntity;

public class SkinHelper {
    
    public static void setSkinSuffix(UUID uuid, String suffix) {
        PixelsCharacterModels.PlayerSkinList.put(uuid, suffix);
    }

    public static void ReloadSkins() {
    	PixelsCharacterModels.minecraft.player.sendChatMessage("Reloading Skins");
    	for (PlayerEntity player : PixelsCharacterModels.minecraft.world.getPlayers()) {
            SkinProviderAPI.SKIN.getSkin(PlayerProfile.wrapGameProfile(player.getGameProfile()));
            SkinProviderAPI.CAPE.getSkin(PlayerProfile.wrapGameProfile(player.getGameProfile()));
            PixelsCharacterModels.minecraft.player.sendChatMessage("Reloaded skin for " + player.getDisplayName().asString());
        }
    	PixelsCharacterModels.minecraft.player.sendChatMessage("Reloaded Skins");
    }

}
