package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.point.CirclePoint;

public class PCirclePoint extends PPoint {


    private ChunkCoordinates center;
    private int radius;
    private CirclePoint.Axis axis;

    public PCirclePoint(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "center": center = readPoint(reader); break;
            case "radius": radius = reader.nextInt();  break;
            case "axis":   axis   = readAxis(reader);  break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                reader.skipValue();
        }
    }

    private CirclePoint.Axis readAxis(SimpleSkyGridConfigReader jsonReader) {
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
