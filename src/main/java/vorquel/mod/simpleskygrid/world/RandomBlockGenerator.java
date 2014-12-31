package vorquel.mod.simpleskygrid.world;

import net.minecraft.block.Block;

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

    public void addBlock(Block block, int metadata, int weight) {
        complexes.add(new BlockComplex(block, metadata));
        weights.add(weight);
        totalWeight += weight;
    }

    public class BlockComplex {
        public Block block;
        public int metadata;
        public BlockComplex(Block block, int metadata) {
            this.block = block;
            this.metadata = metadata;
        }
    }
}
