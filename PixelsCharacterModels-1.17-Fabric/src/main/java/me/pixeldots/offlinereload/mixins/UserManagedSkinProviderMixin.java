package me.pixeldots.offlinereload.mixins;

import java.io.File;
import java.nio.ByteBuffer;

import com.google.common.base.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lain.lib.SharedPool;
import lain.mods.skins.api.interfaces.IPlayerProfile;
import lain.mods.skins.api.interfaces.ISkin;
import lain.mods.skins.impl.Shared;
import lain.mods.skins.impl.SkinData;
import lain.mods.skins.impl.fabric.ImageUtils;
import lain.mods.skins.init.fabric.FabricOfflineSkins;
import lain.mods.skins.providers.UserManagedSkinProvider;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;

@Mixin(UserManagedSkinProvider.class)
public abstract class UserManagedSkinProviderMixin {

    /*@Shadow public File _dirN;
    @Shadow public File _dirU;
    @Shadow public Function<ByteBuffer, ByteBuffer> _filter;

    @Inject(method = "getSkin", at = @At("HEAD"))
    public void getSkin(IPlayerProfile profile, CallbackInfoReturnable<ISkin> info) {
        SkinData skin = new SkinData();
        if (_filter != null)
            skin.setSkinFilter(_filter);
        SharedPool.execute(() -> {
            byte[] data = null;
            
            String suffix = "";
            if (PixelsCharacterModels.PlayerDataList.containsKey(profile.getPlayerID())) 
            	suffix = PixelsCharacterModels.PlayerDataList.get(profile.getPlayerID()).skinSuffix;
            
            if (!Shared.isOfflinePlayer(profile.getPlayerID(), profile.getPlayerName()))
                data = readFile(_dirU, "%s"+suffix+".png", profile.getPlayerID().toString().replaceAll("-", ""));
            if (data == null && !Shared.isBlank(profile.getPlayerName()))
                data = readFile(_dirN, "%s"+suffix+".png", profile.getPlayerName());
            if (data != null)   
                skin.put(data, ImageUtils.judgeSkinType(data));
        });
        info.setReturnValue(skin);
    }

    @Shadow
    public abstract byte[] readFile(File dir, String filename, Object... args);*/

}
