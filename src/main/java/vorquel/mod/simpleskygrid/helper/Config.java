package vorquel.mod.simpleskygrid.helper;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Config {

    private static BlockConfig defaults;
    private static HashMap<Integer, String> dimensionDefaults;

    private static BlockConfig blockConfig;
    private static HashMap<Integer, WorldSettings> settings;

    static {
        defaults = new BlockConfig();
        dimensionDefaults = new HashMap<Integer, String>();
        blockConfig = new BlockConfig();
        settings = new HashMap<Integer, WorldSettings>();

        dimensionDefaults.put(0, "%overworld");
        defaults.put("overworld", "$ground", 100);
        defaults.put("overworld", "$nature",  50);
        defaults.put("overworld", "$fluid",   10);
        defaults.put("overworld", "$ore",      5);
        defaults.put("overworld", "$spawner",  2);
        defaults.put("overworld", "$rare",     1);

        defaults.put("ground", "minecraft:stone",              1000);
        defaults.put("ground", "minecraft:grass",              1000);
        defaults.put("ground", "minecraft:dirt",                100);
        defaults.put("ground", "minecraft:gravel",              100);
        defaults.put("ground", "minecraft:sand",                100);
        defaults.put("ground", "minecraft:sandstone",            50);
        defaults.put("ground", "minecraft:clay",                 50);
        defaults.put("ground", "minecraft:hardened_clay",        50);
        defaults.put("ground", "minecraft:sand::1",              10);
        defaults.put("ground", "minecraft:snow",                 50);
        defaults.put("ground", "minecraft:ice",                  20);
        defaults.put("ground", "minecraft:packed_ice",            1);
        defaults.put("ground", "minecraft:obsidian",             20);
        defaults.put("ground", "minecraft:mycelium",              2);

        defaults.put("nature", "minecraft:log::0",             1000);
        defaults.put("nature", "minecraft:log::1",              500);
        defaults.put("nature", "minecraft:log::2",              500);
        defaults.put("nature", "minecraft:log::3",              500);
        defaults.put("nature", "minecraft:log2::0",             200);
        defaults.put("nature", "minecraft:log2::1",             200);
        defaults.put("nature", "minecraft:leaves::0",           500);
        defaults.put("nature", "minecraft:leaves::1",           250);
        defaults.put("nature", "minecraft:leaves::2",           250);
        defaults.put("nature", "minecraft:leaves::3",           250);
        defaults.put("nature", "minecraft:leaves2::0",          100);
        defaults.put("nature", "minecraft:pumpkin",              50);
        defaults.put("nature", "minecraft:melon_block",          50);
        defaults.put("nature", "minecraft:wool",                 50);
        defaults.put("nature", "minecraft:web",                  10);
        defaults.put("ground", "minecraft:red_mushroom_block",   10);
        defaults.put("ground", "minecraft:brown_mushroom_block", 10);

        defaults.put("fluid", "minecraft:water", 1000);
        defaults.put("fluid", "minecraft:lava",    50);

        defaults.put("ore", "minecraft:coal_ore",    1000);
        defaults.put("ore", "minecraft:iron_ore",     500);
        defaults.put("ore", "minecraft:redstone_ore", 200);
        defaults.put("ore", "minecraft:gold_ore",      50);
        defaults.put("ore", "minecraft:lapis_ore",     10);
        defaults.put("ore", "minecraft:diamond_ore",   10);
        defaults.put("ore", "minecraft:emerald_old",    5);

        defaults.put("spawner", spawnerNBT("Pig"), 10000);//todo finish this new list

        defaults.put("rare", "minecraft:glass",          1000);
        defaults.put("rare", "minecraft:bookshelf",       100);
        defaults.put("rare", "minecraft:noteblock",        50);
        defaults.put("rare", "minecraft:jukebox",          10);
        defaults.put("rare", "minecraft:piston",          100);
        defaults.put("rare", "minecraft:sticky_piston",   100);
        defaults.put("rare", "minecraft:chest",            50);
        defaults.put("rare", "minecraft:furnace",          50);
        defaults.put("rare", "minecraft:hopper",           20);
        defaults.put("rare", "minecraft:dropper",          20);
        defaults.put("rare", "minecraft:dispenser",        20);
        defaults.put("rare", "minecraft:enchanting_table", 10);
        defaults.put("rare", "minecraft:brewing_stand",    10);
        defaults.put("rare", "minecraft:anvil",            10);

        dimensionDefaults.put(-1, "%nether");
        defaults.put("nether", "$nether_ground",  100);
        defaults.put("nether", "$nether_nature",   10);
        defaults.put("nether", "$nether_fluid",    50);
        defaults.put("nether", "$nether_ore",      20);
        defaults.put("nether", "$nether_spawner",   5);
        defaults.put("nether", "$nether_rare",      1);

        defaults.put("nether_ground", "", 0);

        defaults.put("nether_nature", "", 0);

        defaults.put("nether_fluid", "", 0);

        defaults.put("nether_ore", "", 0);

        defaults.put("nether_spawner", "", 0);

        defaults.put("nether_rare", "", 0);

        dimensionDefaults.put(1, "%end");
        defaults.put("end", "$end_ground",  100);
        defaults.put("end", "$end_nature",    5);
        defaults.put("end", "$end_fluid",    10);
        defaults.put("end", "$end_ore",      10);
        defaults.put("end", "$end_spawner",  10);
        defaults.put("end", "$end_rare",      1);

    }

    private static String spawnerNBT(String entityName) {
        return String.format( "minecraft:mob_spawner{\"id\":\"MobSpawner\",\"Delay\":s0,\"EntityId\":\"%s\"}", entityName);
    }

    public static void init(File file) {
        Configuration config = new Configuration(file);

        int[] dimensions = config.get("general", "_dimensions", new int[] {0, -1, 1}, "The list of dimension ids that get Sky Grid generation").getIntList();
        ArrayList<String> labels = new ArrayList<String>();
        HashMap<String, Integer> dimensionLabels = new HashMap<String, Integer>();
        for(int i : dimensions) {
            String defaultName = dimensionDefaults.containsKey(i) ? dimensionDefaults.get(i) : "null";
            String name = config.getString(String.format("dimension%dname", i), "general", defaultName, "The label for this dimension");
            if(!labels.contains(name)) {
                labels.add(name);
                dimensionLabels.put(name.substring(1), i);
            }
        }

        for(int i=0; i<labels.size(); ++i) {
            String label = labels.get(i).substring(1);
            int countDefault = defaults.size(label);
            int count = config.getInt("_blockCount", label, countDefault, 0, Integer.MAX_VALUE, "The number of blocks in this list");
            if(dimensionLabels.containsKey(label)) {
                //TODO: put finite dimensions and other stuff here
                settings.put(dimensionLabels.get(label), new WorldSettings(label));
            }
            for(int j=0; j<count; ++j) {
                String defaultName = getDefaultName(label, j);
                int defaultWeight = getDefaultWeight(label, j);
                String name = config.getString(String.format("block%06d", j), label, defaultName, "The identifier for block number " + j);
                int weight = config.getInt(String.format("block%06dweight", j), label, defaultWeight, 0, Integer.MAX_VALUE, "The spawn rate for block number " + j);
                blockConfig.put(label, name, weight);
                if(name.startsWith("$") || name.startsWith("%") && !labels.contains(name))
                    labels.add(name);
            }
        }

        if(config.hasChanged())
            config.save();
    }

    private static String getDefaultName(String label, int index) {
        if(index >= defaults.size(label))
            return "null";
        else
            return defaults.getEntry(label, index);
    }

    private static int getDefaultWeight(String label, int index) {
        if(index >= defaults.size(label))
            return 0;
        else
            return defaults.getWeight(label, index);
    }

    public static Set<Integer> getDimensions() {
        return settings.keySet();
    }

    public static String getLabel(int dimension) {
        return settings.get(dimension).label;
    }

    public static int size(String label) {
        return blockConfig.size(label);
    }

    public static boolean isLabel(String label, int index) {
        return blockConfig.isLabel(label, index);
    }

    public static boolean isAbsolute(String label, int index) {
        return blockConfig.isAbsolute(label, index);
    }

    public static String getLabel(String label, int index) {
        return blockConfig.getLabel(label, index);
    }

    public static Block getBlock(String label, int index) {
        return blockConfig.getBlock(label, index);
    }

    public static int getMetadata(String label, int index) {
        return blockConfig.getMetadata(label, index);
    }

    public static NBTTagCompound getNBT(String label, int index) {
        return blockConfig.getNBT(label, index);
    }

    public static int getWeight(String label, int index) {
        return blockConfig.getWeight(label, index);
    }

    public static class WorldSettings {
        public String label;
        public WorldSettings(String label) {
            this.label = label;
        }
    }
}
