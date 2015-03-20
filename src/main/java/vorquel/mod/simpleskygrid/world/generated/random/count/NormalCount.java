package vorquel.mod.simpleskygrid.world.generated.random.count;

import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

import java.util.Random;

public class NormalCount implements IRandom<Integer> {

    private double mean;
    private double standardDeviation;

    public NormalCount(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public Integer next(Random random) {
        double normal = mean + standardDeviation * random.nextGaussian();
        return (int) (normal + .5);
    }
}
