package vorquel.mod.simpleskygrid.helper;

import java.util.ArrayList;
import java.util.Random;

public class RandomList<T> {

    private ArrayList<T> list = new ArrayList<>();
    private ArrayList<Double> weights = new ArrayList<>();
    private double totalWeight = 0;

    public T getNext(Random random) {
        double rand = random.nextDouble();
        int i=0;
        double weight = weights.get(0);
        while(weight<=rand) {
            ++i;
            weight += weights.get(i);
        }
        return list.get(i);
    }

    public void add(T generatedObject, double weight) {
        if(generatedObject == null || weight == 0)
            return;
        list.add(generatedObject);
        weights.add(weight);
        totalWeight += weight;
    }

    public void add(RandomList<T> randomT, double weight) {
        for(int i=0; i< randomT.list.size(); ++i) {
            T generatedObject = randomT.list.get(i);
            double newWeight = weight * randomT.weights.get(i);
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
