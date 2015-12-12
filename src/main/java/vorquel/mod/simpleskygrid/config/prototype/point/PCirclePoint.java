package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.BlockPos;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.point.CirclePoint;

public class PCirclePoint extends PPoint {


    private BlockPos center;
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
            default: reader.unknownOnce("label " + label, "random circle location definition");
        }
    }

    private CirclePoint.Axis readAxis(SimpleSkyGridConfigReader jsonReader) {
        String string = jsonReader.nextString();
        switch(string) {
            case "x": return CirclePoint.Axis.X;
            case "y": return CirclePoint.Axis.Y;
            case "z": return CirclePoint.Axis.Z;
            default:
                Log.error("Bad axis %s in config", string);
                return null;
        }
    }

    @Override
    public boolean isComplete() {
        return center != null && radius > 0 && axis != null;
    }

    @Override
    public IRandom<BlockPos> getObject() {
        return new CirclePoint(center, radius, axis);
    }
}
