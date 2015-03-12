package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;

public abstract class PPoint extends Prototype<IRandom<ChunkCoordinates>> {

    public PPoint(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    protected ChunkCoordinates readPoint(SimpleSkyGridConfigReader reader) {
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
                default:
                    SimpleSkyGrid.logger.warn(String.format("Unknown label %s in location definition in config file", label));
                    reader.skipValue();
            }
        }
        reader.endObject();
        if(x == null || y == null || z == null) {
            return null;
        }
        return new ChunkCoordinates(x, y, z);
    }
}
