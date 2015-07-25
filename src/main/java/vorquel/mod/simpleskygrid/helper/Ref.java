package vorquel.mod.simpleskygrid.helper;

import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.config.ConfigDataMap;
import vorquel.mod.simpleskygrid.config.UniqueQuantity;
import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.config.prototype.PLabel;
import vorquel.mod.simpleskygrid.world.WorldTypeSkyGrid;
import vorquel.mod.simpleskygrid.world.generated.GeneratedUnique;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ref {

    public static final String MOD_ID = "SimpleSkyGrid";
    public static WorldTypeSkyGrid worldType;
    private static HashMap<Integer, RandomList<IGeneratedObject>> randomGenerators;
    private static HashMap<Integer, ArrayList<GeneratedUnique>> uniqueGenerators;

    public static void postInit() {
        worldType = new WorldTypeSkyGrid();
        createGenerators();
    }

    public static void createGenerators() {
        randomGenerators = new HashMap<>();
        uniqueGenerators = new HashMap<>();
        try{
            for(int i : Config.dimensionPropertiesMap.keySet()) {
                randomGenerators.put(i, makeRandomGenerator(Config.dimensionPropertiesMap.get(i).generationLabel, true));
                uniqueGenerators.put(i, makeUniqueGenerator(Config.dimensionPropertiesMap.get(i).uniqueGenLabel));
            }
        } catch(StackOverflowError | OutOfMemoryError e) {
            Log.fatal("Fatal Error: Cyclical dependency in config.");
            throw new Error("Fatal Error: Cyclical dependency in config.");
        }
    }

    private static RandomList<IGeneratedObject> makeRandomGenerator(String label, boolean doNormalize) {
        RandomList<IGeneratedObject> randomList = new RandomList<>();
        ConfigDataMap<IPrototype<IGeneratedObject>, Double> config = Config.generationData;
        for(int i=0; i<config.size(label); ++i) {
            IPrototype<IGeneratedObject> entry = config.getEntry(label, i);
            double weight = config.getQuantity(label, i);
            if(weight <= 0)
                continue;
            if(entry instanceof PLabel) {
                PLabel newLabel = (PLabel) entry;
                if(config.size(newLabel.name) > 0)
                    randomList.add(makeRandomGenerator(newLabel.name, newLabel.subtype == PLabel.Subtype.relative), weight);
                else
                    Log.warn("Unused label in config: %s", newLabel.name);
            } else {
                randomList.add(entry.getObject(), weight);
            }
        }
        if(doNormalize)
            randomList.normalize();
        return randomList;
    }

    private static ArrayList<GeneratedUnique> makeUniqueGenerator(String label) {
        ArrayList<GeneratedUnique> list = new ArrayList<>();
        ConfigDataMap<IPrototype<IGeneratedObject>, UniqueQuantity> config = Config.uniqueGenData;
        for(int i=0; i<config.size(label); ++i) {
            IPrototype<IGeneratedObject> entry = config.getEntry(label, i);
            if(entry instanceof PLabel) {
                PLabel newLabel = (PLabel) entry;
                if(config.size(newLabel.name) > 0)
                    list.addAll(makeUniqueGenerator(newLabel.name));
                else
                    Log.warn("Unused label in config: %s", newLabel.name);
            } else {
                IGeneratedObject generatedObject = entry.getObject();
                UniqueQuantity quantity = config.getQuantity(label, i);
                list.add(new GeneratedUnique(generatedObject, quantity.countSource, quantity.pointSource));
            }
        }
        return list;
    }

    public static RandomList<IGeneratedObject> getRandomGenerator(int dimensionId) {
        return randomGenerators.get(dimensionId);
    }

    public static ArrayList<GeneratedUnique> getUniqueGenerator(int dimensionId) {
        return uniqueGenerators.get(dimensionId);
    }
}
