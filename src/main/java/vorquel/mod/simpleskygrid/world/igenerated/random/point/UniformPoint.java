package vorquel.mod.simpleskygrid.world.igenerated.random.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;

import java.util.Random;

public class UniformPoint implements IRandom<ChunkCoordinates> {

    private int startX;
    private int startY;
    private int startZ;
    private int rangeX;
    private int rangeY;
    private int rangeZ;

    public UniformPoint(int lx, int rx, int ly, int ry, int lz, int rz) {
        startX = Math.min(lx, rx);
        startY = Math.min(ly, ry);
        startZ = Math.min(lz, rz);
        rangeX = Math.abs(lx - rx) + 1;
        rangeY = Math.abs(ly - ry) + 1;
        rangeZ = Math.abs(lz - rz) + 1;
    }

    @Override
    public ChunkCoordinates next(Random random) {
        int x = startX;
        int y = startY;
        int z = startZ;
        if(rangeX > 1)
            x += random.nextInt(rangeX);
        if(rangeY > 1)
            y += random.nextInt(rangeY);
        if(rangeZ > 1)
            z += random.nextInt(rangeZ);
        return new ChunkCoordinates(x, y, z);
    }
}
