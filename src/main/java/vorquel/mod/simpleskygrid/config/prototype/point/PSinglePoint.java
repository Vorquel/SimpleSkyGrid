package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.SingleValue;

public class PSinglePoint extends PPoint {

    private ChunkCoordinates value;

    public PSinglePoint(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "point": value = readPoint(reader); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                reader.skipValue();
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
