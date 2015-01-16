package vorquel.mod.simpleskygrid.helper;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Random;

public class RandomBlockGenerator {

    private ArrayList<BlockComplex> complexes = new ArrayList<BlockComplex>();
    private ArrayList<Integer> weights = new ArrayList<Integer>();
    private int totalWeight = 0;

    public BlockComplex getNextBlock(Random random) {
        int rand = random.nextInt(totalWeight);
        int i=0;
        int weight = weights.get(0);
        while(weight<=rand) {
            ++i;
            weight += weights.get(i);
        }
        return complexes.get(i);
    }

    public void addBlock(Block block, int metadata, NBTTagCompound nbt, int weight) {
        complexes.add(new BlockComplex(block, metadata, nbt));
        weights.add(weight);
        totalWeight += weight;
    }

    public static class BlockComplex {
        public Block block;
        public int metadata;
        public NBTTagCompound nbt;
        public BlockComplex(Block block, int metadata, NBTTagCompound nbt) {
            this.block = block;
            this.metadata = metadata;
            this.nbt = nbt;
        }
    }
}
