package vorquel.mod.simpleskygrid.config.prototype;

import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;

public abstract class Prototype<T> implements IPrototype<T> {

    public Prototype(SimpleSkyGridConfigReader reader) {
        while(reader.hasNext())
            readLabel(reader, reader.nextName());
    }

    protected abstract void readLabel(SimpleSkyGridConfigReader reader, String label);
}
