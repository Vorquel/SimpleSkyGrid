package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import vorquel.mod.simpleskygrid.helper.JSON2NBT;
import vorquel.mod.simpleskygrid.helper.Log;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class SimpleSkyGridConfigReader {

    public static boolean useStandards;
    public static boolean useIntegration;
    public static boolean skyGridDefault;
    private static File configHome;

    private File file;
    private String fileName;
    private JsonReader jsonReader;

    static {
        configHome = new File(Loader.instance().getConfigDir(), "SimpleSkyGrid");
        if(!configHome.exists() && !configHome.mkdir())
            Log.kill("Unable to create config directory");
    }

    public static void init() {
        Configuration configuration = new Configuration(new File(configHome, "!meta.cfg"));
        configuration.load();
        configuration.addCustomCategoryComment("general", "You shouldn't need to touch these unless you're making a custom modpack or similar.");
        useStandards = configuration.getBoolean("use_standards", "general", true, "Load built-in standard configs.");
        useIntegration = configuration.getBoolean("use_integration", "general", true, "Load built-in mod integration.");
        skyGridDefault = configuration.getBoolean("sky_grid_default", "general", true, "Sets Sky Grid as the default world type.");
        configuration.save();
    }

    public static ArrayList<SimpleSkyGridConfigReader> getReaders() {
        ArrayList<SimpleSkyGridConfigReader> readers = new ArrayList<>();
        if(useStandards)
            for(URL url : getConfigList("/assets/simpleskygrid/config/standards/"))
                copyFileIfNecessary(url);
        if(useIntegration)
            for(URL url : getConfigList("/assets/simpleskygrid/config/integration/"))
                if(Loader.isModLoaded(getModIdFromUrl(url)))
                    copyFileIfNecessary(url);
        File[] files = configHome.listFiles((FilenameFilter) new SuffixFileFilter(".json"));
        files = files == null ? new File[0] : files;
        for(File file : files)
            readers.add(new SimpleSkyGridConfigReader(file));
        return readers;
    }

    private static ArrayList<URL> getConfigList(String home) {
        URL source = SimpleSkyGridConfigReader.class.getResource(home + "!configList.txt");
        File destination = new File(configHome, "!configList.txt");
        ArrayList<URL> configList = new ArrayList<>();
        try {
            FileUtils.copyURLToFile(source, destination);
            for(String line : FileUtils.readLines(destination))
                configList.add(SimpleSkyGridConfigReader.class.getResource(home + line));
        } catch(IOException e) {
            Log.kill("Unable to copy temp file !configList.txt: %s", e.getMessage());
        } finally {
            if(!destination.delete())
                Log.warn("Unable to delete temporary file !configList.txt");
        }
        return configList;
    }

    private static void copyFileIfNecessary(URL source) {
        String path = source.getPath();
        String name = path.substring(path.lastIndexOf('/') + 1);
        File destination = new File(configHome, name);
        if(destination.exists())
            return;
        try {
            FileUtils.copyURLToFile(source, destination);
        } catch (IOException e) {
            Log.kill("Unable to copy file %s: %s", name, e.getMessage());
        }
    }

    private static String getModIdFromUrl(URL url) {
        String file = url.getFile();
        return file.substring(file.lastIndexOf('/') + 1, file.lastIndexOf('.'));
    }

    public SimpleSkyGridConfigReader(File file) {
        this.file = file;
        fileName = file.getName();
    }

    public void open() {
        if(jsonReader != null)
            Log.warn("Config file %s opened more than once", fileName);
        try {
            jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
        } catch(FileNotFoundException e) {
            Log.kill("Unable to load config file %s: %s", fileName, e.getMessage());
        }
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
            Log.warn("Invalid metadata value %d found, assuming 0", meta);
            meta = 0;
        }
        return meta;
    }

    public NBTTagCompound nextNBT() {
        try {
            return JSON2NBT.readCompound(jsonReader);
        } catch(IOException e) {
            return (NBTTagCompound) handleIO(e);
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

    private Object handleIO(IOException e) {
        Log.kill("Problem reading config file %s: %s", fileName, e.getMessage());
        return null;
    }
}
