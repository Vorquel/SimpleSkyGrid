package vorquel.mod.simpleskygrid.config.prototype.count;

import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.count.NormalCount;

public class PNormalCount extends Prototype<IRandom<Integer>> {

    private Double mean;
    private Double standardDeviation;

    public PNormalCount(SimpleSkyGridConfigReader jsonReader) {
        super(jsonReader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "mean": mean = reader.nextDouble(); break;
            case "standard_deviation": standardDeviation = reader.nextDouble(); break;
            default: reader.unknownOnce("label " + label, "normal count definition");
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
