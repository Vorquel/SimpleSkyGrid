package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.item.Identifier;
import vorquel.mod.simpleskygrid.world.WorldTypeSkyGrid;

import java.util.HashMap;

public class Ref {

    public static WorldTypeSkyGrid worldType;
    public static int spawnHeight = 65;
    public static Identifier itemIdentifier = new Identifier();
    private static HashMap<Integer, RandomBlockGenerator> randomBlockGenerators = new HashMap<Integer, RandomBlockGenerator>();
    private static HashMap<Integer, Class<? extends WorldProvider>> worldProviderProxyClasses = new HashMap<Integer, Class<? extends WorldProvider>>();

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

    private static RandomBlockGenerator makeGenerator(String label, boolean doNormalize) {
        RandomBlockGenerator randomBlockGenerator = new RandomBlockGenerator();
        for(int i=0; i<Config.size(label); ++i) {
            int weight = Config.getWeight(label, i);
            if(Config.isLabel(label, i)) {
                String newLabel = Config.getLabel(label, i);
                boolean newDoNormalize = !Config.isAbsolute(label, i);
                if(Config.size(newLabel) > 0)
                    randomBlockGenerator.addGenerator(makeGenerator(newLabel, newDoNormalize), weight);
                else
                    SimpleSkyGrid.logger.error("Error reading config, unrecognized label: " + newLabel);
            } else {
                Block block = Config.getBlock(label, i);
                int metaData = Config.getMetadata(label, i);
                NBTTagCompound nbt = Config.getNBT(label, i);
                randomBlockGenerator.addBlock(block, metaData, nbt, weight);
            }
        }
        if(doNormalize)
            randomBlockGenerator.normalize();
        return randomBlockGenerator;
    }

    public static RandomBlockGenerator getGenerator(int dimensionId) {
        return randomBlockGenerators.get(dimensionId);
    }

    public static void setWorldProviderProxy(int dim, Class<? extends WorldProvider> clazz) {
        worldProviderProxyClasses.put(dim, clazz);
    }

    public static WorldProvider createWorldProviderProxy(int dim) {
        try {
            return worldProviderProxyClasses.get(dim).newInstance();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating WorldProvider proxy");
        }
    }
}
