package me.PixelDots.PixelsCharacterModels.client;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHook {

    @SubscribeEvent
    public void leaveServer(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    }

    /*@SubscribeEvent
    public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
    }*/

    /*@SubscribeEvent
    public void doRender(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ModelManager.Skin.loadQueuedSkins();
            ModelManager.Model.loadQueuedModels();
            ModelManager.Elytra.loadQueuedElytras();
        }
        PlayerEntity client = Minecraft.getInstance().player;
        if(client != null) {
            ModelManager.Skin.checkSkin((AbstractClientPlayerEntity) client);
            ModelManager.Model.checkModel((AbstractClientPlayerEntity) client);
            ModelManager.Elytra.checkElytra((AbstractClientPlayerEntity) client);
        }
    }*/

    /*@SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Pre event) {
        PlayerEntity client = Minecraft.getInstance().player;
        if(event.getPlayer() instanceof AbstractClientPlayerEntity && event.getPlayer() != client) {
            ModelManager.Skin.checkSkin((AbstractClientPlayerEntity) event.getPlayer());
            ModelManager.Model.checkModel((AbstractClientPlayerEntity) event.getPlayer());
            ModelManager.Elytra.checkElytra((AbstractClientPlayerEntity)event.getPlayer());
        }
    }*/

}