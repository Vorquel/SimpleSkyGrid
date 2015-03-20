package vorquel.mod.simpleskygrid.config.prototype;

public interface IPrototype<T> {
    boolean isComplete();
    T getObject();
}
