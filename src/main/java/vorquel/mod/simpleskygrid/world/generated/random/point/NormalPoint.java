package vorquel.mod.simpleskygrid.world.generated.random.point;

import net.minecraft.util.BlockPos;

import java.util.Random;

public class NormalPoint extends RandomPoint {
    
    private BlockPos mean;
    private double standardDeviationX;
    private double standardDeviationY;
    private double standardDeviationZ;

    public NormalPoint(BlockPos mean, double standardDeviationX, double standardDeviationY, double standardDeviationZ) {
        this.mean = mean;
        this.standardDeviationX = standardDeviationX;
        this.standardDeviationY = standardDeviationY;
        this.standardDeviationZ = standardDeviationZ;
    }

    @Override
    public BlockPos next(Random random) {
        double x = mean.getX();
        double y = mean.getY();
        double z = mean.getZ();
        if(standardDeviationX != 0)
            x += standardDeviationX * random.nextGaussian();
        if(standardDeviationY != 0)
            y += standardDeviationY * random.nextGaussian();
        if(standardDeviationZ != 0)
            z += standardDeviationZ * random.nextGaussian();
        return round(x, y, z);
    }
}
