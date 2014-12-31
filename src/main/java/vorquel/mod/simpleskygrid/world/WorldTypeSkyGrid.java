package vorquel.mod.simpleskygrid.world;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldTypeSkyGrid extends WorldType {

    public WorldTypeSkyGrid() {
        super("skyGrid");
    }

    @Override
    public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
        return new ChunkProviderSkyGrid(world);
    }

    @Override
    public float getCloudHeight() {
        return -10;
    }

    @Override
    public double getHorizon(World world) {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public int getSpawnFuzz() {
        return 1;
    }
}
