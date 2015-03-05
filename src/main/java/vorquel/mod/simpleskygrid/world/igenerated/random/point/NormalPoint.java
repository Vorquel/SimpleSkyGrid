package vorquel.mod.simpleskygrid.world.igenerated.random.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;

import java.util.Random;

public class NormalPoint implements IRandom<ChunkCoordinates> {
    
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
        int x = mean.posX;
        int y = mean.posY;
        int z = mean.posZ;
        if(standardDeviationX != 0)
            x += standardDeviationX * random.nextGaussian() + .5;
        if(standardDeviationY != 0)
            y += standardDeviationY * random.nextGaussian() + .5;
        if(standardDeviationZ != 0)
            z += standardDeviationZ * random.nextGaussian() + .5;
        return new ChunkCoordinates(x, y, z);
    }
}
