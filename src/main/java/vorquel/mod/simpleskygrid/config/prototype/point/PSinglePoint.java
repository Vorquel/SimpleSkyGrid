package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.BlockPos;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.SingleValue;

public class PSinglePoint extends PPoint {

    private BlockPos value;

    public PSinglePoint(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "point": value = readPoint(reader); break;
            default: reader.unknownOnce("label " + label, "single point definition");
        }
    }

    @Override
    public boolean isComplete() {
        return value != null;
    }

    @Override
    public IRandom<BlockPos> getObject() {
        return new SingleValue<>(value);
    }
}
