package vorquel.mod.simpleskygrid.config.prototype.generation;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

public class PBlock extends Prototype<IGeneratedObject> {

    public String name;
    public int meta = 0;
    public NBTTagCompound nbt;

    public PBlock(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "name": name = reader.nextString(); break;
            case "meta":
                meta = reader.nextInt();
                if(meta < 0 || meta > 15) {
                    SimpleSkyGrid.logger.warn(String.format("Invalid metadata value %d found, assuming 0", meta));
                    meta = 0;
                }
                break;
            case "nbt": nbt = NBT2JSON.toNBT(reader); break;
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
        return new GeneratedBlock(block, meta, nbt);
    }
}
