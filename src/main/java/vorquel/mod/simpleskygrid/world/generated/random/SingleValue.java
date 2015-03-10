package vorquel.mod.simpleskygrid.world.generated.random;

import java.util.Random;

public class SingleValue<T> implements IRandom<T> {

    private final T value;

    public SingleValue(T value) {
        this.value = value;
    }

    @Override
    public T next(Random random) {
        return value;
    }
}
