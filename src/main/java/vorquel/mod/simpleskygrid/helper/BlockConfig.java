package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.world.igenerated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.igenerated.GeneratedEntity;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockConfig {
    private HashMap<String, ArrayList<String>> entries = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<Integer>> weights = new HashMap<String, ArrayList<Integer>>();

    public void put(String label, String entry, int weight) {
        ArrayList<String> entryCache;
        ArrayList<Integer> weightCache;
        if(entries.containsKey(label)) {
            entryCache = entries.get(label);
            weightCache = weights.get(label);
        } else {
            entryCache = new ArrayList<String>();
            weightCache = new ArrayList<Integer>();
            entries.put(label, entryCache);
            weights.put(label, weightCache);
        }
        entryCache.add(entry);
        weightCache.add(weight);
    }

    public int size(String label) {
        if(entries.containsKey(label))
            return entries.get(label).size();
        else
            return 0;
    }

    public String getEntry(String label, int index) {
        if(entries.containsKey(label))
            return entries.get(label).get(index);
        else
            return null;
    }

    public boolean isLabel(String label, int index) {
        if(entries.containsKey(label)) {
            String entry = entries.get(label).get(index);
            return entry.startsWith("$") || entry.startsWith("%");
        } else
            return false;
    }

    public boolean isAbsolute(String label, int index) {
        if(entries.containsKey(label)) {
            String entry = entries.get(label).get(index);
            return entry.startsWith("$");
        }
        return false;
    }

    public String getLabel(String label, int index) {
        if(entries.containsKey(label)) {
            String entry = entries.get(label).get(index);
            if(entry.startsWith("$") || entry.startsWith("%"))
                return entry.substring(1);
            else
                return entry;
        } else
            return null;
    }

    public IGeneratedObject getIGeneratedObject(String label, int index) {
        String entry = entries.get(label).get(index);
        if(entry.equals("")) {
            SimpleSkyGrid.logger.error(String.format("Empty label: Section %s, entry %d", label, index));
            return null;
        } else if(entry.startsWith("@")) {
            int nbtStart = entry.indexOf('{');
            String name;
            NBTTagCompound nbt = null;
            if(nbtStart == -1) {
                name = entry.substring(1);
            } else {
                name = entry.substring(1, nbtStart);
                nbt = NBTString.getNBTFromString(entry.substring(nbtStart));
            }
            if(EntityList.stringToClassMapping.containsKey(name))
                return new GeneratedEntity(name, nbt);
            else {
                SimpleSkyGrid.logger.error(String.format("Unrecognized Entity name: %s", name));
                return null;
            }
        } else {
            int metaStart = entry.indexOf("::");
            int nbtStart = entry.indexOf('{');
            String blockName;
            String metaString = null;
            String nbtString = null;
            if(metaStart != -1) {
                blockName = entry.substring(0, metaStart);
                if(nbtStart != -1) {
                    metaString = entry.substring(metaStart, nbtStart);
                    nbtString = entry.substring(nbtStart);
                } else
                    metaString = entry.substring(metaStart);
            } else {
                if(nbtStart != -1) {
                    blockName = entry.substring(0, nbtStart);
                    nbtString = entry.substring(nbtStart);
                } else
                    blockName = entry;
           }
            Block block = getBlock(blockName);
            int metadata = getMetadata(metaString);
            NBTTagCompound nbt = getNBT(nbtString);
            return new GeneratedBlock(block, metadata, nbt);
        }
    }

    private Block getBlock(String blockName) {
        Block block = GameData.getBlockRegistry().getObject(blockName);
        if(block == Blocks.air && !blockName.equals("minecraft:air")) {
            SimpleSkyGrid.logger.error(String.format("Unrecognized block name: %s", blockName));
            return null;
        }
        return block;
    }

    private int getMetadata(String numberString) {
        if(numberString == null)
            return 0;
        int meta;
        try {
            meta = Integer.decode(numberString);
        } catch(NumberFormatException e) {
            SimpleSkyGrid.logger.error(String.format("Non-numeric metadata encountered: %s", numberString));
            return 0;
        }
        if(meta < 0 || meta >= 16) {
            SimpleSkyGrid.logger.error(String.format("Invalid metadata encountered: %d", meta));
            return 0;
        }
        return meta;
    }

    private NBTTagCompound getNBT(String nbtString) {
        if(nbtString == null)
            return null;
        else
            return NBTString.getNBTFromString(nbtString);
    }

    public int getWeight(String label, int index) {
        if(weights.containsKey(label))
            return weights.get(label).get(index);
        else
            return 0;
    }
}
