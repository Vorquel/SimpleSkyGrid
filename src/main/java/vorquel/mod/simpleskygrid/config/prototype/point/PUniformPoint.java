package vorquel.mod.simpleskygrid.config.prototype.point;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.point.UniformPoint;

public class PUniformPoint extends Prototype<IRandom<ChunkCoordinates>> {

    private Double minX;
    private Double minY;
    private Double minZ;
    private Double maxX;
    private Double maxY;
    private Double maxZ;

    public PUniformPoint(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "min_x": minX = reader.nextDouble(); break;
            case "min_y": minY = reader.nextDouble(); break;
            case "min_z": minZ = reader.nextDouble(); break;
            case "max_x": maxX = reader.nextDouble(); break;
            case "max_y": maxY = reader.nextDouble(); break;
            case "max_z": maxZ = reader.nextDouble(); break;
            default: reader.unknownOnce("label " + label, "random uniform location definition");
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
