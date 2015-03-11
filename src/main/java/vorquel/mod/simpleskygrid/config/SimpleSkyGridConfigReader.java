package vorquel.mod.simpleskygrid.config;

import com.google.gson.stream.JsonReader;
import cpw.mods.fml.relauncher.ReflectionHelper;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.io.*;

public class SimpleSkyGridConfigReader extends JsonReader {

    private String fileName;

    public SimpleSkyGridConfigReader(File file) throws FileNotFoundException {
        super(new BufferedReader(new FileReader(file)));
        fileName = file.getName();
    }

    public void nextName(String expected) throws IOException {
        String actual = nextName();
        if(!actual.equals(expected)) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Expected \"%s\", found \"%s\" ", expected, actual));
            sb.append(String.format("at line %d, column %d of config %s", getLine(), getColumn(), fileName));
            String error = sb.toString();
            SimpleSkyGrid.logger.fatal(error);
            throw new RuntimeException(error);
        }
    }

    public int getLine() {
        int line = ReflectionHelper.getPrivateValue(JsonReader.class, this, "lineNumber");
        return line + 1;
    }

    public int getColumn() {
        int pos = ReflectionHelper.getPrivateValue(JsonReader.class, this, "pos");
        int lineStart = ReflectionHelper.getPrivateValue(JsonReader.class, this, "lineStart");
        return pos - lineStart + 1;
    }
}
