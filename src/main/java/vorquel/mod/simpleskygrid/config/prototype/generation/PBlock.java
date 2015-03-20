package vorquel.mod.simpleskygrid.config.prototype.generation;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.config.prototype.PFactory;
import vorquel.mod.simpleskygrid.config.prototype.PNull;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;

public class PBlock extends Prototype<IGeneratedObject> {

    public String name;
    public int meta = 0;
    public NBTTagCompound nbt;
    public IPrototype<ILootSource> loot = PNull.lootSource;

    public PBlock(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "name": name = reader.nextString();             break;
            case "meta": meta = reader.nextMetadata();           break;
            case "nbt":  nbt  = reader.nextNBT();                break;
            case "loot": loot = PFactory.readLootSource(reader); break;
            default: reader.unknownOnce("label " + label, "block definition");
        }
    }

    @Override
    public boolean isComplete() {
        return name != null;
    }

    @Override
    public IGeneratedObject getObject() {
        Block block = GameData.getBlockRegistry().getObject(name);
        if(block == Blocks.air && !name.equals("minecraft:air")) {
            SimpleSkyGrid.logger.error("Unrecognised block name: " + name);
            return null;
        }
        if(nbt != null && !block.hasTileEntity(meta))
            SimpleSkyGrid.logger.warn("NBT specified for block (" + name + ") without a tile entity");
        ILootSource lootSource = null;
        if(loot.isComplete()) {
            if(block.hasTileEntity(meta) && block.createTileEntity(null, meta) instanceof IInventory)
                lootSource = loot.getObject();
            else
                SimpleSkyGrid.logger.warn("Loot specified for block (" + name + ") without an inventory");
        }
        return new GeneratedBlock(block, meta, nbt, lootSource);
    }
}
