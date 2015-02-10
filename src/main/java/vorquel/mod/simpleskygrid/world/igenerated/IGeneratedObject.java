package vorquel.mod.simpleskygrid.world.igenerated;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IGeneratedObject {
    public void provideObject(World world, Chunk chunk, int x, int y, int z);
}
