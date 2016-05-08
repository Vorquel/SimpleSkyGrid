package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.Log;
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
            case "name": name = reader.nextString(); break;
            case "nbt":  nbt  = reader.nextNBT();    break;
            default: reader.unknownOnce("label " + label, "entity definition");
        }
    }

    @Override
    public boolean isComplete() {
        return name != null;
    }

    @Override
    public IGeneratedObject getObject() {
        if(!EntityList.isStringValidEntityName(name)) {
            Log.error("Unrecognised entity name: " + name);
            return null;
        }
        return new GeneratedEntity(name, nbt);
    }
}
