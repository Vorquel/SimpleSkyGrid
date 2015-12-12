package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.BlockPos;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.point.NormalPoint;

public class PNormalPoint extends PPoint {

    private BlockPos mean;
    private Double standardDeviationX;
    private Double standardDeviationY;
    private Double standardDeviationZ;

    public PNormalPoint(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "mean": mean = readPoint(reader); break;
            case "standard_deviation_x": standardDeviationX = reader.nextDouble(); break;
            case "standard_deviation_y": standardDeviationY = reader.nextDouble(); break;
            case "standard_deviation_z": standardDeviationZ = reader.nextDouble(); break;
            default: reader.unknownOnce("label " + label, "random normal location definition");
        }
    }

    @Override
    public boolean isComplete() {
        return mean != null && standardDeviationX != null && standardDeviationY != null && standardDeviationZ != null;
    }

    @Override
    public IRandom<BlockPos> getObject() {
        return new NormalPoint(mean, standardDeviationX, standardDeviationY, standardDeviationZ);
    }
}
