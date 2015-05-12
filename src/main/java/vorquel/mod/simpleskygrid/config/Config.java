package vorquel.mod.simpleskygrid.config;

import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.config.prototype.PFactory;
import vorquel.mod.simpleskygrid.config.prototype.PNull;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.util.HashMap;

public class Config {

    public static HashMap<Integer, DimensionProperties> dimensionPropertiesMap = new HashMap<>();
    public static ConfigDataMap<IPrototype<IGeneratedObject>, Double> generationData;
    public static ConfigDataMap<IPrototype<IGeneratedObject>, UniqueQuantity> uniqueGenData;

    public static void loadConfigs() {
        generationData = new ConfigDataMap<>();
        uniqueGenData = new ConfigDataMap<>();
        for(SimpleSkyGridConfigReader reader : SimpleSkyGridConfigReader.getReaders()) {
            reader.open();
            reader.beginObject();
            while(reader.hasNext()) {
                String name = reader.nextName();
                switch(name) {
                    case "generation": readGeneration(reader); break;
                    case "unique_gen": readUniqueGen(reader);  break;
                    default:
                        if(name.startsWith("dim"))
                            readDimension(reader, name);
                        else {
                            reader.unknownOnce("label " + name, "top level objects");
                        }
                }
            }
            reader.endObject();
            reader.close();
        }
    }

    private static void readDimension(SimpleSkyGridConfigReader reader, String dimName) {
        int dim;
        try {
            dim = Integer.decode(dimName.substring(3));
        } catch(NumberFormatException e) {
            reader.unknownOnce("label " + dimName, "top level");
            return;
        }
        DimensionProperties prop = new DimensionProperties();
        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            switch(name) {
                case "height":         prop.height             = reader.nextInt();    break;
                case "radius":         prop.radius             = reader.nextInt();    break;
                case "spawn_height":   prop.spawnHeight        = reader.nextInt();    break;
                case "generation":     prop.generationLabel    = reader.nextString(); break;
                case "unique_gen":     prop.uniqueGenLabel     = reader.nextString(); break;
                default: reader.unknownOnce("label " + name, "dimension definition");
            }
        }
        reader.endObject();
        if(prop.height == -1 || prop.generationLabel == null) {
            Log.kill("Underspecified dimension %d in config file", dim);
        }
        dimensionPropertiesMap.put(dim, prop);
    }

    private static void readGeneration(SimpleSkyGridConfigReader reader) {
        reader.beginObject();
        while(reader.hasNext()) {
            String label = reader.nextName();
            reader.beginArray();
            while(reader.hasNext()) {
                reader.beginObject();
                IPrototype<IGeneratedObject> prototype = PNull.generatedObject;
                double weight = 0;
                while(reader.hasNext()) {
                    String innerLabel = reader.nextName();
                    switch(innerLabel) {
                        case "object": prototype = PFactory.readGeneratedObject(reader); break;
                        case "weight": weight    = readWeight(reader);                   break;
                        default: reader.unknownOnce("label " + label, "generation definition");
                    }
                }
                if(prototype.isComplete() && weight > 0)
                    generationData.put(label, prototype, weight);
                reader.endObject();
            }
            reader.endArray();
        }
        reader.endObject();
    }

    private static void readUniqueGen(SimpleSkyGridConfigReader reader) {
        reader.beginObject();
        while(reader.hasNext()) {
            String label = reader.nextName();
            reader.beginArray();
            while(reader.hasNext()) {
                reader.beginObject();
                IPrototype<IGeneratedObject> prototype = PNull.generatedObject;
                UniqueQuantity quantity = new UniqueQuantity();
                while(reader.hasNext()) {
                    String innerLabel = reader.nextName();
                    switch(innerLabel) {
                        case "object":   prototype = PFactory.readGeneratedObject(reader); break;
                        case "count":    quantity.countSource = PFactory.readCount(reader).getObject(); break;
                        case "location": quantity.pointSource = PFactory.readPoint(reader).getObject(); break;
                        default:
                            Log.warn("Unknown uniqueGen label %s in config file", innerLabel);
                            reader.skipValue();
                    }
                }
                if(prototype.isComplete() && quantity.isComplete())
                    uniqueGenData.put(label, prototype, quantity);
                reader.endObject();
            }
            reader.endArray();
        }
        reader.endObject();
    }

    private static double readWeight(SimpleSkyGridConfigReader reader) {
        double weight = reader.nextDouble();
        if(weight < 0) {
            Log.error("Negative weight in config file");
            weight = 0;
        } else if(Double.isInfinite(weight) || Double.isNaN(weight)) {
            Log.error("Crazy weight in config file");
            weight = 0;
        }
        return weight;
    }

    public static class DimensionProperties {
        public int    height             = -1;
        public int    radius             = -1;
        public int    spawnHeight        = 65;
        public String generationLabel    = null;
        public String uniqueGenLabel     = null;

        public boolean isFinite() {
            return radius != -1;
        }

        public boolean notInRadius(int xChunk, int zChunk) {
            int xAbs = xChunk < 0 ? -xChunk : xChunk + 1;
            int zAbs = zChunk < 0 ? -zChunk : zChunk + 1;
            return xAbs > radius || zAbs > radius;
        }
    }
}
