package vorquel.mod.simpleskygrid.world.generated.random.point;

import net.minecraft.util.BlockPos;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

public abstract class RandomPoint implements IRandom<BlockPos> {

    protected BlockPos round(double x, double y, double z) {
        return new BlockPos(round(x), round(y), round(z));
    }

    private int round(double value) {
        return ((int) (value + 2) / 4) * 4;
    }
}
