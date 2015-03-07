package vorquel.mod.simpleskygrid.world.igenerated.random.point;

import net.minecraft.util.ChunkCoordinates;

import java.util.Random;

public class NormalPoint extends RandomPoint {
    
    private ChunkCoordinates mean;
    private double standardDeviationX;
    private double standardDeviationY;
    private double standardDeviationZ;

    public NormalPoint(ChunkCoordinates mean, double standardDeviationX, double standardDeviationY, double standardDeviationZ) {
        this.mean = mean;
        this.standardDeviationX = standardDeviationX;
        this.standardDeviationY = standardDeviationY;
        this.standardDeviationZ = standardDeviationZ;
    }

    @Override
    public ChunkCoordinates next(Random random) {
        double x = mean.posX;
        double y = mean.posY;
        double z = mean.posZ;
        if(standardDeviationX != 0)
            x += standardDeviationX * random.nextGaussian();
        if(standardDeviationY != 0)
            y += standardDeviationY * random.nextGaussian();
        if(standardDeviationZ != 0)
            z += standardDeviationZ * random.nextGaussian();
        return round(x, y, z);
    }
}
