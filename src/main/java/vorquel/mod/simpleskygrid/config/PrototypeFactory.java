package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.io.IOException;

public class PrototypeFactory {

    public static IPrototype readPrototype(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        String TYPE = jsonReader.nextName();
        if(!TYPE.equals("type")) {
            SimpleSkyGrid.logger.fatal(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
            throw new RuntimeException(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
        }
        IPrototype prototype = null;
        String type = jsonReader.nextString();
        switch (type) {
            case "label":  prototype = new PrototypeLabel(jsonReader);  break;
            case "block":  prototype = new PrototypeBlock(jsonReader);  break;
            case "entity": prototype = new PrototypeEntity(jsonReader); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown generation type %s in config file", type));
                while (jsonReader.hasNext()) {
                    jsonReader.nextName();
                    jsonReader.skipValue();
                }
        }
        jsonReader.endObject();
        return prototype;
    }
}
