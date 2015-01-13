package vorquel.mod.simpleskygrid;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import org.apache.logging.log4j.Logger;
import vorquel.mod.simpleskygrid.event.SpawnFixer;
import vorquel.mod.simpleskygrid.helper.Config;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.WorldProviderSkyGrid;

import java.util.Hashtable;

@Mod(modid = "SimpleSkyGrid", name = "Simple Sky Grid", version = "@MOD_VERSION@", dependencies = "after: YUNoMakeGoodMap")
public class SimpleSkyGrid {

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        Config.init(event.getSuggestedConfigurationFile());
        Ref.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new SpawnFixer());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Ref.postInit();
        createWorldProviders();
    }

    private void createWorldProviders() {
        Hashtable<Integer, Class<? extends WorldProvider>> providers = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "providers");
        WorldProviderSkyGrid.Surface.setOtherType(providers.get(0));
        WorldProviderSkyGrid.Hell.setOtherType(providers.get(-1));
        WorldProviderSkyGrid.End.setOtherType(providers.get(1));
        providers.put(0, WorldProviderSkyGrid.Surface.class);
        providers.put(-1, WorldProviderSkyGrid.Hell.class);
        providers.put(1, WorldProviderSkyGrid.End.class);
    }
}
