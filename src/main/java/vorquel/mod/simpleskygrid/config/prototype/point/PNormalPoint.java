package vorquel.mod.simpleskygrid.config.prototype.point;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;

import java.io.IOException;

public class PNormalPoint extends Prototype<IRandom<ChunkCoordinates>> {
    public PNormalPoint(JsonReader jsonReader) throws IOException { //todo
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        //todo
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public IRandom<ChunkCoordinates> getObject() {
        return null;
    }
}
