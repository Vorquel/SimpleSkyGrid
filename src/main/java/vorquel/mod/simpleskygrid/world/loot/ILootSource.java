package vorquel.mod.simpleskygrid.world.loot;

import net.minecraft.inventory.IInventory;

import java.util.Random;

public interface ILootSource {
    void provideLoot(Random random, IInventory inventory);
}
