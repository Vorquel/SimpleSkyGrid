package vorquel.mod.simpleskygrid.world.provider;

import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.ChunkProviderSkyGrid;

public class WorldProviderSkyGrid extends WorldProviderSurface {
    @Override
    public IChunkProvider createChunkGenerator() {
        if(worldObj.getWorldInfo().getTerrainType() == Ref.worldType)
            return new ChunkProviderSkyGrid(worldObj, worldObj.getSeed(), dimensionId);
        else
            return super.createChunkGenerator();
    }
}
