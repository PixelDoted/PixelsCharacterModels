package me.pixeldots.offlinereload.mixins;

import org.spongepowered.asm.mixin.Mixin;
import lain.mods.skins.providers.UserManagedSkinProvider;

@Mixin(UserManagedSkinProvider.class)
public abstract class UserManagedSkinProviderMixin {

    /*@Shadow public File _dirN;
    @Shadow public File _dirU;
    @Shadow public Function<ByteBuffer, ByteBuffer> _filter;

    @Inject(method = "getSkin", at = @At("RETURN"))
    public void getSkin(IPlayerProfile profile, CallbackInfoReturnable<ISkin> info) {
        SkinData skin = new SkinData();
        if (_filter != null)
            skin.setSkinFilter(_filter);
        SharedPool.execute(() -> {
            byte[] data = null;
            
            String suffix = "";
            if (PixelsCharacterModels.PlayerDataList.containsKey(profile.getPlayerID())) 
            	suffix = PixelsCharacterModels.PlayerDataList.get(profile.getPlayerID()).skinSuffix;
            
            if (suffix.equals("")) return;
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
