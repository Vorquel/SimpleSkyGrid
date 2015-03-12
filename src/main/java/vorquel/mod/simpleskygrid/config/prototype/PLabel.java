package vorquel.mod.simpleskygrid.config.prototype;

import vorquel.mod.simpleskygrid.SimpleSkyGrid;
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
            case "subtype":
                String subtype = reader.nextString();
                try {
                    this.subtype = Subtype.valueOf(subtype);
                } catch(EnumConstantNotPresentException e) {
                    SimpleSkyGrid.logger.fatal(String.format("Unknown subtype encountered in config file: %s", subtype));
                    throw new RuntimeException(String.format("Unknown subtype encountered in config file: %s", subtype));
                }
                break;
            case "name": name = reader.nextString(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in block definition in config file", name));
                reader.skipValue();
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
