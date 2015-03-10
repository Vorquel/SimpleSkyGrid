package vorquel.mod.simpleskygrid.world.generated.random.count;

import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

import java.util.Random;

public class UniformCount implements IRandom<Integer> {

    private int start;
    private int range;

    public UniformCount(int l, int r) {
        start = Math.min(l, r);
        range = Math.abs(l - r) + 1;
    }

    @Override
    public Integer next(Random random) {
        int rand = start;
        if(range > 1)
            rand += random.nextInt(range);
        return rand;
    }
}
