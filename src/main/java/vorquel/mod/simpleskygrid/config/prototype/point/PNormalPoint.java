package vorquel.mod.simpleskygrid.config.prototype.point;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;
import vorquel.mod.simpleskygrid.world.igenerated.random.point.NormalPoint;

import java.io.IOException;

public class PNormalPoint extends PPoint {

    private ChunkCoordinates mean;
    private Double standardDeviationX;
    private Double standardDeviationY;
    private Double standardDeviationZ;

    public PNormalPoint(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "mean": mean = readPoint(jsonReader); break;
            case "standard_deviation_x": standardDeviationX = jsonReader.nextDouble(); break;
            case "standard_deviation_y": standardDeviationY = jsonReader.nextDouble(); break;
            case "standard_deviation_z": standardDeviationZ = jsonReader.nextDouble(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                jsonReader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return mean != null && standardDeviationX != null && standardDeviationY != null && standardDeviationZ != null;
    }

    @Override
    public IRandom<ChunkCoordinates> getObject() {
        return new NormalPoint(mean, standardDeviationX, standardDeviationY, standardDeviationZ);
    }
}
