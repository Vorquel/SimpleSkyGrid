package vorquel.mod.simpleskygrid.config;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.xml.internal.ws.util.StreamUtils;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import vorquel.mod.simpleskygrid.helper.Log;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class SimpleSkyGridConfigReaders implements Iterable<SimpleSkyGridConfigReader> {

    private static SimpleSkyGridConfigReaders that = new SimpleSkyGridConfigReaders();

    public static boolean useStandards;
    public static boolean useIntegration;
    public static boolean skyGridDefault;

    private static File configHome;

    private static final String[] illegalNames = {"CON", "PRN", "AUX", "NUL",
                                                         "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
                                                         "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

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

    public static SimpleSkyGridConfigReaders get() {
        return that;
    }

    private static ArrayList<String> getConfigList(String path) {
        URL source = SimpleSkyGridConfigReader.class.getResource("/assets/simpleskygrid/config/" + path + "/!configList.txt");
        File destination = new File(configHome, "!configList.txt");
        ArrayList<String> configList = new ArrayList<>();
        try {
            FileUtils.copyURLToFile(source, destination);
            configList.addAll(FileUtils.readLines(destination));
        } catch(IOException e) {
            Log.kill("Unable to copy temp file !configList.txt: %s", e.getMessage());
        } finally {
            if(!destination.delete())
                Log.warn("Unable to delete temporary file !configList.txt");
        }
        return configList;
    }

    private static void copyFileIfNecessary(String path, String name) {
        String fileName = name + ".json";
        URL source = SimpleSkyGridConfigReader.class.getResource("/assets/simpleskygrid/config/" + path + "/" + fileName);
        File destination = new File(configHome, fileName);
        try {
            String hash = getHash(source.openStream());
            if(shouldOverwrite(destination, hash))
                copy(source, destination, hash);
        } catch (IOException e) {
            Log.error("Unable to copy file %s: %s", name, e.getMessage());
        }
    }
    
    private static String getHash(InputStream stream) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            StringBuilder sb = new StringBuilder();
            for(byte b : digest.digest(IOUtils.toByteArray(stream))) {
                int a = b;
                a = a < 0 ? a + 256 : a;
                sb.append(Character.forDigit(a >> 4, 16));
                sb.append(Character.forDigit(a & 15, 16));
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException ignore) {
        } finally {
            stream.close();
        }
        return "";
    }
    
    private static boolean shouldOverwrite(File destination, String sourceHash) {
        InputStream stream = null;
        try {
            stream = FileUtils.openInputStream(destination);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String header = reader.readLine();
            if(!header.startsWith("// sha256: "))
                return true;
            String declaredHash = header.substring(11, 76);
            if(sourceHash.equals(declaredHash))
                return false;
            String destHash = getHash(stream);
            return !sourceHash.equals(destHash) && declaredHash.equals(destHash);
        } catch(IOException e) {
            return true;
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
    
    private static void copy(URL source, File destination, String sha256) throws IOException {
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            writer = new FileWriter(destination);
            writer.write(String.format("// sha256: %s *DO NOT ALTER OR REMOVE THIS LINE - YOUR CHANGES WILL BE LOST*\n", sha256));
            reader = new BufferedReader(new InputStreamReader(source.openStream()));
            String nextLine;
            while((nextLine = reader.readLine()) != null)
                writer.write(nextLine + "\n");
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
    }
    
    private static String encodeModId(String modId) {
        modId = modId.replace("%", encodeChar('%'));
        for(char c : modId.toCharArray()) {
            if(c != '%' && !Character.isAlphabetic(c) && !Character.isDigit(c)) {
                modId = modId.replace(String.valueOf(c), encodeChar(c));
            }
        }
        if(isIllegal(modId))
            modId = modId.concat("%");
        return modId;
    }

    private static String encodeChar(char c) {
        if(c < 256)
            return "%" + Strings.padStart(Integer.toHexString(c), 2, '0');
        else
            return "%%" + Strings.padStart(Integer.toHexString(c), 4, '0');
    }

    private static boolean isIllegal(String name) {
        for(String illegalName : illegalNames)
            if(illegalName.equalsIgnoreCase(name))
                return true;
        return false;
    }

    private SimpleSkyGridConfigReaders() {}

    @Override
    public Iterator<SimpleSkyGridConfigReader> iterator() {
        return new Iterator<SimpleSkyGridConfigReader>() {

            private Iterator<File> configFiles;

            {
                if(useStandards)
                    for(String string : getConfigList("standards"))
                        copyFileIfNecessary("standards", string);
                if(useIntegration)
                    for(String modId : getConfigList("integration"))
                        if(Loader.isModLoaded(modId))
                            copyFileIfNecessary("integration", encodeModId(modId));
                configFiles = Arrays.asList(configHome.listFiles((FilenameFilter) new SuffixFileFilter(".json"))).iterator();
            }

            @Override
            public boolean hasNext() {
                return configFiles.hasNext();
            }

            @Override
            public SimpleSkyGridConfigReader next() {
                File file = configFiles.next();
                String name = file.getName();
                try {
                    return new SimpleSkyGridConfigReader(name, new FileReader(file));
                } catch (FileNotFoundException e) {
                    Log.kill("Unable to load config file %s: %s", name, e.getMessage());
                }
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
