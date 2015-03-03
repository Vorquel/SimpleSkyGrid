package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;

import java.io.IOException;

public class UniqueQuantity {

    IRandom<Integer> countSource;
    IRandom<ChunkCoordinates> locationSource;

    public void readCount(JsonReader jsonReader) throws IOException {
        //todo
        jsonReader.skipValue();
    }

    public void readLocation(JsonReader jsonReader) throws IOException {
        //todo
        jsonReader.skipValue();
    }

    public boolean isComplete() {
        return countSource != null && locationSource != null;
    }
}
