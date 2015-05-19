package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.util.ChunkCoordinates;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.IPrototype;
import vorquel.mod.simpleskygrid.config.prototype.PFactory;
import vorquel.mod.simpleskygrid.config.prototype.PLabel;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.GeneratedComplex;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.util.HashMap;

public class PComplex extends Prototype<IGeneratedObject> {

    private HashMap<ChunkCoordinates, IPrototype<IGeneratedObject>> generationMap;

    public PComplex(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        if(generationMap == null) generationMap = new HashMap<>();
        ChunkCoordinates key = getChunkCoordinates(label);
        if(key != null) {
            IPrototype<IGeneratedObject> value = PFactory.readGeneratedObject(reader);
            if(value.isComplete() && !(value instanceof PLabel))
                generationMap.put(key, value);
        }
        else
            reader.unknownOnce("label " + label, "complex object definition");
    }

    private ChunkCoordinates getChunkCoordinates(String label) {
        try {
            int xyPoint = label.indexOf(',');
            int yzPoint = label.indexOf(',', xyPoint + 1);
            String xPart = label.substring(0, xyPoint);
            String yPart = label.substring(xyPoint + 1, yzPoint);
            String zPart = label.substring(yzPoint + 1);
            int x = Integer.decode(xPart);
            int y = Integer.decode(yPart);
            int z = Integer.decode(zPart);
            return new ChunkCoordinates(x, y, z);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean isComplete() {
        if(generationMap.isEmpty())
            return false;
        for(ChunkCoordinates key : generationMap.keySet())
            if(!generationMap.get(key).isComplete())
                return false;
        return true;
    }

    @Override
    public IGeneratedObject getObject() {
        GeneratedComplex complex = new GeneratedComplex();
        for(ChunkCoordinates key : generationMap.keySet()) {
            IGeneratedObject generatedObject = generationMap.get(key).getObject();
            if(generatedObject == null)
                return null;
            complex.put(key, generatedObject);
        }
        return complex;
    }
}
