package vorquel.mod.simpleskygrid.config.prototype.generation;

import com.google.gson.stream.JsonReader;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
import vorquel.mod.simpleskygrid.world.igenerated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

import java.io.IOException;

public class PBlock extends Prototype<IGeneratedObject> {

    public String name;
    public int meta = 0;
    public NBTTagCompound nbt;

    public PBlock(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "name": name = jsonReader.nextString(); break;
            case "meta":
                meta = jsonReader.nextInt();
                if(meta < 0 || meta > 15) {
                    SimpleSkyGrid.logger.warn(String.format("Invalid metadata value %d found, assuming 0", meta));
                    meta = 0;
                }
                break;
            case "nbt": nbt = NBT2JSON.toNBT(jsonReader); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in block definition in config file", label));
                jsonReader.skipValue();
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
