package vorquel.mod.simpleskygrid.config.prototype.count;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.count.NormalCount;

import java.io.IOException;

public class PNormalCount extends Prototype<IRandom<Integer>> {

    private Double mean;
    private Double standardDeviation;

    public PNormalCount(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "mean": mean = jsonReader.nextDouble(); break;
            case "standard_deviation": standardDeviation = jsonReader.nextDouble(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in count definition in config file", label));
                jsonReader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return mean != null && standardDeviation != null;
    }

    @Override
    public IRandom<Integer> getObject() {
        return new NormalCount(mean, standardDeviation);
    }
}
