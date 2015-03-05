package vorquel.mod.simpleskygrid.world.igenerated.random.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;

import java.util.Random;

public class CirclePoint implements IRandom<ChunkCoordinates> {

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
        int x = center.posX;
        int y = center.posY;
        int z = center.posZ;
        switch(axis) {
            case X:
                x = round(x, 4);
                y = round(y + cos, 4);
                z = round(z + sin, 4);
                break;
            case Y:
                x = round(x + sin, 4);
                y = round(y, 4);
                z = round(z + cos, 4);
                break;
            case Z:
                x = round(x + cos, 4);
                y = round(y + sin, 4);
                z = round(z, 4);
        }
        return new ChunkCoordinates(x, y, z);
    }

    private int round(double value, int precision) {
        int half = precision / 2;
        return ((int) (value + half) / precision) * precision;
    }

    public static enum Axis {
        X, Y, Z
    }
}
