package me.pixeldots.pixelscharactermodels.skin;

import java.nio.file.Paths;
import java.util.UUID;

import lain.mods.skins.api.SkinProviderAPI;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;

public class SkinHelper {
    
    public static void reloadSkins() {
        SkinProviderAPI.SKIN.clearProviders();
        SkinProviderAPI.CAPE.clearProviders();
        
        SkinProviderAPI.SKIN.registerProvider(new PCMSkinProvider(Paths.get(".", "PCM")));
        SkinProviderAPI.CAPE.registerProvider(new PCMCapeProvider(Paths.get(".", "PCM")));
    }

    public static void setSkinSuffix(UUID uuid, String suffix) {
        PixelsCharacterModels.PlayerSkinList.put(uuid, suffix);
    }

}
