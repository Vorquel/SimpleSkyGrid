package vorquel.mod.simpleskygrid.config.prototype.generation;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.GeneratedEndPortal;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.io.IOException;

public class PEndPortal extends Prototype<IGeneratedObject> {

    private Double meanFilledFrames;

    public PEndPortal(JsonReader jsonReader) throws IOException {
        super(jsonReader);
    }

    @Override
    protected void readLabel(JsonReader jsonReader, String label) throws IOException {
        switch(label) {
            case "mean_filled_frames": meanFilledFrames = jsonReader.nextDouble(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in end portal definition in config file", label));
                jsonReader.skipValue();
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
