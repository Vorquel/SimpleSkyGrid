package vorquel.mod.simpleskygrid.config.prototype.count;

import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.count.UniformCount;

public class PUniformCount extends Prototype<IRandom<Integer>> {

    private Integer min;
    private Integer max;

    public PUniformCount(SimpleSkyGridConfigReader jsonReader) {
        super(jsonReader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "min": min = reader.nextInt(); break;
            case "max": max = reader.nextInt(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in count definition in config file", label));
                reader.skipValue();
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
