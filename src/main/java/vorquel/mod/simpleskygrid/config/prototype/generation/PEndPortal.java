package vorquel.mod.simpleskygrid.config.prototype.generation;

import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.GeneratedEndPortal;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

public class PEndPortal extends Prototype<IGeneratedObject> {

    private Double meanFilledFrames;

    public PEndPortal(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "mean_filled_frames": meanFilledFrames = reader.nextDouble(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in end portal definition in config file", label));
                reader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return meanFilledFrames != null;
    }

    @Override
    public IGeneratedObject getObject() {
        return new GeneratedEndPortal(meanFilledFrames/12d);
    }
}
