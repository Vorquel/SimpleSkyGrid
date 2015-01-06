package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.io.File;
import java.util.ArrayList;

public class Config {

    private static Configuration config;

    private static final String[] blockDefaults = {"minecraft:stone", "minecraft:grass", "minecraft:dirt", "minecraft:water",
            "minecraft:lava", "minecraft:sand", "minecraft:gravel", "minecraft:gold_ore", "minecraft:iron_ore", "minecraft:coal_ore",
            "minecraft:log", "minecraft:leaves", "minecraft:glass", "minecraft:lapis_ore", "minecraft:sandstone", "minecraft:sticky_piston",
            "minecraft:web", "minecraft:piston", "minecraft:wool", "minecraft:tnt", "minecraft:bookshelf", "minecraft:mossy_cobblestone",
            "minecraft:obsidian", "minecraft:mob_spawner", "minecraft:chest", "minecraft:diamond_ore", "minecraft:redstone_ore",
            "minecraft:ice", "minecraft:snow", "minecraft:clay", "minecraft:pumpkin", "minecraft:melon_block", "minecraft:mycelium"};
    private static final int[] weightDefaults = {120, 80, 20, 10, 5, 20, 10, 10, 20, 40, 100, 40, 1, 5, 10, 1, 10, 1, 25,
            2, 3, 5, 5, 1, 1, 1, 8, 4, 8, 20, 5, 5, 15};
    private static final int length = blockDefaults.length;

    private static ArrayList<String> blocks = new ArrayList<String>();
    private static ArrayList<Integer> weights = new ArrayList<Integer>();

    public static void init(File file) {
        config = new Configuration(file);

        int count = config.getInt("_blockCount", "overworld", 33, 1, Integer.MAX_VALUE, "The number of blocks in this list");
        config.addCustomCategoryComment("overworld", "Be sure that the sum of the block weights is less than 2 billion or so. Otherwise things will break.");
        for(int i=0; i<count; ++i) {
            String temp = i<length ? blockDefaults[i] : "null";
            int weight = i<length ? weightDefaults[i] : 0;
            blocks.add(config.getString(String.format("block%06d", i), "overworld", temp, "The identifier for block number " + i));
            weight = config.getInt(String.format("block%06dweight", i), "overworld", weight, 0, Integer.MAX_VALUE, "The spawn rate for block number "+i);
            weights.add(weight);
        }
        if(config.hasChanged())
            config.save();
    }

    public static int size() {
        return blocks.size();
    }

    public static Block getBlock(int index) {
        String string = blocks.get(index);
        String name;
        int metaStart = string.indexOf("::");
        int nbtStart = string.indexOf('{');
        if(metaStart != -1)
            name = string.substring(0, metaStart);
        else if(nbtStart != -1)
            name = string.substring(0, nbtStart);
        else
            name = blocks.get(index);
        Block block = GameData.getBlockRegistry().getObject(name);
        if(block == BlockCache.air && !name.equals("minecraft:air"))
            SimpleSkyGrid.logger.error(String.format("Unrecognized block name: %s", name));
        return block;
    }

    public static int getMetadata(int index) {
        String string = blocks.get(index);
        int meta = 0;
        int metaStart = string.indexOf("::");
        int nbtStart = string.indexOf('{');
        if(metaStart == -1)
            return 0;
        String number = "";
        try {
            if(nbtStart != -1) {
                number = string.substring(metaStart+2, nbtStart);
                meta = Integer.decode(number);
            } else {
                number = string.substring(metaStart+2);
                meta = Integer.decode(number);
            }
        } catch(NumberFormatException e) {
            SimpleSkyGrid.logger.error(String.format("Non-numeric metadata encountered: %s", number));
            return 0;
        }
        if(meta < 0 || meta >= 16) {
            SimpleSkyGrid.logger.error(String.format("Invalid metadata encountered: %d", meta));
            return 0;
        }
        return meta;
    }

    public static NBTTagCompound getNBT(int index) {
        String string = blocks.get(index);
        int nbtStart = string.indexOf('{');
        if(nbtStart == -1)
            return null;
        return NBTString.getNBTFromString(string.substring(nbtStart));
    }

    public static int getWeight(int index) {
        return weights.get(index);
    }
}
