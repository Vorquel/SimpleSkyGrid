package vorquel.mod.simpleskygrid.config.prototype.lootsource;

import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.PFactory;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;
import vorquel.mod.simpleskygrid.world.loot.LootSourceNative;

public class PNative extends Prototype<ILootSource> {

    private String source;
    private LootSourceNative.Type type;
    private IRandom<Integer> countSource;

    public PNative(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        switch(label) {
            case "subType": break; //todo
            case "name": source = reader.nextString(); break;
            case "count": countSource = PFactory.readCount(reader).getObject(); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown label %s in loot source definition in config file", label));
                reader.skipValue();
        }
    }

    @Override
    public boolean isComplete() {
        return source != null && type != null;
    }

    @Override
    public ILootSource getObject() {
        return new LootSourceNative(source, type, countSource);
    }
}
