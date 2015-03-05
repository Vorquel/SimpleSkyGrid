package vorquel.mod.simpleskygrid.config.prototype;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.io.IOException;

public class PLabel<T> extends Prototype<T> {

    public Subtype subtype;
    public String name;

    public PLabel(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "subtype":
                String subtype = jsonReader.nextString();
                try {
                    this.subtype = Subtype.valueOf(subtype);
                } catch(EnumConstantNotPresentException e) {
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

    @Override
    public boolean isComplete() {
        return name != null && subtype != null;
    }

    @Override
    public T getObject() {
        return null;
    }

    public static enum Subtype {
        absolute, relative
    }
}
