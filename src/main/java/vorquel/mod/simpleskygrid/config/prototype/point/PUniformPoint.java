package vorquel.mod.simpleskygrid.config.prototype.point;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;
import vorquel.mod.simpleskygrid.world.igenerated.random.point.UniformPoint;

import java.io.IOException;

public class PUniformPoint extends Prototype<IRandom<ChunkCoordinates>> { //todo

    private Integer minX;
    private Integer minY;
    private Integer minZ;
    private Integer maxX;
    private Integer maxY;
    private Integer maxZ;

    public PUniformPoint(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "min_x": minX = jsonReader.nextInt(); break;
            case "min_y": minY = jsonReader.nextInt(); break;
            case "min_z": minZ = jsonReader.nextInt(); break;
            case "max_x": maxX = jsonReader.nextInt(); break;
            case "max_y": maxY = jsonReader.nextInt(); break;
            case "max_z": maxZ = jsonReader.nextInt(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                jsonReader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return minX != null && minY != null && minZ != null && maxX != null && maxY != null && maxZ != null;
    }

    @Override
    public IRandom<ChunkCoordinates> getObject() {
        return new UniformPoint(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
