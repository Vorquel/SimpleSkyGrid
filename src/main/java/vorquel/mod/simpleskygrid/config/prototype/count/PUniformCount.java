package vorquel.mod.simpleskygrid.config.prototype.count;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.igenerated.random.IRandom;
import vorquel.mod.simpleskygrid.world.igenerated.random.count.UniformCount;

import java.io.IOException;

public class PUniformCount extends Prototype<IRandom<Integer>> {

    private Integer min;
    private Integer max;

    public PUniformCount(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "min": min = jsonReader.nextInt(); break;
            case "max": max = jsonReader.nextInt(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in count definition in config file", label));
                jsonReader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return min != null && max != null;
    }

    @Override
    public IRandom<Integer> getObject() {
        return new UniformCount(min, max);
    }
}
