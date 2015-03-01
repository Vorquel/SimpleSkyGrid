package vorquel.mod.simpleskygrid.config;

import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

public class PrototypeEntity implements IPrototype { //todo

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public IGeneratedObject getGeneratedObject() {
        return null;
    }
}
