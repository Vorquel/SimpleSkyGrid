package vorquel.mod.simpleskygrid.helper;

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

    static ArrayList<String> blocks = new ArrayList<String>();
    static ArrayList<Integer> metas = new ArrayList<Integer>();
    static ArrayList<Integer> weights = new ArrayList<Integer>();

    public static void init(File file) {
        config = new Configuration(file);

        int count = config.getInt("_blockCount", "overworld", 33, 1, Integer.MAX_VALUE, "The number of blocks in this list");
        config.addCustomCategoryComment("overworld", "Be sure that the sum of the block weights is less than 2 billion or so. Otherwise things will break.");
        for(int i=0; i<count; ++i) {
            String temp = i<length ? blockDefaults[i] : "null";
            int weight = i<length ? weightDefaults[i] : 0;
            temp = config.getString(String.format("block%06d", i), "overworld", temp, "The identifier for block number "+i);
            weight = config.getInt(String.format("block%06dweight", i), "overworld", weight, 0, Integer.MAX_VALUE, "The spawn rate for block number "+i);
            int place = temp.indexOf("::");
            if(place == -1) {
                blocks.add(temp);
                metas.add(0);
            } else {
                blocks.add(temp.substring(0, place));
                try {
                    int meta = Integer.decode(temp.substring(place+2));
                    if(meta >=0 && meta <= 15) {
                        metas.add(meta);
                    } else {
                        SimpleSkyGrid.logger.log(Level.ERROR, String.format("Invalid metadata %d found", meta));
                        metas.add(0);
                    }
                } catch(NumberFormatException e) {
                    SimpleSkyGrid.logger.log(Level.ERROR, "Non-numeric metadata found in config.");
                    metas.add(0);
                }
            }
            weights.add(weight);
        }
        if(config.hasChanged())
            config.save();

    }
}
