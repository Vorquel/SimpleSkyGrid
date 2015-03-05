package vorquel.mod.simpleskygrid.config.prototype.point;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;
import vorquel.mod.simpleskygrid.world.igenerated.random.SingleValue;

import java.io.IOException;

public class PSinglePoint extends PPoint {

    private ChunkCoordinates value;

    public PSinglePoint(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "point": value = readPoint(jsonReader); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                jsonReader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return value != null;
    }

    @Override
    public IRandom<ChunkCoordinates> getObject() {
        return new SingleValue<>(value);
    }
}
