package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.math.BlockPos;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

public abstract class PPoint extends Prototype<IRandom<BlockPos>> {

    public PPoint(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    protected BlockPos readPoint(SimpleSkyGridConfigReader reader) {
        Integer x = null;
        Integer y = null;
        Integer z = null;
        reader.beginObject();
        while(reader.hasNext()) {
            String label = reader.nextName();
            switch(label) {
                case "x": x = reader.nextInt(); break;
                case "y": y = reader.nextInt(); break;
                case "z": z = reader.nextInt(); break;
                default: reader.unknownOnce("label " + label, "location definition");
            }
        }
        reader.endObject();
        if(x == null || y == null || z == null) {
            return null;
        }
        return new BlockPos(x, y, z);
    }
}
