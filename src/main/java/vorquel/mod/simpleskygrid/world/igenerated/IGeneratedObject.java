package vorquel.mod.simpleskygrid.world.igenerated;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public interface IGeneratedObject {
    public void provideObject(Random random, World world, Chunk chunk, int x, int y, int z);
}
