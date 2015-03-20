package vorquel.mod.simpleskygrid.config.prototype.count;

import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.generated.random.SingleValue;

public class PSingleCount implements IPrototype<IRandom<Integer>> {

    int count;

    public PSingleCount(SimpleSkyGridConfigReader reader) {
        count = reader.nextInt();
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
