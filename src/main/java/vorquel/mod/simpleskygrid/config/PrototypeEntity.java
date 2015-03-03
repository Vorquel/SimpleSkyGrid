package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
import vorquel.mod.simpleskygrid.world.igenerated.GeneratedEntity;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

import java.io.IOException;

public class PrototypeEntity implements IPrototype {

    private String name;
    private NBTTagCompound nbt;

    public PrototypeEntity(JsonReader jsonReader) throws IOException {
        while(jsonReader.hasNext()) {
            String label = jsonReader.nextName();
            switch(label) {
                case "name": name = jsonReader.nextString();  break;
                case "nbt": nbt = NBT2JSON.toNBT(jsonReader); break;
                default:
                    SimpleSkyGrid.logger.warn(String.format("Unknown label %s in entity definition in config file", name));
                    jsonReader.skipValue();
            }
        }
    }

    @Override
    public boolean isComplete() {
        return name != null;
    }

    @Override
    public IGeneratedObject getGeneratedObject() {
        if(!EntityList.func_151515_b().contains(name)) {
            SimpleSkyGrid.logger.error("Unrecognised entity name: " + name);
            return null;
        }
        return new GeneratedEntity(name, nbt);
    }
}
