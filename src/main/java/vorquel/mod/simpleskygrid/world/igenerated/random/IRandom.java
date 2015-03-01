package vorquel.mod.simpleskygrid.world.igenerated.random;

import java.util.Random;

public interface IRandom<T> {
    T next(Random random);
}
