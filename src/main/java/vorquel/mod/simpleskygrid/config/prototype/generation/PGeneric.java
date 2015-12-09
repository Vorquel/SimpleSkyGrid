package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.Pair;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PGeneric extends Prototype<IGeneratedObject> {

    private String name;
    private ArrayList<String> names;
    private boolean stasis;

    public PGeneric(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "name": name = reader.nextString(); return;
            case "names": readNames(reader); return;
            case "stasis": stasis = reader.nextBoolean(); return;
            default: reader.unknownOnce("label " + label, "generic block definition");
        }
    }

    private void readNames(SimpleSkyGridConfigReader reader) {
        names = new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext())
            names.add(reader.nextString());
        reader.endArray();
    }

    @Override
    public boolean isComplete() {
        return name != null || (names != null && !names.isEmpty());
    }

    @Override
    public IGeneratedObject getObject() {
        if(name != null) {
            for(ItemStack stack : OreDictionary.getOres(name)) {
                if(stack == null)
                    continue;
                Item item = stack.getItem();
                Block block = Block.getBlockFromItem(item);
                if(block == Blocks.air)
                    continue;
                int meta = item.getMetadata(stack.getMetadata());
                return new GeneratedBlock(block, meta, null, null, stasis);
            }
            if(names == null)
                return null;
        }
        Map<Pair<Block, Integer>, ArrayList<String>> blockMap = new HashMap<>();
        ArrayList<String> usedNames = new ArrayList<>();
        for(String name : names) {
            boolean isNameUsed = false;
            for(ItemStack stack : OreDictionary.getOres(name)) {
                if(stack == null)
                    continue;
                Item item = stack.getItem();
                Block block = Block.getBlockFromItem(item);
                if(block == Blocks.air)
                    continue;
                int meta = item.getMetadata(stack.getMetadata());
                Pair<Block, Integer> pair = new Pair<>(block, meta);
                if(!blockMap.containsKey(pair))
                    blockMap.put(pair, new ArrayList<String>());
                blockMap.get(pair).add(name);
                isNameUsed = true;
            }
            if(isNameUsed)
                usedNames.add(name);
        }
        if(usedNames.isEmpty())
            return null;
        for(String name : usedNames) {
            Iterator<Map.Entry<Pair<Block, Integer>, ArrayList<String>>> iterator = blockMap.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<Pair<Block, Integer>, ArrayList<String>> entry = iterator.next();
                Pair<Block, Integer> pair = entry.getKey();
                if(!entry.getValue().contains(name))
                    iterator.remove();
                if(blockMap.isEmpty())
                    return new GeneratedBlock(pair.left, pair.right, null, null, stasis);
            }
        }
        Pair<Block, Integer> pair = blockMap.keySet().iterator().next();
        return new GeneratedBlock(pair.left, pair.right, null, null, stasis);
    }
}
