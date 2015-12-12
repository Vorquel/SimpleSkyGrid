package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import com.jcraft.jorbis.Block;

public class GeneratedEndPortal extends GeneratedComplex {

    private double filledChance;

    public GeneratedEndPortal(double filledChance) {
        this.filledChance = filledChance;
        put(new BlockPos(-1, 0, -2), new GeneratedFrame(0));
        put(new BlockPos( 0, 0, -2), new GeneratedFrame(0));
        put(new BlockPos( 1, 0, -2), new GeneratedFrame(0));
        put(new BlockPos( 2, 0, -1), new GeneratedFrame(1));
        put(new BlockPos( 2, 0,  0), new GeneratedFrame(1));
        put(new BlockPos( 2, 0,  1), new GeneratedFrame(1));
        put(new BlockPos( 1, 0,  2), new GeneratedFrame(2));
        put(new BlockPos( 0, 0,  2), new GeneratedFrame(2));
        put(new BlockPos(-1, 0,  2), new GeneratedFrame(2));
        put(new BlockPos(-2, 0,  1), new GeneratedFrame(3));
        put(new BlockPos(-2, 0,  0), new GeneratedFrame(3));
        put(new BlockPos(-2, 0, -1), new GeneratedFrame(3));
    }

    private class GeneratedFrame implements IGeneratedObject {

        private int direction;

        private GeneratedFrame(int direction) {
            this.direction = direction;
        }

        @Override
        public void provideObject(Random random, World world, int x, int y, int z) {
            int meta = random.nextDouble() < filledChance ? direction + 4 : direction;
            BlockPos pos = new BlockPos(x,y,z);
            world.setBlockState(pos, Blocks.end_portal_frame.getDefaultState(), meta);
        }
    }
}
