package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.GeneratedComplex;
import vorquel.mod.simpleskygrid.world.generated.GeneratedEntity;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

public class PEndCrystal extends Prototype<IGeneratedObject> {

    public PEndCrystal(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        reader.unknownOnce("label " + label, "end crystal definition");
        Log.warn("What are you doing? End crystals have no extra data.");
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public IGeneratedObject getObject() {
        GeneratedComplex complex = new GeneratedComplex();
        complex.put(new BlockPos(0, 0, 0), new GeneratedBlock(Blocks.BEDROCK, 0, null, false));
        complex.put(new BlockPos(0, 1, 0), new GeneratedEntity("EnderCrystal", null));
        return complex;
    }
}
