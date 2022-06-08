package me.pixeldots.pixelscharactermodels.skin;

import java.io.File;
import java.nio.file.Path;

import lain.lib.SharedPool;
import lain.mods.skins.api.interfaces.IPlayerProfile;
import lain.mods.skins.api.interfaces.ISkin;
import lain.mods.skins.api.interfaces.ISkinProvider;
import lain.mods.skins.impl.Shared;
import lain.mods.skins.impl.SkinData;
import lain.mods.skins.impl.fabric.ImageUtils;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;

public class PCMSkinProvider implements ISkinProvider {

    public File _dirN;
    public File _dirU;

    public PCMSkinProvider(Path workDir) {
        _dirN = new File(workDir.toFile(), "skins");
        _dirN.mkdirs();
        _dirU = new File(_dirN, "uuid");
        _dirU.mkdirs();
    }

    @Override
    public ISkin getSkin(IPlayerProfile profile) {
        SkinData skin = new SkinData();
        skin.setSkinFilter(ImageUtils::legacyFilter);
        SharedPool.execute(() -> {
            byte[] data = null;
            
            String suffix = "";
            if (PixelsCharacterModels.PlayerSkinList.containsKey(profile.getPlayerID())) 
            	suffix = PixelsCharacterModels.PlayerSkinList.get(profile.getPlayerID());

            if (!Shared.isOfflinePlayer(profile.getPlayerID(), profile.getPlayerName()))
                data = readFile(_dirU, "%s"+suffix+".png", profile.getPlayerID().toString().replaceAll("-", ""));
            if (data == null && !Shared.isBlank(profile.getPlayerName()))
                data = readFile(_dirN, "%s"+suffix+".png", profile.getPlayerName());
            if (data != null)   
                skin.put(data, ImageUtils.judgeSkinType(data));
        });
        
        return skin;
    }

    private byte[] readFile(File dir, String filename) {
        byte[] contents;
        if ((contents = Shared.readFile(new File(dir, filename), null, null)) != null && ImageUtils.validateData(contents))
            return contents;
        return null;
    }

    private byte[] readFile(File dir, String filename, Object... args) {
        return readFile(dir, String.format(filename, args));
    }
    
}