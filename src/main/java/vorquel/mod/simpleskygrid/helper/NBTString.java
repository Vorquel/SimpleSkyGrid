package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.nbt.*;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.util.*;
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
                System.out.print(temp + " ");
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
        try {
            readToken(iterator, Type.LEFT_BRACE);
            return readCompound(iterator);
        } catch(IllegalStateException e) {
            SimpleSkyGrid.logger.error("Syntax error in NBT data: unexpected tokens.");
            return null;
        } catch(NoSuchElementException e) {
            SimpleSkyGrid.logger.error("Syntax error in NBT data: unexpected end of token stream.");
            return null;
        }
    }

    private static Object readToken(Iterator<Token> iterator, Type type) {
        Token token = iterator.next();
        if(token.type != type)
            throw new IllegalStateException();
        return token.value;
    }

    private static NBTTagCompound readCompound(Iterator<Token> iterator) {
        NBTTagCompound tag = new NBTTagCompound();
        Token token = iterator.next();
        if(token.type == Type.RIGHT_BRACE)
            return tag;
        else if(token.type != Type.LIT_STRING)
            throw new IllegalStateException();
        readComplex(iterator, (String) token.value, tag);
        while(true) {
            token = iterator.next();
            if(token.type == Type.RIGHT_BRACE)
                break;
            else if(token.type != Type.COMMA)
                throw new IllegalStateException();
            readComplex(iterator, (String) readToken(iterator, Type.LIT_STRING), tag);
        }
        return tag;
    }

    private static void readComplex(Iterator<Token> iterator, String key, NBTTagCompound tag) {
        readToken(iterator, Type.COLON);
        Token token = iterator.next();
        switch(token.type) {
            case LIT_BYTE:        tag.setByte(     key,      (Byte) token.value); break;
            case LIT_SHORT:       tag.setShort(    key,     (Short) token.value); break;
            case LIT_INT:         tag.setInteger(  key,   (Integer) token.value); break;
            case LIT_LONG:        tag.setLong(     key,      (Long) token.value); break;
            case LIT_FLOAT:       tag.setFloat(    key,     (Float) token.value); break;
            case LIT_DOUBLE:      tag.setDouble(   key,    (Double) token.value); break;
            case LIT_STRING:      tag.setString(   key,    (String) token.value); break;
            case OPEN_BYTE_ARRAY: tag.setByteArray(key, readByteArray(iterator)); break;
            case OPEN_INT_ARRAY:  tag.setIntArray( key, readIntArray( iterator)); break;
            case LEFT_SQUARE:     tag.setTag(      key, readList(     iterator)); break;
            case LEFT_BRACE:      tag.setTag(      key, readCompound( iterator)); break;
            default: throw new IllegalStateException();
        }
    }

    private static byte[] readByteArray(Iterator<Token> iterator) {
        Token token = iterator.next();
        if(token.type == Type.RIGHT_SQUARE)
            return new byte[0];
        else if(token.type != Type.LIT_BYTE)
            throw new IllegalStateException();
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        bytes.add((Byte) token.value);
        while(true) {
            token = iterator.next();
            if(token.type == Type.RIGHT_SQUARE)
                break;
            else if(token.type != Type.COMMA)
                throw new IllegalStateException();
            bytes.add((Byte) readToken(iterator, Type.LIT_BYTE));
        }
        byte[] out = new byte[bytes.size()];
        for(int i=0; i<out.length; ++i)
            out[i] = bytes.get(i);
        return out;
    }

    private static int[] readIntArray(Iterator<Token> iterator) {
        Token token = iterator.next();
        if(token.type == Type.RIGHT_SQUARE)
            return new int[0];
        else if(token.type != Type.LIT_INT)
            throw new IllegalStateException();
        ArrayList<Integer> ints = new ArrayList<Integer>();
        ints.add((Integer) token.value);
        while(true) {
            token = iterator.next();
            if(token.type == Type.RIGHT_SQUARE)
                break;
            else if(token.type != Type.COMMA)
                throw new IllegalStateException();
            ints.add((Integer) readToken(iterator, Type.LIT_INT));
        }
        int[] out = new int[ints.size()];
        for(int i=0; i<out.length; ++i)
            out[i] = ints.get(i);
        return out;
    }

    private static NBTTagList readList(Iterator<Token> iterator) {
        NBTTagList tag = new NBTTagList();
        Token token = iterator.next();
        if(token.type == Type.RIGHT_BRACE)
            return tag;
        readElement(iterator, token, tag);
        while(true) {
            token = iterator.next();
            if(token.type == Type.RIGHT_BRACE)
                break;
            else if(token.type != Type.COMMA)
                throw new IllegalStateException();
            readToken(iterator, Type.COLON);
            readElement(iterator, iterator.next(), tag);
        }
        return tag;
    }

    private static void readElement(Iterator<Token> iterator, Token token, NBTTagList tag) {
        switch(token.type) {
            case LIT_BYTE:        tag.appendTag(new NBTTagByte(          (Byte) token.value)); break;
            case LIT_SHORT:       tag.appendTag(new NBTTagShort(        (Short) token.value)); break;
            case LIT_INT:         tag.appendTag(new NBTTagInt(        (Integer) token.value)); break;
            case LIT_LONG:        tag.appendTag(new NBTTagLong(          (Long) token.value)); break;
            case LIT_FLOAT:       tag.appendTag(new NBTTagFloat(        (Float) token.value)); break;
            case LIT_DOUBLE:      tag.appendTag(new NBTTagDouble(      (Double) token.value)); break;
            case LIT_STRING:      tag.appendTag(new NBTTagString(      (String) token.value)); break;
            case OPEN_BYTE_ARRAY: tag.appendTag(new NBTTagByteArray(readByteArray(iterator))); break;
            case OPEN_INT_ARRAY:  tag.appendTag(new NBTTagIntArray( readIntArray( iterator))); break;
            case LEFT_SQUARE:     tag.appendTag(                    readList(     iterator));  break;
            case LEFT_BRACE:      tag.appendTag(                    readCompound( iterator));  break;
            default: throw new IllegalStateException();
        }
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
        if(out.charAt(out.length()-1) == ',')
            out.setCharAt(out.length() - 1, ']');
        else
            out.append(']');
    }

    private static void appendString(StringBuilder out, NBTTagString in) {
        out.append('"');
        out.append(in.func_150285_a_());
        out.append('"');
    }

    private static void appendList(StringBuilder out, NBTTagList in) {
        try {
            List<NBTBase> tags = ReflectionHelper.getPrivateValue(NBTTagList.class, in, "b", "field_74747_a", "tagList");
            out.append('[');
            for(NBTBase tag : tags) {
                appendTag(out, tag);
                out.append(',');
            }
            if(out.charAt(out.length()-1) == ',')
                out.setCharAt(out.length() - 1, ']');
            else
                out.append(']');
        } catch(Exception e) {
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("********************************************");
            SimpleSkyGrid.logger.fatal("Go complain to Vorquel. His NBT parser broke");
            SimpleSkyGrid.logger.fatal("His reflection didn't do obfuscation correct");
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
        if(out.charAt(out.length()-1) == ',')
            out.setCharAt(out.length() - 1, '}');
        else
            out.append('}');
    }

    private static void appendIntArray(StringBuilder out, NBTTagIntArray in) {
        out.append("i[");
        for(int i : in.func_150302_c()) {
            out.append(i);
            out.append(',');
        }
        if(out.charAt(out.length()-1) == ',')
            out.setCharAt(out.length() - 1, ']');
        else
            out.append(']');
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
