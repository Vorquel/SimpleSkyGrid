package vorquel.mod.simpleskygrid.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import vorquel.mod.simpleskygrid.config.CommandReloadConfigs;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.event.SpawnFixer;
import vorquel.mod.simpleskygrid.helper.JSON2NBT;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.generated.localizer.LocalizerThaumcraftTileNode;

public class Proxy {

    public void preInit() {
        Config.loadConfigs();
    }

    public void init() {
        FMLCommonHandler.instance().bus().register(new SpawnFixer());
        if(Loader.isModLoaded("Thaumcraft"))
            JSON2NBT.addBlockLocalizer(new LocalizerThaumcraftTileNode());
    }

    public void postInit() {
        Ref.postInit();
    }

    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadConfigs());
    }
}
