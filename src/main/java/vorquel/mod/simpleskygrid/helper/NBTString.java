package vorquel.mod.simpleskygrid.helper;

import net.minecraft.nbt.*;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String goodIn = in.replaceAll("\\s+", "");
        Pattern pattern = Pattern.compile("[:,\\{\\}\\]]|[bi]?\\[|[bsil]-?(0|[1-9]\\d*)|[fd]-?(0?|[1-9]\\d*)\\.\\d*|\".+?\"");
        Matcher matcher = pattern.matcher(goodIn);
        ArrayList<Token> out = new ArrayList<Token>();
        try {
            int end = 0;
            while(matcher.find()) {
                end = matcher.end();
                String temp = goodIn.substring(matcher.start(), end);
                if(temp.equals(":"))
                    out.add(new Token(Type.COLON));
                else if(temp.equals(","))
                    out.add(new Token(Type.COMMA));
                else if(temp.equals("{"))
                    out.add(new Token(Type.LEFT_BRACE));
                else if(temp.equals("}"))
                    out.add(new Token(Type.RIGHT_BRACE));
                else if(temp.equals("["))
                    out.add(new Token(Type.LEFT_SQUARE));
                else if(temp.equals("]"))
                    out.add(new Token(Type.RIGHT_SQUARE));
                else if(temp.equals("b["))
                    out.add(new Token(Type.OPEN_BYTE_ARRAY));
                else if(temp.equals("i["))
                    out.add(new Token(Type.OPEN_INT_ARRAY));
                else if(temp.startsWith("b"))
                    out.add(new Token(Type.LIT_BYTE, Byte.decode(temp.substring(1))));
                else if(temp.startsWith("s"))
                    out.add(new Token(Type.LIT_SHORT, Short.decode(temp.substring(1))));
                else if(temp.startsWith("i"))
                    out.add(new Token(Type.LIT_INT, Integer.decode(temp.substring(1))));
                else if(temp.startsWith("l"))
                    out.add(new Token(Type.LIT_LONG, Long.decode(temp.substring(1))));
                else if(temp.startsWith("f"))
                    out.add(new Token(Type.LIT_FLOAT, Float.valueOf(temp.substring(1))));
                else if(temp.startsWith("d"))
                    out.add(new Token(Type.LIT_DOUBLE, Double.valueOf(temp.substring(1))));
                else if(temp.startsWith("\"") && temp.endsWith("\""))
                    out.add(new Token(Type.LIT_STRING, temp.substring(1, temp.length() - 1)));
                else
                    throw new IllegalArgumentException("type1");
            }
            if(end != goodIn.length())
                throw new IllegalArgumentException("type2");
        } catch(NumberFormatException e) {
            SimpleSkyGrid.logger.error("Syntax error in NBT data: malformed numeric data.");
            out.clear();
            out.add(new Token(Type.LEFT_BRACE));
            out.add(new Token(Type.RIGHT_BRACE));
        } catch(IllegalArgumentException e) {
            if(e.getMessage().equals("type1")) {
                SimpleSkyGrid.logger.fatal("********************************************");
                SimpleSkyGrid.logger.fatal("********************************************");
                SimpleSkyGrid.logger.fatal("********************************************");
                SimpleSkyGrid.logger.fatal("Go complain to Vorquel. His NBT parser broke");
                SimpleSkyGrid.logger.fatal("Input string could not be properly tokenized");
                SimpleSkyGrid.logger.fatal("********************************************");
                SimpleSkyGrid.logger.fatal("********************************************");
                SimpleSkyGrid.logger.fatal("********************************************");
            } else if(e.getMessage().equals("type2")) {
                SimpleSkyGrid.logger.error("Syntax error in NBT data: unrecognized tokens.");
            } else {
                SimpleSkyGrid.logger.fatal("Unrecognized error in NBT data.");
            }
            out.clear();
            out.add(new Token(Type.LEFT_BRACE));
            out.add(new Token(Type.RIGHT_BRACE));
        }
        return out;
    }

    private static NBTTagCompound readTokens(ArrayList<Token> tokens) {
        if(tokens.isEmpty())
            return null;
        Iterator<Token> iterator = tokens.iterator();
    }

    public static String getStringFromNBT(NBTTagCompound in) {
        StringBuilder out = new StringBuilder();
        appendCompound(out, in);
        return out.toString();
    }

    private static void appendTag(StringBuilder out, NBTBase in) {
        switch(in.getId()) {
            case 1:  appendByte(     out,      (NBTTagByte) in); break;
            case 2:  appendShort(    out,     (NBTTagShort) in); break;
            case 3:  appendInt(      out,       (NBTTagInt) in); break;
            case 4:  appendLong(     out,      (NBTTagLong) in); break;
            case 5:  appendFloat(    out,     (NBTTagFloat) in); break;
            case 6:  appendDouble(   out,    (NBTTagDouble) in); break;
            case 7:  appendByteArray(out, (NBTTagByteArray) in); break;
            case 8:  appendString(   out,    (NBTTagString) in); break;
            case 9:  appendList(     out,      (NBTTagList) in); break;
            case 10: appendCompound( out,  (NBTTagCompound) in); break;
            case 11: appendIntArray( out,  (NBTTagIntArray) in); break;
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
        out.setCharAt(out.length() - 1, ']');
    }

    private static void appendString(StringBuilder out, NBTTagString in) {
        out.append('"');
        out.append(in.func_150285_a_());
        out.append('"');
    }

    private static void appendList(StringBuilder out, NBTTagList in) {
        try {
            Field field = NBTTagList.class.getDeclaredField("tagList");
            field.setAccessible(true);
            List<NBTBase> tags = (List<NBTBase>)field.get(in);
            out.append('[');
            for(NBTBase tag : tags) {
                appendTag(out, tag);
                out.append(',');
            }
            out.setCharAt(out.length() - 1, ']');
        } catch(Exception e) {
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("Go complain to Vorquel. His NBT parser broke");
            SimpleSkyGrid.logger.fatal("His reflection didnt account for obfuscation");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
        }
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
        out.setCharAt(out.length() - 1, '}');
    }

    private static void appendIntArray(StringBuilder out, NBTTagIntArray in) {
        out.append("i[");
        for(int i : in.func_150302_c()) {
            out.append(i);
            out.append(',');
        }
        out.setCharAt(out.length()-1, ']');
    }

    private static class Token {
        public Type type;
        public Object value;
        public Token(Type type, Object value) {
            this.type = type;
            this.value = value;
        }
        public Token(Type type) {
            this(type, null);
        }
    }

    private enum Type {
        LIT_BYTE, LIT_SHORT, LIT_INT, LIT_LONG, LIT_FLOAT, LIT_DOUBLE, LIT_STRING, COLON, COMMA,
        LEFT_BRACE, RIGHT_BRACE, LEFT_SQUARE, RIGHT_SQUARE, OPEN_BYTE_ARRAY, OPEN_INT_ARRAY
    }
}
