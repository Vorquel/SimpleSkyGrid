package vorquel.mod.simpleskygrid.world.generated.random.point;

import net.minecraft.util.BlockPos;

import java.util.Random;

public class CirclePoint extends RandomPoint {

    private BlockPos center;
    private int radius;
    private Axis axis;

    public CirclePoint(BlockPos center, int radius, Axis axis) {
        this.center = center;
        this.radius = radius;
        this.axis = axis;
    }

    @Override
    public BlockPos next(Random random) {
        double angle = random.nextDouble() * Math.PI * 2;
        double cos = radius * Math.cos(angle);
        double sin = radius * Math.sin(angle);
        double x = center.getX();
        double y = center.getY();
        double z = center.getZ();
        switch(axis) {
            case X: y += cos; z += sin; break;
            case Y: z += cos; x += sin; break;
            case Z: x += cos; y += sin;
        }
        return round(x, y, z);
    }

    public enum Axis {
        X, Y, Z
    }
}
