package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

public class PBlock extends Prototype<IGeneratedObject> {

    public String name;
    public int meta;
    public NBTTagCompound nbt;
    public boolean stasis;

    public PBlock(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "name":   name   = reader.nextString();             break;
            case "meta":   meta   = reader.nextMetadata();           break;
            case "nbt":    nbt    = reader.nextNBT();                break;
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
        Block block = Block.REGISTRY.getObject(new ResourceLocation(name));
        if(block == Blocks.AIR && !name.equals("minecraft:air")) {
            Log.error("Unrecognised block name: " + name);
            return null;
        }
        if(nbt != null && !block.hasTileEntity(block.getStateFromMeta(meta)))
            Log.warn("NBT specified for block (" + name + ") without a tile entity");
        return new GeneratedBlock(block, meta, nbt, stasis);
    }
}
