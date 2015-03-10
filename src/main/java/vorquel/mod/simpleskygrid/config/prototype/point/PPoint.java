package vorquel.mod.simpleskygrid.config.prototype.point;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

import java.io.IOException;

public abstract class PPoint extends Prototype<IRandom<ChunkCoordinates>> {

    public PPoint(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    protected ChunkCoordinates readPoint(JsonReader jsonReader) throws IOException {
        Integer x = null;
        Integer y = null;
        Integer z = null;
        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            String label = jsonReader.nextName();
            switch(label) {
                case "x": x = jsonReader.nextInt(); break;
                case "y": y = jsonReader.nextInt(); break;
                case "z": z = jsonReader.nextInt(); break;
                default:
                    SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                    jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        if(x == null || y == null || z == null) {
            return null;
        }
        return new ChunkCoordinates(x, y, z);
    }
}
