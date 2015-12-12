package vorquel.mod.simpleskygrid.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.helper.Ref;

public class SpawnFixer {

    //Thank you diesieben07 for this function.
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NBTTagCompound data = event.player.getEntityData();
        NBTTagCompound persistent;
        if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));
        } else {
            persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        }

        if (!persistent.hasKey("simpleskygrid.hasLogged")) {
            persistent.setBoolean("simpleskygrid.hasLogged", true);
            playerRespawn(new PlayerEvent.PlayerRespawnEvent(event.player));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(event.player.worldObj.getWorldInfo().getTerrainType() != Ref.worldType || !Config.dimensionPropertiesMap.containsKey(event.player.dimension))
            return;
        int x = (int) event.player.posX;
        int y = (int) event.player.posY;
        int z = (int) event.player.posZ;
        BlockPos bedCoordinates = event.player.getBedLocation(event.player.dimension);
        if(y <= 1 || bedCoordinates == null || bedCoordinates.distanceSq(x, y, z) > 10) {
            x -= x%4;
            z -= z%4;
            Chunk chunk = event.player.worldObj.getChunkFromBlockCoords(new BlockPos(x, y, z));
            int spawnHeight = Config.dimensionPropertiesMap.get(event.player.dimension).spawnHeight;
            for(y=Math.min(chunk.getTopFilledSegment()+16, spawnHeight); y>0; --y) {
                if(chunk.getBlock(x&15, y-1, z&15).getMaterial().blocksMovement())
                    break;
            }
            event.player.setPositionAndUpdate(x + .5, y, z + .5);
        }
    }
}
