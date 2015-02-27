package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;

import java.io.IOException;

public class PrototypeBlock implements IPrototype {

    public String name;
    public int meta = 0;
    public NBTTagCompound nbt;

    public PrototypeBlock(JsonReader jsonReader) throws IOException {
        while(jsonReader.hasNext()) {
            String label = jsonReader.nextName();
            switch(label) {
                case "name": name = jsonReader.nextString();    break;
                case "meta": meta = jsonReader.nextInt();       break;
                case "nbt":  nbt  = NBT2JSON.toNBT(jsonReader); break;
                default:
                    SimpleSkyGrid.logger.warn(String.format("Unknown label %s in block definition in config file", name));
                    jsonReader.skipValue();
            }
        }
    }
}
