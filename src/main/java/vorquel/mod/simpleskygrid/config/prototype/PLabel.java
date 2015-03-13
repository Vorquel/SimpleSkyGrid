package vorquel.mod.simpleskygrid.config.prototype;

import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;

public class PLabel<T> extends Prototype<T> {

    public Subtype subtype;
    public String name;

    public PLabel(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "subtype": subtype = reader.nextSubType(Subtype.class); break;
            case "name":    name    = reader.nextString();               break;
            default: reader.unknownOnce("label " + label, "label definition");
        }
    }

    @Override
    public boolean isComplete() {
        return name != null && subtype != null;
    }

    @Override
    public T getObject() {
        return null;
    }

    public static enum Subtype {
        absolute, relative
    }
}
