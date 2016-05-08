package vorquel.mod.simpleskygrid.proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.CommandReloadConfigs;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.entity.EntityStasis;
import vorquel.mod.simpleskygrid.event.SpawnFixer;
import vorquel.mod.simpleskygrid.helper.Ref;

public class Proxy {

    public void preInit() {
        Config.loadConfigs();
        EntityRegistry.registerModEntity(EntityStasis.class, "stasis", 0, SimpleSkyGrid.instance, 80, Integer.MAX_VALUE, false);
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
