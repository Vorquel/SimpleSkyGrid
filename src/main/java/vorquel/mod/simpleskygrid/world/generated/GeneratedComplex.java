package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class GeneratedComplex implements IGeneratedObject {

    private HashMap<BlockPos, IGeneratedObject> generationMap = new HashMap<>();

    public void put(BlockPos key, IGeneratedObject value) {
        generationMap.put(key, value);
    }

    @Override
    public void provideObject(Random random, World world, BlockPos pos) {
        int max = world.provider.getHeight();
        for(BlockPos key : generationMap.keySet()) {
            int newY = pos.getY() + key.getY();
            if(newY >= 0 && newY < max)
                generationMap.get(key).provideObject(random, world, key.add(pos));
        }
    }
}
