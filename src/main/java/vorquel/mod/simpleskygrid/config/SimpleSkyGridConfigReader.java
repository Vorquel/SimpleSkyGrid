package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class SimpleSkyGridConfigReader {

    private static File configHome;

    private File file;
    private String fileName;
    private JsonReader jsonReader;

    static {
        configHome = new File(Loader.instance().getConfigDir(), "SimpleSkyGrid");
        if(!configHome.exists() && !configHome.mkdir()) {
            String error = "Unable to create config directory";
            SimpleSkyGrid.logger.fatal(error);
            throw new RuntimeException(error);
        }
    }

    public static ArrayList<SimpleSkyGridConfigReader> getReaders() {
        //todo: load standard and integrated configs
        ArrayList<SimpleSkyGridConfigReader> readers = new ArrayList<>();
        Configuration configuration = new Configuration(new File(configHome, "!meta.cfg"));
        configuration.load();
        configuration.addCustomCategoryComment("general", "You shouldn't need to touch these unless you're making a custom modpack or similar.");
        boolean standards = configuration.getBoolean("use_standards", "general", true, "Use built-in standard configs.");
        boolean integrate = configuration.getBoolean("use_integration", "general", true, "Use built-in mod integration");
        configuration.save();
        if(standards) {
            //todo: copy standard configs here
        }
        if(integrate) {
            //todo: copy integrated configs here
        }
        File[] files = configHome.listFiles();
        files = files == null ? new File[0] : files;
        for(File file : files)
            readers.add(new SimpleSkyGridConfigReader(file));
        return readers;
    }

    public SimpleSkyGridConfigReader(File file) {
        this.file = file;
        fileName = file.getName();
    }

    public void open() {
        if(jsonReader != null)
            SimpleSkyGrid.logger.warn(String.format("Config file %s opened more than once", fileName));
        try {
            jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
        } catch(FileNotFoundException e) {
            String error = String.format("Unable to load config file: %s\n%s", fileName, e.getMessage());
            SimpleSkyGrid.logger.fatal(error);
            throw new RuntimeException(error);
        }
        jsonReader.setLenient(true);
    }

    public SimpleSkyGridConfigReader(String name) {
        fileName = name + ".json";
        File config = new File(configHome, fileName);
        if(!config.exists()) {
            String configHomeDir = "/assets/simpleskygrid/config/";
            URL configURL = Config.class.getResource(configHomeDir + fileName);
            try {
                FileUtils.copyURLToFile(configURL, config);
            } catch (IOException e) {
                SimpleSkyGrid.logger.fatal("Unable to copy config file: " + fileName);
                SimpleSkyGrid.logger.fatal(e.getMessage());
                throw new RuntimeException("Unable to copy config file: " + fileName + "\n" + e.getMessage());
            }
        }
    }

    public void nextName(String expected) {
        String actual = nextName();
        if(!actual.equals(expected)) {
            String error = String.format("Expected \"%s\", found \"%s\" at %s", expected, actual, getLocation());
            SimpleSkyGrid.logger.fatal(error);
            throw new RuntimeException(error);
        }
    }

    public void unknownOnce(String object, String where) {
        SimpleSkyGrid.logger.warn(String.format("Unknown %s found in %s at %s", object, where, getLocation()));
        skipValue();
    }

    public void unknownAll(String object, String where) {
        SimpleSkyGrid.logger.warn(String.format("Unknown %s found in %s at %s", object, where, getLocation()));
        while(hasNext()) {
            nextName();
            skipValue();
        }
    }

    private String getLocation() {
        return String.format("line %d, column %d of config file %s", getLine(), getColumn(), fileName);
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
            return (boolean) handleIO(e);
        }
    }

    public JsonToken peek() {
        try {
            return jsonReader.peek();
        } catch (IOException e) {
            return (JsonToken) handleIO(e);
        }
    }

    public String nextName() {
        try {
            return jsonReader.nextName();
        } catch (IOException e) {
            return (String) handleIO(e);
        }
    }

    public String nextString() {
        try {
            return jsonReader.nextString();
        } catch (IOException e) {
            return (String) handleIO(e);
        }
    }

    public boolean nextBoolean() {
        try {
            return jsonReader.nextBoolean();
        } catch (IOException e) {
            return (boolean) handleIO(e);
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
            return (double) handleIO(e);
        }
    }

    public long nextLong() {
        try {
            return jsonReader.nextLong();
        } catch (IOException e) {
            return (long) handleIO(e);
        }
    }

    public int nextInt() {
        try {
            return jsonReader.nextInt();
        } catch (IOException e) {
            return (int) handleIO(e);
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
            SimpleSkyGrid.logger.warn(String.format("Invalid metadata value %d found, assuming 0", meta));
            meta = 0;
        }
        return meta;
    }

    public NBTTagCompound nextNBT() {
        try {
            return NBT2JSON.readCompound(jsonReader);
        } catch(IOException e) {
            return (NBTTagCompound) handleIO(e);
        }
    }

    public <E extends Enum<E>> E nextSubType(Class<E> clazz) {
        String string = nextString();
        try {
            return Enum.valueOf(clazz, string);
        } catch(EnumConstantNotPresentException e) {
            String error = String.format("Unknown subtype %s at %s", string, getLocation());
            SimpleSkyGrid.logger.fatal(error);
            throw new RuntimeException(error);
        }
    }

    private Object handleIO(IOException e) {
        SimpleSkyGrid.logger.fatal("Problem reading config file: " + fileName);
        SimpleSkyGrid.logger.fatal(e.getMessage());
        throw new RuntimeException("Problem reading config file: " + fileName + "\n" + e.getMessage());
    }
}
