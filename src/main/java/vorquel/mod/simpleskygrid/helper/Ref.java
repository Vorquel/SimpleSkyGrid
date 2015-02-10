package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameRegistry;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.item.Identifier;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;
import vorquel.mod.simpleskygrid.world.WorldTypeSkyGrid;

import java.util.HashMap;

public class Ref {

    public static WorldTypeSkyGrid worldType;
    public static int spawnHeight = 65;
    public static Identifier itemIdentifier = new Identifier();
    private static HashMap<Integer, RandomList<IGeneratedObject>> randomBlockGenerators = new HashMap<Integer, RandomList<IGeneratedObject>>();

    public static void preInit() {
        GameRegistry.registerItem(itemIdentifier, "identifier");
    }

    public static void postInit() {
        worldType = new WorldTypeSkyGrid();
        try{
            for(int i : Config.getDimensions())
                randomBlockGenerators.put(i, makeGenerator(Config.getLabel(i), true));
        } catch(Error e) {
            SimpleSkyGrid.logger.fatal("Fatal Error: Cyclical dependency in config.");
            throw new Error();
        }
    }

    private static RandomList<IGeneratedObject> makeGenerator(String label, boolean doNormalize) {
        RandomList<IGeneratedObject> randomList = new RandomList<IGeneratedObject>();
        for(int i=0; i<Config.size(label); ++i) {
            int weight = Config.getWeight(label, i);
            if(Config.isLabel(label, i)) {
                String newLabel = Config.getLabel(label, i);
                boolean newDoNormalize = !Config.isAbsolute(label, i);
                if(Config.size(newLabel) > 0)
                    randomList.add(makeGenerator(newLabel, newDoNormalize), weight);
                else
                    SimpleSkyGrid.logger.error("Error reading config, unrecognized label: " + newLabel);
            } else {
                IGeneratedObject generatedObject = Config.getIGeneratedObject(label, i);
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
