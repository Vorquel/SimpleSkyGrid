package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

import java.util.Random;

public class GeneratedUnique implements IGeneratedObject {

    private IGeneratedObject generatedObject;
    private IRandom<Integer> countSource;
    private IRandom<BlockPos> locationSource;

    public GeneratedUnique(IGeneratedObject generatedObject, IRandom<Integer> countSource, IRandom<BlockPos> locationSource) {
        this.generatedObject = generatedObject;
        this.countSource = countSource;
        this.locationSource = locationSource;
    }

    public IGeneratedObject getGeneratedObject() {
        return generatedObject;
    }

    public int getCount(Random random) {
        return countSource.next(random);
    }

    public BlockPos getLocation(Random random) {
        return locationSource.next(random);
    }

    @Override
    public void provideObject(Random random, World world, int x, int y, int z) {
        generatedObject.provideObject(random, world, x, y, z);
    }
}
