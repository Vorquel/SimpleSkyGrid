package vorquel.mod.simpleskygrid.world.igenerated.random.point;

import net.minecraft.util.ChunkCoordinates;

import java.util.Random;

public class CirclePoint extends RandomPoint {

    private ChunkCoordinates center;
    private int radius;
    private Axis axis;

    public CirclePoint(ChunkCoordinates center, int radius, Axis axis) {
        this.center = center;
        this.radius = radius;
        this.axis = axis;
    }

    @Override
    public ChunkCoordinates next(Random random) {
        double angle = random.nextDouble() * Math.PI * 2;
        double cos = radius * Math.cos(angle);
        double sin = radius * Math.sin(angle);
        double x = center.posX;
        double y = center.posY;
        double z = center.posZ;
        switch(axis) {
            case X: y += cos; z += sin; break;
            case Y: z += cos; x += sin; break;
            case Z: x += cos; y += sin;
        }
        return round(x, y, z);
    }

    public static enum Axis {
        X, Y, Z
    }
}
