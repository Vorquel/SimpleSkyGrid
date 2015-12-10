package vorquel.mod.simpleskygrid.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.common.MinecraftForge;
import vorquel.mod.simpleskygrid.entity.EntityStasis;
import vorquel.mod.simpleskygrid.entity.RenderStasis;
import vorquel.mod.simpleskygrid.event.WorldTypeSelector;

@SuppressWarnings("unused")
public class ProxyClient extends Proxy {
    
    @Override
    public void preInit() {
        super.preInit();
    }
    
    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new WorldTypeSelector());
        RenderingRegistry.registerEntityRenderingHandler(EntityStasis.class, new RenderStasis());
    }
}
