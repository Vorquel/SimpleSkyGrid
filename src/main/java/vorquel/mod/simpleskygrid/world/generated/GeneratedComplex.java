package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class GeneratedComplex implements IGeneratedObject {

    private HashMap<ChunkCoordinates, IGeneratedObject> generationMap = new HashMap<>();

    public IGeneratedObject put(ChunkCoordinates key, IGeneratedObject value) {
        return generationMap.put(key, value);
    }

    @Override
    public void provideObject(Random random, World world, int x, int y, int z) {
        int max = world.provider.getHeight();
        for(ChunkCoordinates key : generationMap.keySet()) {
            int newY = y + key.posY;
            if(newY >= 0 && newY < max)
                generationMap.get(key).provideObject(random, world, x + key.posX, newY, z + key.posZ);
        }
    }
}
