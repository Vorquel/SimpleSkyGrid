package vorquel.mod.simpleskygrid.world.generated.random.point;

import net.minecraft.util.BlockPos;

import java.util.Random;

public class UniformPoint extends RandomPoint {

    private double startX;
    private double startY;
    private double startZ;
    private double rangeX;
    private double rangeY;
    private double rangeZ;

    public UniformPoint(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        startX = minX;
        startY = minY;
        startZ = minZ;
        rangeX = maxX - minX;
        rangeY = maxY - minY;
        rangeZ = maxZ - minZ;
    }

    @Override
    public BlockPos next(Random random) {
        double x = startX;
        double y = startY;
        double z = startZ;
        if(rangeX != 0)
            x += random.nextDouble() * rangeX;
        if(rangeY != 0)
            y += random.nextDouble() * rangeY;
        if(rangeZ != 0)
            z += random.nextDouble() * rangeZ;
        return round(x, y, z);
    }
}
