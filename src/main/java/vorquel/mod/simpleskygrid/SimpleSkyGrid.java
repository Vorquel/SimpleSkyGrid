package vorquel.mod.simpleskygrid;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;
import org.apache.logging.log4j.Logger;
import vorquel.mod.simpleskygrid.event.SpawnFixer;
import vorquel.mod.simpleskygrid.helper.Config;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.WorldProviderSkyGrid;

import java.util.Hashtable;

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
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new SpawnFixer());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Ref.postInit();
        createWorldProviders();
    }

    private void createWorldProviders() {
        int idTrue = Integer.MAX_VALUE;
        while(!DimensionManager.registerProviderType(++idTrue, WorldProviderSkyGrid.LoadedTrue.class, true));
        int idFalse = idTrue;
        while(!DimensionManager.registerProviderType(++idFalse, WorldProviderSkyGrid.LoadedFalse.class, false));
        for(int dim : Config.getDimensions()) {
            if(DimensionManager.isDimensionRegistered(dim)) {
                Hashtable<Integer, Integer> dimensions = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "dimensions");
                int id = dimensions.get(dim);
                DimensionManager.unregisterDimension(dim);
                Hashtable<Integer, Class<? extends WorldProvider>> providers = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "providers");
                Hashtable<Integer, Boolean> spawnSettings = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "spawnSettings");
                Ref.setWorldProviderProxy(dim, providers.get(id));
                if(spawnSettings.get(id))
                    DimensionManager.registerDimension(dim, idTrue);
                else
                    DimensionManager.registerDimension(dim, idFalse);
            } else {
                Ref.setWorldProviderProxy(dim, WorldProviderSurface.class);
                DimensionManager.registerDimension(dim, idFalse);
            }
        }
    }
}
