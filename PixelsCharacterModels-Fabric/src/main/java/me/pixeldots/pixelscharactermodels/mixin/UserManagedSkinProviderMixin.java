package me.pixeldots.pixelscharactermodels.mixin;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lain.lib.SharedPool;
import lain.mods.skins.api.interfaces.IPlayerProfile;
import lain.mods.skins.api.interfaces.ISkin;
import lain.mods.skins.impl.Shared;
import lain.mods.skins.impl.SkinData;
import lain.mods.skins.impl.fabric.ImageUtils;
import lain.mods.skins.providers.UserManagedSkinProvider;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;

@Mixin(UserManagedSkinProvider.class)
public abstract class UserManagedSkinProviderMixin {

    /*@Accessor("_dirN")
    public File get_dirN() { return null; }

    @Accessor("_dirU")
    public File get_dirU() { return null; }

    @Accessor("_filter")
    public Function<ByteBuffer, ByteBuffer> get_filter() { return null; }

    @Inject(method = "getSkin", at = @At("RETURN"))
    public void getSkin(IPlayerProfile profile, CallbackInfoReturnable<ISkin> info) {
        Function<ByteBuffer, ByteBuffer> _filter = get_filter();

        SkinData skin = new SkinData();
        if (_filter != null)
            skin.setSkinFilter(_filter);
        SharedPool.execute(() -> {
            byte[] data = null;
            
            String suffix = "";
            if (PixelsCharacterModels.PlayerSkinList.containsKey(profile.getPlayerID())) 
            	suffix = PixelsCharacterModels.PlayerSkinList.get(profile.getPlayerID());
            
            if (suffix.equals("")) return;
            if (!Shared.isOfflinePlayer(profile.getPlayerID(), profile.getPlayerName()))
                data = readFile(get_dirU(), "%s"+suffix+".png", profile.getPlayerID().toString().replaceAll("-", ""));
            if (data == null && !Shared.isBlank(profile.getPlayerName()))
                data = readFile(get_dirN(), "%s"+suffix+".png", profile.getPlayerName());
            if (data != null)   
                skin.put(data, ImageUtils.judgeSkinType(data));
        });
        info.setReturnValue(skin);
    }

    @Invoker("readFile")
    public byte[] readFile(File dir, String filename, Object... args) {
        return null;
    }*/

}
