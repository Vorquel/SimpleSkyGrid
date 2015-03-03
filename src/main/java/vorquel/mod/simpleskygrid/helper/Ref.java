package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameRegistry;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.config.ConfigDataMap;
import vorquel.mod.simpleskygrid.config.IPrototype;
import vorquel.mod.simpleskygrid.config.PrototypeLabel;
import vorquel.mod.simpleskygrid.item.Identifier;
import vorquel.mod.simpleskygrid.world.WorldTypeSkyGrid;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

import java.util.HashMap;

public class Ref {

    public static final String MOD_ID = "SimpleSkyGrid";
    public static WorldTypeSkyGrid worldType;
    public static Identifier itemIdentifier = new Identifier();
    private static HashMap<Integer, RandomList<IGeneratedObject>> randomBlockGenerators = new HashMap<>();

    public static void preInit() {
        GameRegistry.registerItem(itemIdentifier, "identifier");
    }

    public static void postInit() {
        worldType = new WorldTypeSkyGrid();
        try{
            for(int i : Config.dimensionPropertiesMap.keySet())
                randomBlockGenerators.put(i, makeGenerator(Config.dimensionPropertiesMap.get(i).generationLabel, true));
        } catch(StackOverflowError | OutOfMemoryError e) {
            SimpleSkyGrid.logger.fatal("Fatal Error: Cyclical dependency in config.");
            throw new Error();
        }
    }

    private static RandomList<IGeneratedObject> makeGenerator(String label, boolean doNormalize) {
        RandomList<IGeneratedObject> randomList = new RandomList<>();
        ConfigDataMap<IPrototype, Double> config = Config.generationData;
        for(int i=0; i< config.size(label); ++i) {
            IPrototype entry = config.getEntry(label, i);
            double weight = config.getQuantity(label, i);
            if(entry instanceof PrototypeLabel) {
                PrototypeLabel newLabel = (PrototypeLabel) entry;
                if(config.size(newLabel.name) > 0)
                   randomList.add(makeGenerator(newLabel.name, newLabel.subtype == PrototypeLabel.Subtype.relative), weight);
                else
                    SimpleSkyGrid.logger.error("Error reading config, unrecognized label: " + newLabel);
            } else {
                IGeneratedObject generatedObject = entry.getGeneratedObject();
                if(generatedObject != null)
                    randomList.add(generatedObject, weight);
            }
        }
        if(doNormalize)
            randomList.normalize();
        return randomList;
    }

    public static RandomList<IGeneratedObject> getGenerator(int dimensionId) {
        return randomBlockGenerators.get(dimensionId);
    }
}
