package vorquel.mod.simpleskygrid.world;

import java.util.ArrayList;
import java.util.Random;

public class RandomIGeneratedObject {

    private ArrayList<IGeneratedObject> generatedObjects = new ArrayList<IGeneratedObject>();
    private ArrayList<Double> weights = new ArrayList<Double>();
    private double totalWeight = 0;

    public IGeneratedObject getNext(Random random) {
        double rand = random.nextDouble();
        int i=0;
        double weight = weights.get(0);
        while(weight<=rand) {
            ++i;
            weight += weights.get(i);
        }
        return generatedObjects.get(i);
    }

    public void add(IGeneratedObject generatedObject, double weight) {
        if(generatedObject == null || weight == 0)
            return;
        generatedObjects.add(generatedObject);
        weights.add(weight);
        totalWeight += weight;
    }

    public void add(RandomIGeneratedObject randomIGeneratedObject, double weight) {
        for(int i=0; i< randomIGeneratedObject.generatedObjects.size(); ++i) {
            IGeneratedObject generatedObject = randomIGeneratedObject.generatedObjects.get(i);
            double newWeight = weight * randomIGeneratedObject.weights.get(i);
            add(generatedObject, newWeight);
        }
    }

    public void normalize() {
        for(int i=0; i< weights.size(); ++i) {
            weights.set(i, weights.get(i)/totalWeight);
        }
        totalWeight = 1;
    }
}
