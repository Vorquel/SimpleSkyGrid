package vorquel.mod.simpleskygrid;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import vorquel.mod.simpleskygrid.helper.Config;
import vorquel.mod.simpleskygrid.helper.Ref;

@Mod(modid = "SimpleSkyGrid", name = "Simple Sky Grid", version = "@MOD_VERSION@")
public class SimpleSkyGrid {

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        Config.init(event.getSuggestedConfigurationFile());
        Ref.preInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Ref.postInit();
    }
}
