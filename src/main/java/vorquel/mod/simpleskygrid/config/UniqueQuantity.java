package vorquel.mod.simpleskygrid.config;

import net.minecraft.util.math.BlockPos;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

public class UniqueQuantity {

    public IRandom<Integer> countSource;
    public IRandom<BlockPos> pointSource;

    public boolean isComplete() {
        return countSource != null && pointSource != null;
    }
}
