package vorquel.mod.simpleskygrid.config.prototype.point;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;
import vorquel.mod.simpleskygrid.world.igenerated.random.point.CirclePoint;

import java.io.IOException;

public class PCirclePoint extends PPoint {


    private ChunkCoordinates center;
    private int radius;
    private CirclePoint.Axis axis;

    public PCirclePoint(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "center": center = readPoint(jsonReader); break;
            case "radius": radius = jsonReader.nextInt();  break;
            case "axis":   axis   = readAxis(jsonReader);  break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                jsonReader.skipValue();
        }
    }

    private CirclePoint.Axis readAxis(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        switch(string) {
            case "x": return CirclePoint.Axis.X;
            case "y": return CirclePoint.Axis.Y;
            case "z": return CirclePoint.Axis.Z;
            default:
                SimpleSkyGrid.logger.error(String.format("Bad axis %s in config", string));
                return null;
        }
    }

    @Override
    public boolean isComplete() {
        return center != null && radius > 0 && axis != null;
    }

    @Override
    public IRandom<ChunkCoordinates> getObject() {
        return new CirclePoint(center, radius, axis);
    }
}
