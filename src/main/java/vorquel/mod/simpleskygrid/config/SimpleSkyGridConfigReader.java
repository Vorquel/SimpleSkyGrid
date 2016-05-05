package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vorquel.mod.simpleskygrid.helper.JSON2NBT;
import vorquel.mod.simpleskygrid.helper.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class SimpleSkyGridConfigReader {

    private String readerName;
    private JsonReader jsonReader;

    public SimpleSkyGridConfigReader(String name, Reader reader) {
        readerName = name;
        jsonReader = new JsonReader(new BufferedReader(reader));
        jsonReader.setLenient(true);
    }

    public void nextName(String expected) {
        String actual = nextName();
        if(!actual.equals(expected))
            Log.kill("Expected \"%s\", found \"%s\" at %s", expected, actual, getLocation());
    }

    public void unknownOnce(String object, String where) {
        Log.warn("Unknown %s found in %s at %s", object, where, getLocation());
        skipValue();
    }

    public void unknownAll(String object, String where) {
        Log.warn("Unknown %s found in %s at %s", object, where, getLocation());
        while(hasNext()) {
            nextName();
            skipValue();
        }
    }

    private String getLocation() {
        return String.format("line %d, column %d of config file %s", getLine(), getColumn(), readerName);
    }

    private int getLine() {
        int line = ReflectionHelper.getPrivateValue(JsonReader.class, jsonReader, "lineNumber");
        return line + 1;
    }

    private int getColumn() {
        int pos = ReflectionHelper.getPrivateValue(JsonReader.class, jsonReader, "pos");
        int lineStart = ReflectionHelper.getPrivateValue(JsonReader.class, jsonReader, "lineStart");
        return pos - lineStart + 1;
    }

    public void beginArray() {
        try {
            jsonReader.beginArray();
        } catch (IOException e) {
            handleIO(e);
        }
    }

    public void endArray() {
        try {
            jsonReader.endArray();
        } catch (IOException e) {
            handleIO(e);
        }
    }

    public void beginObject() {
        try {
            jsonReader.beginObject();
        } catch (IOException e) {
            handleIO(e);
        }
    }

    public void endObject() {
        try {
            jsonReader.endObject();
        } catch (IOException e) {
            handleIO(e);
        }
    }

    public boolean hasNext() {
        try {
            return jsonReader.hasNext();
        } catch (IOException e) {
            handleIO(e);
            return false;
        }
    }

    public JsonToken peek() {
        try {
            return jsonReader.peek();
        } catch (IOException e) {
            handleIO(e);
            return null;
        }
    }

    public String nextName() {
        try {
            return jsonReader.nextName();
        } catch (IOException e) {
            handleIO(e);
            return null;
        }
    }

    public String nextString() {
        try {
            return jsonReader.nextString();
        } catch (IOException e) {
            handleIO(e);
            return null;
        }
    }

    public boolean nextBoolean() {
        try {
            return jsonReader.nextBoolean();
        } catch (IOException e) {
            handleIO(e);
            return false;
        }
    }

    public void nextNull() {
        try {
            jsonReader.nextNull();
        } catch (IOException e) {
            handleIO(e);
        }
    }

    public double nextDouble() {
        try {
            return jsonReader.nextDouble();
        } catch (IOException e) {
            handleIO(e);
            return 0;
        }
    }

    public long nextLong() {
        try {
            return jsonReader.nextLong();
        } catch (IOException e) {
            handleIO(e);
            return 0;
        }
    }

    public int nextInt() {
        try {
            return jsonReader.nextInt();
        } catch (IOException e) {
            handleIO(e);
            return 0;
        }
    }

    public void close() {
        try {
            jsonReader.close();
        } catch (IOException e) {
            handleIO(e);
        }
    }

    public void skipValue() {
        try {
            jsonReader.skipValue();
        } catch (IOException e) {
            handleIO(e);
        }
    }

    public int nextMetadata() {
        int meta = nextInt();
        if(meta < 0 || meta > 15) {
            Log.warn("Invalid metadata value %d found, assuming 0", meta);
            meta = 0;
        }
        return meta;
    }

    public NBTTagCompound nextNBT() {
        try {
            return JSON2NBT.readCompound(jsonReader);
        } catch(IOException e) {
            handleIO(e);
            return null;
        }
    }

    public <E extends Enum<E>> E nextSubType(Class<E> clazz) {
        String string = nextString();
        try {
            return Enum.valueOf(clazz, string);
        } catch(EnumConstantNotPresentException e) {
            Log.kill("Unknown subtype %s at %s", string, getLocation());
            return null;
        }
    }

    private void handleIO(IOException e) {
        Log.kill("Problem reading config file %s: %s", readerName, e.getMessage());
    }
}
