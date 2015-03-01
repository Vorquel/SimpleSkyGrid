package vorquel.mod.simpleskygrid.world.igenerated;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;

import java.util.Random;

public class GeneratedUnique implements IGeneratedObject {

    private IGeneratedObject generatedObject;
    private IRandom<Integer> countSource;
    private IRandom<ChunkCoordinates> locationSource;

    public GeneratedUnique(IGeneratedObject generatedObject, IRandom<Integer> countSource, IRandom<ChunkCoordinates> locationSource) {
        this.generatedObject = generatedObject;
        this.countSource = countSource;
        this.locationSource = locationSource;
    }

    public int getCount(Random random) {
        return countSource.next(random);
    }

    public ChunkCoordinates getLocation(Random random) {
        return locationSource.next(random);
    }

    @Override
    public void provideObject(Random random, World world, Chunk chunk, int x, int y, int z) {
        generatedObject.provideObject(random, world, chunk, z, y, z);
    }
}
