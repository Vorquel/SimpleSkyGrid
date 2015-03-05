package vorquel.mod.simpleskygrid.config.prototype.count;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;
import vorquel.mod.simpleskygrid.world.igenerated.random.count.NormalCount;

import java.io.IOException;

public class PNormalCount extends Prototype<IRandom<Integer>> {

    private double mean = Double.NaN;
    private double standardDeviation = 1;

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
        return !Double.isNaN(mean);
    }

    @Override
    public IRandom<Integer> getObject() {
        return new NormalCount(mean, standardDeviation);
    }
}
