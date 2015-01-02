package vorquel.mod.simpleskygrid.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.world.chunk.Chunk;

public class SpawnFixer {

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.player.getHealth() <= 0)
            return;
        playerRespawn(new PlayerEvent.PlayerRespawnEvent(event.player));
    }

    @SubscribeEvent
    public void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        int x = (int) event.player.posX;
        int y = (int) event.player.posY;
        int z = (int) event.player.posZ;
        if(y>0)
            return;
        x -= x%4;
        z -= z%4;
        Chunk chunk = event.player.worldObj.getChunkFromBlockCoords(x, z);
        for(y=chunk.getTopFilledSegment()+16; y>0; --y) {
            if(chunk.getBlock(x&15, y-1, z&15).getMaterial().blocksMovement())
                break;
        }
        event.player.setPositionAndUpdate(x + .5, y, z + .5);
    }
}
