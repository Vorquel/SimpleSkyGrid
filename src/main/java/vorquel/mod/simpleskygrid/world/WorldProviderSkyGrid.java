package vorquel.mod.simpleskygrid.world;

import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import vorquel.mod.simpleskygrid.helper.Ref;

public class WorldProviderSkyGrid extends WorldProviderSurface {
    @Override
    public IChunkProvider createChunkGenerator() {
        if(worldObj.getWorldInfo().getTerrainType() == Ref.worldType)
            return new ChunkProviderSkyGrid(worldObj, worldObj.getSeed(), dimensionId);
        else
            return super.createChunkGenerator();
    }
}
