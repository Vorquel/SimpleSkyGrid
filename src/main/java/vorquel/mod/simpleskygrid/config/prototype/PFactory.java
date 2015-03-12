package vorquel.mod.simpleskygrid.config.prototype;

import com.google.gson.stream.JsonToken;
import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.count.PNormalCount;
import vorquel.mod.simpleskygrid.config.prototype.count.PSingleCount;
import vorquel.mod.simpleskygrid.config.prototype.count.PUniformCount;
import vorquel.mod.simpleskygrid.config.prototype.generation.PBlock;
import vorquel.mod.simpleskygrid.config.prototype.generation.PEndCrystal;
import vorquel.mod.simpleskygrid.config.prototype.generation.PEndPortal;
import vorquel.mod.simpleskygrid.config.prototype.generation.PEntity;
import vorquel.mod.simpleskygrid.config.prototype.lootsource.PNative;
import vorquel.mod.simpleskygrid.config.prototype.point.PCirclePoint;
import vorquel.mod.simpleskygrid.config.prototype.point.PNormalPoint;
import vorquel.mod.simpleskygrid.config.prototype.point.PSinglePoint;
import vorquel.mod.simpleskygrid.config.prototype.point.PUniformPoint;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;

public class PFactory {

    public static IPrototype<IGeneratedObject> readGeneratedObject(SimpleSkyGridConfigReader reader) {
        IPrototype<IGeneratedObject> prototype = PNull.generatedObject;
        reader.beginObject();
        String TYPE = reader.nextName();
        if(!TYPE.equals("type")) {
            SimpleSkyGrid.logger.fatal(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
            throw new RuntimeException(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
        }
        String type = reader.nextString();
        switch (type) {
            case "label":   prototype = new PLabel<>(reader);  break;
            case "block":   prototype = new PBlock(reader);    break;
            case "entity":  prototype = new PEntity(reader);   break;
            case "special": prototype = readSpecial(reader);   break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown generation type %s in config file", type));
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.skipValue();
                }
        }
        reader.endObject();
        return prototype;
    }

    private static IPrototype<IGeneratedObject> readSpecial(SimpleSkyGridConfigReader reader) {
        IPrototype<IGeneratedObject> prototype = PNull.generatedObject;
        String NAME = reader.nextName();
        if(!NAME.equals("name")) {
            SimpleSkyGrid.logger.fatal(String.format("\"name\" expected in config file, found \"%s\"", NAME));
            throw new RuntimeException(String.format("\"name\" expected in config file, found \"%s\"", NAME));
        }
        String type = reader.nextString();
        switch (type) {
            case "end_portal":  prototype = new PEndPortal(reader);  break;
            case "end_crystal": prototype = new PEndCrystal(reader); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown special type %s in config file", type));
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.skipValue();
                }
        }
        return prototype;
    }

    public static IPrototype<IRandom<Integer>> readCount(SimpleSkyGridConfigReader reader) {
        IPrototype<IRandom<Integer>> prototype = PNull.count;
        if(reader.peek() == JsonToken.NUMBER)
            return new PSingleCount(reader);
        else {
            reader.beginObject();
            String TYPE = reader.nextName();
            if(!TYPE.equals("type")) {
                SimpleSkyGrid.logger.fatal(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
                throw new RuntimeException(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
            }
            String type = reader.nextString();
            switch (type) {
                case "normal":  prototype = new PNormalCount(reader);  break;
                case "uniform": prototype = new PUniformCount(reader); break;
                default:
                    SimpleSkyGrid.logger.warn(String.format("Unknown count type %s in config file", type));
                    while (reader.hasNext()) {
                        reader.nextName();
                        reader.skipValue();
                    }
            }
            reader.endObject();
        }
        return prototype;
    }

    public static IPrototype<IRandom<ChunkCoordinates>> readPoint(SimpleSkyGridConfigReader reader) {
        IPrototype<IRandom<ChunkCoordinates>> prototype = PNull.point;
        reader.beginObject();
        String TYPE = reader.nextName();
        if(!TYPE.equals("type")) {
            SimpleSkyGrid.logger.fatal(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
            throw new RuntimeException(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
        }
        String type = reader.nextString();
        switch (type) {
            case "point":   prototype = new PSinglePoint(reader);  break;
            case "circle":  prototype = new PCirclePoint(reader);  break;
            case "normal":  prototype = new PNormalPoint(reader);  break;
            case "uniform": prototype = new PUniformPoint(reader); break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown location type %s in config file", type));
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.skipValue();
                }
        }
        reader.endObject();
        return prototype;
    }

    public static IPrototype<ILootSource> readLootSource(SimpleSkyGridConfigReader reader) {
        IPrototype<ILootSource> prototype = PNull.lootSource;
        reader.beginObject();
        String TYPE = reader.nextName();
        if(!TYPE.equals("type")) {
            SimpleSkyGrid.logger.fatal(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
            throw new RuntimeException(String.format("\"type\" expected in config file, found \"%s\"", TYPE));
        }
        String type = reader.nextString();
        switch (type) {
            case "native":   prototype = new PNative(reader);  break;
            default:
                SimpleSkyGrid.logger.warn(String.format("Unknown location type %s in config file", type));
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.skipValue();
                }
        }
        reader.endObject();
        return prototype;
    }
}
