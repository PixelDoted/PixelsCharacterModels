package me.pixeldots.pixelscharactermodels.mixin;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.pixeldots.pixelscharactermodels.files.FileHelper;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    
    @Inject(method = "loadWorld", at = @At("TAIL"))
    private void loadWorld(CallbackInfo info) {
        File folder = new File(((MinecraftServer)(Object)this).getSavePath(WorldSavePath.ROOT).toFile().getAbsolutePath() + File.separator + "pcm" + File.separator);

        if (!folder.exists()) return;
        File[] files = folder.listFiles();
        for (File file : files) {
            EntityData data = (EntityData)FileHelper.read(file, EntityData.class);
            if (data.script == null) data.script = "";
            
            ScriptedModelsMain.EntityData.put(UUID.fromString(file.getName()), data);
        }
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void save(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> info) {
        Map<UUID, EntityData> data_map = ScriptedModelsMain.EntityData;
        String path = ((MinecraftServer)(Object)this).getSavePath(WorldSavePath.ROOT).toFile().getAbsolutePath() + File.separator + "pcm" + File.separator;
        new File(path).mkdir();

        for (UUID key : data_map.keySet()) {
            EntityData data = data_map.get(key);
            File file = new File(path + key.toString());
            FileHelper.write(file, data);
        }
    }

}
