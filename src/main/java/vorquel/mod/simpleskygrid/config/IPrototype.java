package vorquel.mod.simpleskygrid.config;

import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

public interface IPrototype {

    public static final IPrototype NullObject = new IPrototype() {
        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public IGeneratedObject getGeneratedObject() {
            return null;
        }
    };

    boolean isComplete();
    IGeneratedObject getGeneratedObject();
}
