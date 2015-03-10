package vorquel.mod.simpleskygrid.world.generated.random.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

public abstract class RandomPoint implements IRandom<ChunkCoordinates> {

    protected ChunkCoordinates round(double x, double y, double z) {
        return new ChunkCoordinates(round(x), round(y), round(z));
    }

    private int round(double value) {
        return ((int) (value + 2) / 4) * 4;
    }
}
