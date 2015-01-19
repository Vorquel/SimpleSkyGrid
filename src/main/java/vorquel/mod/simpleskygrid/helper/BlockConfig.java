package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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

    public Set<String> getLabels() {
        return entries.keySet();
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

    public Block getBlock(String label, int index) {
        if(entries.containsKey(label)) {
            String entry = entries.get(label).get(index);
            String name;
            int metaStart = entry.indexOf("::");
            int nbtStart = entry.indexOf('{');
            if(metaStart != -1)
                name = entry.substring(0, metaStart);
            else if(nbtStart != -1)
                name = entry.substring(0, nbtStart);
            else
                name = entry;
            Block block = GameData.getBlockRegistry().getObject(name);
            if(block == Blocks.air && !name.equals("minecraft:air")) {
                SimpleSkyGrid.logger.error(String.format("Unrecognized block name: %s", name));
                return null;
            }
            return block;
        } else
            return null;
    }

    public int getMetadata(String label, int index) {
        if(entries.containsKey(label)) {
            String entry = entries.get(label).get(index);
            int meta;
            int metaStart = entry.indexOf("::");
            int nbtStart = entry.indexOf('{');
            if(metaStart == -1)
                return 0;
            String number = "";
            try {
                if(nbtStart != -1) {
                    number = entry.substring(metaStart+2, nbtStart);
                    meta = Integer.decode(number);
                } else {
                    number = entry.substring(metaStart+2);
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
        } else
            return 0;
    }

    public NBTTagCompound getNBT(String label, int index) {
        if(entries.containsKey(label)) {
            String entry = entries.get(label).get(index);
            int nbtStart = entry.indexOf('{');
            if(nbtStart == -1)
                return null;
            return NBTString.getNBTFromString(entry.substring(nbtStart));
        } else
            return null;
    }

    public int getWeight(String label, int index) {
        if(weights.containsKey(label))
            return weights.get(label).get(index);
        else
            return 0;
    }
}
