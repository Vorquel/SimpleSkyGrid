package vorquel.mod.simpleskygrid;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import vorquel.mod.simpleskygrid.config.CommandReloadConfigs;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.event.SpawnFixer;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.network.MessageClipboard;
import vorquel.mod.simpleskygrid.network.MessageHandlerClipboard;
import vorquel.mod.simpleskygrid.network.MessageHandlerIdentify;
import vorquel.mod.simpleskygrid.network.MessageIdentify;
import vorquel.mod.simpleskygrid.world.provider.ClassLoaderWorldProvider;
import vorquel.mod.simpleskygrid.world.provider.WorldProviderSurfaceAlt;

import java.util.Hashtable;

@Mod(modid = Ref.MOD_ID, name = "Simple Sky Grid", version = "@MOD_VERSION@")
public class SimpleSkyGrid {

    @Mod.Instance(Ref.MOD_ID)
    @SuppressWarnings("unused")
    public static SimpleSkyGrid instance;
    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        Log.setLogger(event.getModLog());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Ref.MOD_ID);
        network.registerMessage(MessageHandlerClipboard.class, MessageClipboard.class, 0, Side.CLIENT);
        network.registerMessage(MessageHandlerIdentify.class, MessageIdentify.class, 1, Side.SERVER);
        Config.loadConfigs();
        Ref.preInit();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new SpawnFixer());
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {
        Ref.postInit();
        createWorldProviders();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadConfigs());
    }

    private void createWorldProviders() {
        int currentId = Integer.MAX_VALUE;
        ClassLoaderWorldProvider classLoader = ClassLoaderWorldProvider.that;
        Hashtable<Class<? extends WorldProvider>, Integer> ourProviderIds = new Hashtable<>();
        Hashtable<Integer, Integer> dimensions = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "dimensions");
        Hashtable<Integer, Class<? extends WorldProvider>> providers = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "providers");
        Hashtable<Integer, Boolean> spawnSettings = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "spawnSettings");
        for(int dim : Config.dimensionPropertiesMap.keySet()) {
            Class<? extends WorldProvider> superClass = WorldProviderSurfaceAlt.class;
            boolean keepLoaded = false;
            int newId;
            if(DimensionManager.isDimensionRegistered(dim)) {
                int id = dimensions.get(dim);
                superClass = providers.get(id);
                keepLoaded = spawnSettings.get(id);
                DimensionManager.unregisterDimension(dim);
            }
            if(classLoader.hasProxy(superClass))
                newId = ourProviderIds.get(superClass);
            else {
                Class<? extends WorldProvider> proxyClass = classLoader.getWorldProviderProxy(superClass);
                //noinspection StatementWithEmptyBody
                while(!DimensionManager.registerProviderType(++currentId, proxyClass, keepLoaded));
                ourProviderIds.put(superClass, currentId);
                newId = currentId;
            }
            DimensionManager.registerDimension(dim, newId);
        }
    }
}
