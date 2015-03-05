package vorquel.mod.simpleskygrid.config.prototype.point;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;

import java.io.IOException;

public class PUniformPoint extends Prototype<IRandom<ChunkCoordinates>> { //todo
    public PUniformPoint(JsonReader jsonReader) throws IOException {
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
