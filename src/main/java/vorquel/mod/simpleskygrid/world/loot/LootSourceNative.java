package vorquel.mod.simpleskygrid.world.loot;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

import java.util.Random;

public class LootSourceNative implements ILootSource {

    private ChestGenHooks source;
    private Type type;
    private IRandom<Integer> countSource;

    public LootSourceNative(String p_source, Type p_type, IRandom<Integer> p_countSource) {
        source = ChestGenHooks.getInfo(p_source);
        if(source.getMin() == 0 && source.getMax() == 0)
            Log.warn("Native Loot Source %s never gives loot", p_source);
        type = p_type;
        if(p_countSource != null)
            countSource = p_countSource;
        else
            this.countSource = new IRandom<Integer>() {
                @Override
                public Integer next(Random random) {
                    return source.getCount(random);
                }
            };
    }

    @Override
    public void provideLoot(Random random, IInventory inventory) {
        switch(type) {
            case chest:
                WeightedRandomChestContent.generateChestContents(random, source.getItems(random), inventory, countSource.next(random));
                return;
            case dispenser:
                WeightedRandomChestContent.generateDispenserContents(random, source.getItems(random), (TileEntityDispenser) inventory, countSource.next(random));
        }
    }

    public enum Type {
        chest, dispenser
    }
}
