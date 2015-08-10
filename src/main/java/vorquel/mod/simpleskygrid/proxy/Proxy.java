package vorquel.mod.simpleskygrid.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import vorquel.mod.simpleskygrid.config.CommandReloadConfigs;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.event.SpawnFixer;
import vorquel.mod.simpleskygrid.helper.Ref;

public class Proxy {

    public void preInit() {
        Config.loadConfigs();
    }

    public void init() {
        FMLCommonHandler.instance().bus().register(new SpawnFixer());
    }

    public void postInit() {
        Ref.postInit();
    }

    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadConfigs());
    }
}
