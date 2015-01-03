package vorquel.mod.simpleskygrid.helper;

import net.minecraft.nbt.*;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NBTString {

    public static void sanitizeNBT(NBTTagCompound tag) {
        tag.removeTag("x");
        tag.removeTag("y");
        tag.removeTag("z");
    }

    public static void localizeNBT(NBTTagCompound tag, int x, int y, int z) {
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
    }

    public static NBTTagCompound getNBTFromString(String in) {
        ArrayList<Token> tokens = tokenize(in);
        return readTokens(tokens);
    }

    private static ArrayList<Token> tokenize(String in) {
        return null;
    }

    private static NBTTagCompound readTokens(ArrayList<Token> tokens) {
        return null;
    }

    public static String getStringFromNBT(NBTTagCompound in) {
        StringBuilder out = new StringBuilder();
        appendCompound(out, in);
        return out.toString();
    }

    private static void appendTag(StringBuilder out, NBTBase in) {
        switch(in.getId()) {
            case 1: appendByte(out,           (NBTTagByte) in); break;
            case 2: appendShort(out,         (NBTTagShort) in); break;
            case 3: appendInt(out,             (NBTTagInt) in); break;
            case 4: appendLong(out,           (NBTTagLong) in); break;
            case 5: appendFloat(out,         (NBTTagFloat) in); break;
            case 6: appendDouble(out,       (NBTTagDouble) in); break;
            case 7: appendByteArray(out, (NBTTagByteArray) in); break;
            case 8: appendString(out,       (NBTTagString) in); break;
            case 9: appendList(out,           (NBTTagList) in); break;
            case 10: appendCompound(out,  (NBTTagCompound) in); break;
            case 11: appendIntArray(out,  (NBTTagIntArray) in); break;
        }
    }

    private static void appendByte(StringBuilder out, NBTTagByte in) {
        out.append('b');
        out.append(in.func_150290_f());
    }

    private static void appendShort(StringBuilder out, NBTTagShort in) {
        out.append('s');
        out.append(in.func_150289_e());
    }

    private static void appendInt(StringBuilder out, NBTTagInt in) {
        out.append('i');
        out.append(in.func_150287_d());
    }

    private static void appendLong(StringBuilder out, NBTTagLong in) {
        out.append('l');
        out.append(in.func_150291_c());
    }

    private static void appendFloat(StringBuilder out, NBTTagFloat in) {
        out.append('f');
        out.append(in.func_150288_h());
    }

    private static void appendDouble(StringBuilder out, NBTTagDouble in) {
        out.append('d');
        out.append(in.func_150286_g());
    }

    private static void appendByteArray(StringBuilder out, NBTTagByteArray in) {
        out.append("b[");
        for(byte b : in.func_150292_c()) {
            out.append(b);
            out.append(',');
        }
        out.setCharAt(out.length()-1, ']');
    }

    private static void appendString(StringBuilder out, NBTTagString in) {
        out.append('"');
        out.append(in.func_150285_a_());
        out.append('"');
    }

    private static void appendList(StringBuilder out, NBTTagList in) {
        out.append('[');
        try {
            Field field = NBTTagList.class.getDeclaredField("tagList");
            field.setAccessible(true);
            for(NBTBase tag : (List<NBTBase>)field.get(in)) {
                appendTag(out, tag);
            }
        } catch(Exception e) {
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("Go complain to Vorquel. His NBT parser broke");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
        }
        //TODO finish this garbage thing
        out.setCharAt(out.length() - 1, ']');
    }

    private static void appendCompound(StringBuilder out, NBTTagCompound in) {
        out.append('{');
        for(String key : (Set<String>)in.func_150296_c()) {
            out.append('"');
            out.append(key);
            out.append('"');
            out.append(':');
            appendTag(out, in.getTag(key));
            out.append(',');
        }
        out.setCharAt(out.length()-1, '}');
    }

    private static void appendIntArray(StringBuilder out, NBTTagIntArray in) {
        out.append("i[");
        for(int i : in.func_150302_c()) {
            out.append(i);
            out.append(',');
        }
        out.setCharAt(out.length()-1, ']');
    }

    private class Token {
        public Type type;
        public Object value;
    }

    private enum Type {
        LIT_BYTE, LIT_SHORT, LIT_INT, LIT_LONG, LIT_FLOAT, LIT_DOUBLE, LIT_STRING, COLON, COMMA,
        LEFT_BRACE, RIGHT_BRACE, LEFT_SQUARE, RIGHT_SQUARE, OPEN_BYTE_ARRAY, OPEN_INT_ARRAY
    }
}
