package vorquel.mod.simpleskygrid.proxy;

import net.minecraftforge.common.MinecraftForge;
import vorquel.mod.simpleskygrid.event.WorldTypeSelector;

@SuppressWarnings("unused")
public class ProxyClient extends Proxy {
    
    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new WorldTypeSelector());
//        RenderingRegistry.registerEntityRenderingHandler(EntityStasis.class, new RenderStasis());
    }
}
