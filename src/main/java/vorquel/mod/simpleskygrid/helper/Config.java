package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Config {

    private static HashMap<String, String[]> blockDefaults;
    private static HashMap<String, Integer[]> weightDefaults;
    private static HashMap<Integer, String> dimensionDefaults;

    private static HashMap<String, ArrayList<String>> blocks;
    private static HashMap<String, ArrayList<Integer>> weights;
    private static HashMap<Integer, WorldSettings> settings;

    static {
        blockDefaults = new HashMap<String, String[]>();
        weightDefaults = new HashMap<String, Integer[]>();
        dimensionDefaults = new HashMap<Integer, String>();
        blocks = new HashMap<String, ArrayList<String>>();
        weights = new HashMap<String, ArrayList<Integer>>();
        settings = new HashMap<Integer, WorldSettings>();

        blockDefaults.put("overworld", new String[] {"minecraft:stone", "minecraft:grass", "minecraft:dirt",
                "minecraft:water", "minecraft:lava", "minecraft:sand", "minecraft:gravel", "minecraft:gold_ore",
                "minecraft:iron_ore", "minecraft:coal_ore", "minecraft:log", "minecraft:leaves", "minecraft:glass",
                "minecraft:lapis_ore", "minecraft:sandstone", "minecraft:sticky_piston", "minecraft:web",
                "minecraft:piston", "minecraft:wool", "minecraft:tnt", "minecraft:bookshelf",
                "minecraft:mossy_cobblestone", "minecraft:obsidian", "minecraft:mob_spawner", "minecraft:chest",
                "minecraft:diamond_ore", "minecraft:redstone_ore", "minecraft:ice", "minecraft:snow", "minecraft:clay",
                "minecraft:pumpkin", "minecraft:melon_block", "minecraft:mycelium"});
        weightDefaults.put("overworld", new Integer[]{120, 80, 20, 10, 5, 20, 10, 10, 20, 40, 100, 40, 1, 5, 10, 1, 10,
                1, 25, 2, 3, 5, 5, 1, 1, 1, 8, 4, 8, 20, 5, 5, 15});
        dimensionDefaults.put(0, "$overworld");

        blockDefaults.put("nether", new String[] {"minecraft:lava", "minecraft:gravel", "minecraft:mob_spawner",
                "minecraft:chest", "minecraft:netherrack", "minecraft:soul_sand", "minecraft:glowstone",
                "minecraft:nether_brick", "minecraft:nether_brick_fence", "minecraft:nether_brick_stairs"});
        weightDefaults.put("nether", new Integer[] {50, 30, 2, 1, 300, 100, 50, 30, 10, 15});
        dimensionDefaults.put(-1, "$nether");

        blockDefaults.put("end", new String[] {"minecraft:end_stone", "minecraft:obsidian"});
        weightDefaults.put("end", new Integer[] {80, 20});
        dimensionDefaults.put(1, "$end");
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

        for(int i=0; i<labels.size(); ++i) { //TODO: topologically sort the labels
            String label = labels.get(i).substring(1);
            blocks.put(label, new ArrayList<String>());
            weights.put(label, new ArrayList<Integer>());
            int countDefault = blockDefaults.containsKey(label) ? blockDefaults.get(label).length : 1;
            int count = config.getInt("_blockCount", label, countDefault, 1, Integer.MAX_VALUE, "The number of blocks in this list");
            if(dimensionLabels.containsKey(label)) {
                //TODO: put finite dimensions and other stuff here
                settings.put(dimensionLabels.get(label), new WorldSettings(label));
            }
            for(int j=0; j<count; ++j) {
                String defaultName = getDefaultName(label, j);
                int defaultWeight = getDefaultWeight(label, j);
                String name = config.getString(String.format("block%06d", j), label, defaultName, "The identifier for block number " + j);
                int weight = config.getInt(String.format("block%06dweight", j), label, defaultWeight, 0, Integer.MAX_VALUE, "The spawn rate for block number " + j);
                blocks.get(label).add(name);
                weights.get(label).add(weight);
                if(name.startsWith("$") && !labels.contains(name))
                    labels.add(name);
            }
        }

        if(config.hasChanged())
            config.save();
    }

    private static String getDefaultName(String label, int index) {
        if(!blockDefaults.containsKey(label) || index >= blockDefaults.get(label).length)
            return "null";
        else
            return blockDefaults.get(label)[index];
    }

    private static int getDefaultWeight(String label, int index) {
        if(!weightDefaults.containsKey(label) || index >= weightDefaults.get(label).length)
            return 0;
        else
            return weightDefaults.get(label)[index];
    }

    public static Set<Integer> getDimensions() {
        return settings.keySet();
    }

    public static String getLabel(int dimension) {
        return settings.get(dimension).label;
    }

    public static int size(String label) {
        return blocks.get(label).size();
    }

    public static Block getBlock(String label, int index) {
        String string = blocks.get(label).get(index);
        String name;
        int metaStart = string.indexOf("::");
        int nbtStart = string.indexOf('{');
        if(metaStart != -1)
            name = string.substring(0, metaStart);
        else if(nbtStart != -1)
            name = string.substring(0, nbtStart);
        else
            name = string;
        Block block = GameData.getBlockRegistry().getObject(name);
        if(block == BlockCache.air && !name.equals("minecraft:air"))
            SimpleSkyGrid.logger.error(String.format("Unrecognized block name: %s", name));
        return block;
    }

    public static int getMetadata(String label, int index) {
        String string = blocks.get(label).get(index);
        int meta;
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

    public static NBTTagCompound getNBT(String label, int index) {
        String string = blocks.get(label).get(index);
        int nbtStart = string.indexOf('{');
        if(nbtStart == -1)
            return null;
        return NBTString.getNBTFromString(string.substring(nbtStart));
    }

    public static int getWeight(String label, int index) { //TODO finish specification of dimension
        return weights.get(label).get(index);
    }

    public static class WorldSettings {
        public String label;
        public WorldSettings(String label) {
            this.label = label;
        }
    }
}
