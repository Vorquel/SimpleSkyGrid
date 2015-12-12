package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraftforge.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.config.prototype.PFactory;
import vorquel.mod.simpleskygrid.config.prototype.PNull;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;

public class PBlock extends Prototype<IGeneratedObject> {

    public String name;
    public int meta;
    public NBTTagCompound nbt;
    public IPrototype<ILootSource> loot;
    public boolean stasis;

    public PBlock(SimpleSkyGridConfigReader reader) {
        super(reader);
        if(loot == null)
            loot = PNull.lootSource;
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "name":   name   = reader.nextString();             break;
            case "meta":   meta   = reader.nextMetadata();           break;
            case "nbt":    nbt    = reader.nextNBT();                break;
            case "loot":   loot   = PFactory.readLootSource(reader); break;
            case "stasis": stasis = reader.nextBoolean();            break;
            default: reader.unknownOnce("label " + label, "block definition");
        }
    }

    @Override
    public boolean isComplete() {
        return name != null;
    }

    @Override
    public IGeneratedObject getObject() {
        Block block = GameData.getBlockRegistry().getObject(new ResourceLocation(name));
        if(block == Blocks.air && !name.equals("minecraft:air")) {
            Log.error("Unrecognised block name: " + name);
            return null;
        }
        IBlockState state = block.getDefaultState();
        if(nbt != null && !block.hasTileEntity(state))
            Log.warn("NBT specified for block (" + name + ") without a tile entity");
        ILootSource lootSource = null;
        if(loot.isComplete()) {
            if(block.hasTileEntity(state) && block.createTileEntity(null, state) instanceof IInventory)
                lootSource = loot.getObject();
            else
                Log.warn("Loot specified for block (" + name + ") without an inventory");
        }
        return new GeneratedBlock(block, meta, nbt, lootSource, stasis);
    }
}
