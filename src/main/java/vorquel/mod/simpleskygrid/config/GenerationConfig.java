package vorquel.mod.simpleskygrid.config;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerationConfig {

    private HashMap<String, ArrayList<IPrototype>> entries = new HashMap<>();
    private HashMap<String, ArrayList<Double>> weights = new HashMap<>();

    public void put(String label, IPrototype prototype, double weight) {
        if(!entries.containsKey(label)) {
            entries.put(label, new ArrayList<IPrototype>());
            weights.put(label, new ArrayList<Double>());
        }
        entries.get(label).add(prototype);
        weights.get(label).add(weight);
    }
}
