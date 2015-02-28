package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

import java.io.IOException;

public class PrototypeLabel implements IPrototype {

    public Subtype subtype;
    public String name;

    public PrototypeLabel(JsonReader jsonReader) throws IOException {
        while(jsonReader.hasNext()) {
            String label = jsonReader.nextName();
            switch(label) {
                case "subtype":
                    String subtype = jsonReader.nextString();
                    try {
                        this.subtype = Subtype.valueOf(subtype);
                    } catch(EnumConstantNotPresentException ignored) {}
                    if(this.subtype == null) {
                        SimpleSkyGrid.logger.fatal(String.format("Unknown subtype encountered in config file: %s", subtype));
                        throw new RuntimeException(String.format("Unknown subtype encountered in config file: %s", subtype));
                    }
                    break;
                case "name": name = jsonReader.nextString(); break;
                default:
                    SimpleSkyGrid.logger.warn(String.format("Unknown label %s in block definition in config file", name));
                    jsonReader.skipValue();
            }
        }
    }

    @Override
    public boolean isComplete() {
        return name != null && subtype != null;
    }

    @Override
    public IGeneratedObject getGeneratedObject() {
        return null;
    }

    public static enum Subtype {
        absolute, relative
    }
}
