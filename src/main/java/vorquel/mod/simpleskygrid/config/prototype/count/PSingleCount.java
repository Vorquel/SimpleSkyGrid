package vorquel.mod.simpleskygrid.config.prototype.count;

import com.google.gson.stream.JsonReader;
import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.SingleValue;

import java.io.IOException;

public class PSingleCount implements IPrototype<IRandom<Integer>> {

    int count;

    public PSingleCount(JsonReader jsonReader) throws IOException {
        count = jsonReader.nextInt();
    }

    @Override
    public boolean isComplete() {
        return count > 0;
    }

    @Override
    public IRandom<Integer> getObject() {
        return new SingleValue<>(count);
    }
}
