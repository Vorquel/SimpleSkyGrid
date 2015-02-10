package vorquel.mod.simpleskygrid.helper;

import net.minecraftforge.common.config.Configuration;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Config {

    private static BlockConfig defaults;
    private static HashMap<Integer, WorldSettings> settingsDefaults;

    private static BlockConfig blockConfig;
    private static HashMap<Integer, WorldSettings> settings;

    static { //todo: Find balance for new config
        defaults = new BlockConfig();
        settingsDefaults = new HashMap<Integer, WorldSettings>();
        blockConfig = new BlockConfig();
        settings = new HashMap<Integer, WorldSettings>();

        settingsDefaults.put(0, new WorldSettings("$overworld"));
        defaults.put("overworld", "minecraft:stone",    120);
        defaults.put("overworld", "minecraft:grass",     80);
        defaults.put("overworld", "minecraft:dirt",      20);
        defaults.put("overworld", "minecraft:sand",      20);
        defaults.put("overworld", "minecraft:clay",      20);
        defaults.put("overworld", "minecraft:mycelium",  15);
        defaults.put("overworld", "minecraft:sandstone", 10);
        defaults.put("overworld", "minecraft:gravel",    10);
        defaults.put("overworld", "minecraft:coal_ore",    40);
        defaults.put("overworld", "minecraft:iron_ore",    20);
        defaults.put("overworld", "minecraft:gold_ore",    10);
        defaults.put("overworld", "minecraft:redstone_ore", 8);
        defaults.put("overworld", "minecraft:lapis_ore",    5);
        defaults.put("overworld", "minecraft:diamond_ore",  1);
        defaults.put("overworld", "minecraft:log",       100);
        defaults.put("overworld", "minecraft:leaves",     40);
        defaults.put("overworld", "minecraft:wool",       25);
        defaults.put("overworld", "minecraft:web",        10);
        defaults.put("overworld", "minecraft:pumpkin",     5);
        defaults.put("overworld", "minecraft:melon_block", 5);
        defaults.put("overworld", "minecraft:water", 10);
        defaults.put("overworld", "minecraft:lava",   5);
        defaults.put("overworld", "minecraft:snow", 8);
        defaults.put("overworld", "minecraft:ice",  4);
        defaults.put("overworld", "minecraft:mossy_cobblestone", 5);
        defaults.put("overworld", "minecraft:obsidian",          5);
        defaults.put("overworld", "minecraft:bookshelf",         3);
        defaults.put("overworld", "minecraft:tnt",               2);
        defaults.put("overworld", "minecraft:glass",             1);
        defaults.put("overworld", "minecraft:piston",            1);
        defaults.put("overworld", "minecraft:sticky_piston",     1);
        defaults.put("overworld", "minecraft:chest",             1);
        defaults.put("overworld", "%overworld_spawner",          1);

        defaults.put("overworld_spawner", spawnerNBT("Creeper"),    1);
        defaults.put("overworld_spawner", spawnerNBT("Skeleton"),   1);
        defaults.put("overworld_spawner", spawnerNBT("Spider"),     1);
        defaults.put("overworld_spawner", spawnerNBT("CaveSpider"), 1);
        defaults.put("overworld_spawner", spawnerNBT("Zombie"),     1);
        defaults.put("overworld_spawner", spawnerNBT("Slime"),      1);
        defaults.put("overworld_spawner", spawnerNBT("Pig"),        1);
        defaults.put("overworld_spawner", spawnerNBT("Sheep"),      1);
        defaults.put("overworld_spawner", spawnerNBT("Cow"),        1);
        defaults.put("overworld_spawner", spawnerNBT("Chicken"),    1);
        defaults.put("overworld_spawner", spawnerNBT("Squid"),      1);
        defaults.put("overworld_spawner", spawnerNBT("Wolf"),       1);
        defaults.put("overworld_spawner", spawnerNBT("Enderman"),   1);
        defaults.put("overworld_spawner", spawnerNBT("Silverfish"), 1);
        defaults.put("overworld_spawner", spawnerNBT("Villager"),   1);

        settingsDefaults.put(-1, new WorldSettings("$nether"));
        defaults.put("nether", "minecraft:netherrack",         300);
        defaults.put("nether", "minecraft:soul_sand",          100);
        defaults.put("nether", "minecraft:glowstone",           50);
        defaults.put("nether", "minecraft:lava",                50);
        defaults.put("nether", "minecraft:gravel",              30);
        defaults.put("nether", "minecraft:nether_brick",        30);
        defaults.put("nether", "minecraft:nether_brick_stairs", 15);
        defaults.put("nether", "minecraft:nether_brick_fence",  10);
        defaults.put("nether", "minecraft:chest",                1);
        defaults.put("nether", "minecraft:%nether_spawner",      2);

        defaults.put("nether_spawner", spawnerNBT("PigZombie"), 1);
        defaults.put("nether_spawner", spawnerNBT("Blaze"),     1);
        defaults.put("nether_spawner", spawnerNBT("LavaSlime"), 1);

        settingsDefaults.put(1, new WorldSettings("$end"));
        settingsDefaults.get(1).radius = 16;
        defaults.put("end", "minecraft:end_stone", 400);
        defaults.put("end", "minecraft:obsidian",  100);
        defaults.put("end", "%end_spawner", 1);

        defaults.put("end_spawner", spawnerNBT("Enderman"), 1);
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
            String defaultName = settingsDefaults.containsKey(i) ? settingsDefaults.get(i).label : "dim" + i;
            String name = config.getString(String.format("dimension%dname", i), "general", defaultName, "The label for this dimension");
            if(!labels.contains(name)) {
                labels.add(name);
                dimensionLabels.put(name.substring(1), i);
            }
        }

        Ref.spawnHeight = config.getInt("spawnHeight", "general", 65, 1, 256, "The height at which you spawn in the world");

        for(int i=0; i<labels.size(); ++i) {
            String label = labels.get(i).substring(1);
            int countDefault = defaults.size(label);
            int count = config.getInt("_blockCount", label, countDefault, 0, Integer.MAX_VALUE, "The number of blocks in this list");
            if(dimensionLabels.containsKey(label)) {
                WorldSettings worldSettings = new WorldSettings(label);
                worldSettings.height = config.getInt("_worldHeight", label, getDefaultHeight(dimensionLabels.get(label)), 1, 256, "The height for this dimension");
                worldSettings.radius = config.getInt("_worldRadius", label, getDefaultRadius(dimensionLabels.get(label)), -1, Integer.MAX_VALUE, "The radius of this dimension in chunks. (-1 is infinite)");
                settings.put(dimensionLabels.get(label), worldSettings);
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

    private static int getDefaultHeight(int dim) {
        if(settingsDefaults.containsKey(dim))
            return settingsDefaults.get(dim).height;
        else
            return 128;
    }

    private static int getDefaultRadius(int dim) {
        if(settingsDefaults.containsKey(dim))
            return settingsDefaults.get(dim).radius;
        else
            return -1;
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

    public static WorldSettings getSettings(int dimension) {
        return settings.get(dimension);
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

    public static IGeneratedObject getIGeneratedObject(String label, int index) {
        return blockConfig.getIGeneratedObject(label, index);
    }

    public static int getWeight(String label, int index) {
        return blockConfig.getWeight(label, index);
    }

    public static class WorldSettings {
        public String label;
        public int radius = -1;
        public int height = 128;
        public WorldSettings(String label) {
            this.label = label;
        }
        public boolean isFinite() {
            return radius != -1;
        }
        public boolean inRadius(int xChunk, int zChunk) {
            xChunk = xChunk < 0 ? -xChunk : xChunk+1;
            zChunk = zChunk < 0 ? -zChunk : zChunk+1;
            return xChunk <= radius && zChunk <= radius;
        }
    }
}
