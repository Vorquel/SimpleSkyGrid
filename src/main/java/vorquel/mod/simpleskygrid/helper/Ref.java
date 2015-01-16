package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import vorquel.mod.simpleskygrid.item.Identifier;
import vorquel.mod.simpleskygrid.world.WorldTypeSkyGrid;

import java.util.HashMap;

public class Ref {

    public static WorldTypeSkyGrid worldType;
    public static Identifier itemIdentifier = new Identifier();
    private static HashMap<Integer, RandomBlockGenerator> randomBlockGenerators = new HashMap<Integer, RandomBlockGenerator>();
    private static HashMap<Integer, Class<? extends WorldProvider>> worldProviderProxyClasses = new HashMap<Integer, Class<? extends WorldProvider>>();

    public static void preInit() {
        GameRegistry.registerItem(itemIdentifier, "identifier");
    }

    public static void postInit() {
        worldType = new WorldTypeSkyGrid();
        for(int i : Config.getDimensions()) {
            RandomBlockGenerator randomBlockGenerator = new RandomBlockGenerator();
            String label = Config.getLabel(i);
            for(int j=0; j<Config.size(label); ++j) {
                Block block = Config.getBlock(label, j);
                int metaData = Config.getMetadata(label, j);
                NBTTagCompound nbt = Config.getNBT(label, j);
                int weight = Config.getWeight(label, j);
                randomBlockGenerator.addBlock(block, metaData, nbt, weight);
            }
            randomBlockGenerators.put(i, randomBlockGenerator);
        }
    }

    public static RandomBlockGenerator getGenerator(int dimensionId) {
        System.out.println("Loading Dimension: " + dimensionId);
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
