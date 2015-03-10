package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.world.World;

import java.util.Random;

public interface IGeneratedObject {
    void provideObject(Random random, World world, int x, int y, int z);
}
