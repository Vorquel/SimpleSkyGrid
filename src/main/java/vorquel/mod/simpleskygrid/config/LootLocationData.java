package vorquel.mod.simpleskygrid.config;

import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LootLocationData {

    private HashMap<String, HashMap<IPrototype<IGeneratedObject>, ArrayList<IPrototype<ILootSource>>>> entries;
    private HashMap<String, HashMap<IPrototype<IGeneratedObject>, ArrayList<Double>>> weights;

    public void put(String label, IPrototype<IGeneratedObject> target, IPrototype<ILootSource> source, double weight) {
        if(!entries.containsKey(label)) {
            entries.put(label, new HashMap<IPrototype<IGeneratedObject>, ArrayList<IPrototype<ILootSource>>>());
            weights.put(label, new HashMap<IPrototype<IGeneratedObject>, ArrayList<Double>>());
        }
        if(!entries.get(label).containsKey(target)) {
            entries.get(label).put(target, new ArrayList<IPrototype<ILootSource>>());
            weights.get(label).put(target, new ArrayList<Double>());
        }
        entries.get(label).get(target).add(source);
        weights.get(label).get(target).add(weight);
    }

    public int size() {
        return entries.size();
    }

    public int size(String label, IPrototype<IGeneratedObject> target) {
        if(entries.containsKey(label))
            if(entries.get(label).containsKey(target))
                return entries.get(label).get(target).size();
            else
                return 0;
        else
            return 0;
    }

    public Set<IPrototype<IGeneratedObject>> getTargetSet(String label) {
        if(entries.containsKey(label))
            return entries.get(label).keySet();
        else
            return new HashSet<>();
    }

    public IPrototype<ILootSource> getEntry(String label, IPrototype<IGeneratedObject> target, int index) {
        return entries.get(label).get(target).get(index);
    }

    public double getWeight(String label, IPrototype<IGeneratedObject> target, int index) {
        return weights.get(label).get(target).get(index);
    }

}
