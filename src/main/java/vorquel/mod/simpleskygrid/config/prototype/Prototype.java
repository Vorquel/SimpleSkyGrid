package vorquel.mod.simpleskygrid.config.prototype;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public abstract class Prototype<T> implements IPrototype<T> {

    public Prototype(JsonReader jsonReader) throws IOException {
        while(jsonReader.hasNext())
            readLabel(jsonReader, jsonReader.nextName());
    }

    protected abstract void readLabel(JsonReader jsonReader, String label) throws IOException;
}
