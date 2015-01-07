package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import vorquel.mod.simpleskygrid.item.Identifier;
import vorquel.mod.simpleskygrid.world.RandomBlockGenerator;
import vorquel.mod.simpleskygrid.world.WorldTypeSkyGrid;

public class Ref {

    public static WorldTypeSkyGrid worldType;
    public static RandomBlockGenerator randomBlockGenerator;
    public static Identifier itemIdentifier = new Identifier();

    public static void preInit() {
        GameRegistry.registerItem(itemIdentifier, "identifier");
    }

    public static void postInit() {
        worldType = new WorldTypeSkyGrid();
        randomBlockGenerator = new RandomBlockGenerator();
        for(int i=0; i<Config.size(); ++i) {
            randomBlockGenerator.addBlock(Config.getBlock(i), Config.getMetadata(i), Config.getNBT(i), Config.getWeight(i));
        }
    }
}
