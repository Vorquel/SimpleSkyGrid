package vorquel.mod.simpleskygrid.config.prototype;

import net.minecraft.util.BlockPos;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;
import vorquel.mod.simpleskygrid.world.generated.random.IRandom;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;

public class PNull<T> implements IPrototype<T> {

    public static final PNull object = new PNull();
    public static final PNull<IGeneratedObject> generatedObject = new PNull<>();
    public static final IPrototype<IRandom<Integer>> count = new PNull<>();
    public static final IPrototype<IRandom<BlockPos>> point = new PNull<>();
    public static final IPrototype<ILootSource> lootSource = new PNull<>();

    private PNull() {}

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public T getObject() {
        return null;
    }
}
