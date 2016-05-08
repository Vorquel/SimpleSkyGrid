package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface IGeneratedObject {
    void provideObject(Random random, World world, BlockPos pos);
}
