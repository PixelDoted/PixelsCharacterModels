package me.edoren.skin_changer.server;

import me.edoren.skin_changer.server.providers.CrafatarCapeProvider;
import me.edoren.skin_changer.server.providers.CrafatarSkinProvider;
import net.minecraft.server.MinecraftServer;

public class ServerController {
    private static ServerController singleInstance = null;

    public static ServerController GetInstance() {
        if (singleInstance == null)
            singleInstance = new ServerController();

        return singleInstance;
    }

    private ServerController() {
    }

    public void initialize(MinecraftServer server) {
        SkinsCommand.register(server.getCommandManager().getDispatcher());
        //File file = server.getWorld(World.field_234918_g_).//.getSaveHandler().getWorldDirectory();
        //String saveFolder = Paths.get(file.getPath(), "skin_changer").toString();

        SkinProviderController.GetInstance().initialize();//saveFolder);

        SkinProviderController.GetInstance().registerSkinProvider(new CrafatarSkinProvider());

        SkinProviderController.GetInstance().registerCapeProvider(new CrafatarCapeProvider());
    }
}
