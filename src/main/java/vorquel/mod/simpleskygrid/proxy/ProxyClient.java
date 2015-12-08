package vorquel.mod.simpleskygrid.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import vorquel.mod.simpleskygrid.entity.EntityStasis;
import vorquel.mod.simpleskygrid.entity.RenderStasis;
import vorquel.mod.simpleskygrid.event.WorldTypeSelector;

@SuppressWarnings("unused")
public class ProxyClient extends Proxy {
    
    final static KeyBinding binding = new KeyBinding("debug", Keyboard.KEY_X, "debug");
    
    @Override
    public void preInit() {
        super.preInit();
    }
    
    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new WorldTypeSelector());
        RenderingRegistry.registerEntityRenderingHandler(EntityStasis.class, new RenderStasis());
        ClientRegistry.registerKeyBinding(binding); //todo: remove this debug code
        FMLCommonHandler.instance().bus().register(new Fish());
    }
    
    public static class Fish {
            @SubscribeEvent
            public void onKey(InputEvent.KeyInputEvent event) {
                if(!binding.isPressed()) return;
                EntityClientPlayerMP playerMP = Minecraft.getMinecraft().thePlayer;
                EntityStasis stasis = new EntityStasis(MinecraftServer.getServer().getEntityWorld());
                stasis.setPosition(playerMP.posX, playerMP.posY, playerMP.posZ);
                MinecraftServer.getServer().getEntityWorld().spawnEntityInWorld(stasis);
            }
    };
}
