package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
import vorquel.mod.simpleskygrid.world.generated.GeneratedEntity;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

public class PEntity extends Prototype<IGeneratedObject> {

    private String name;
    private NBTTagCompound nbt;

    public PEntity(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "name": name = reader.nextString();  break;
            case "nbt": nbt = NBT2JSON.toNBT(reader); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in entity definition in config file", label));
                reader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return name != null;
    }

    @Override
    public IGeneratedObject getObject() {
        if(!EntityList.func_151515_b().contains(name)) {
            SimpleSkyGrid.logger.error("Unrecognised entity name: " + name);
            return null;
        }
        return new GeneratedEntity(name, nbt);
    }
}
