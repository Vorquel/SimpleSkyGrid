package vorquel.mod.simpleskygrid.helper;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Random;

public class RandomBlockGenerator {

    private ArrayList<BlockComplex> complexes = new ArrayList<BlockComplex>();
    private ArrayList<Double> weights = new ArrayList<Double>();
    private double totalWeight = 0;

    public BlockComplex getNextBlock(Random random) {
        double rand = random.nextDouble();
        int i=0;
        double weight = weights.get(0);
        while(weight<=rand) {
            ++i;
            weight += weights.get(i);
        }
        return complexes.get(i);
    }

    public void addBlock(Block block, int metadata, NBTTagCompound nbt, double weight) {
        if(block == null || weight == 0)
            return;
        complexes.add(new BlockComplex(block, metadata, nbt));
        weights.add(weight);
        totalWeight += weight;
    }

    public void addGenerator(RandomBlockGenerator randomBlockGenerator, double weight) {
        for(int i=0; i<randomBlockGenerator.complexes.size(); ++i) {
            Block block = randomBlockGenerator.complexes.get(i).block;
            int metadata = randomBlockGenerator.complexes.get(i).metadata;
            NBTTagCompound nbt = randomBlockGenerator.complexes.get(i).nbt;
            double newWeight = weight * randomBlockGenerator.weights.get(i);
            addBlock(block, metadata, nbt, newWeight);
        }
    }

    public void normalize() {
        for(int i=0; i< weights.size(); ++i) {
            weights.set(i, weights.get(i)/totalWeight);
        }
        totalWeight = 1;
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
