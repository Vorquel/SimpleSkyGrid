package vorquel.mod.simpleskygrid;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.proxy.Proxy;

@Mod(modid = Ref.MOD_ID, name = "Simple Sky Grid", version = "@MOD_VERSION@")
public class SimpleSkyGrid {

    @Mod.Instance(Ref.MOD_ID)
    @SuppressWarnings("unused")
    public static SimpleSkyGrid instance;
    @SidedProxy(clientSide = "vorquel.mod.simpleskygrid.proxy.ProxyClient", serverSide = "vorquel.mod.simpleskygrid.proxy.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverLoad(FMLServerStartingEvent event) {
        proxy.serverLoad(event);
    }
}
