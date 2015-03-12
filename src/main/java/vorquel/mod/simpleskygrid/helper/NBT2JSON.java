package vorquel.mod.simpleskygrid.helper;

import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.nbt.*;
import org.apache.commons.io.output.StringBuilderWriter;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBT2JSON {

    public static String toString(NBTTagCompound nbt, boolean pretty) throws IOException {
        StringBuilderWriter sbw = new StringBuilderWriter();
        JsonWriter jw = new JsonWriter(sbw);
        if(pretty)
            jw.setIndent("  ");
        writeCompound(jw, nbt);
        return sbw.toString();
    }

    public static NBTTagCompound toNBT(SimpleSkyGridConfigReader jr) {
        return readCompound(jr);
    }

    private static void writeCompound(JsonWriter jw, NBTTagCompound nbt) throws IOException {
        jw.beginObject();
        for(Object key : nbt.func_150296_c()) {
            String label = (String) key;
            NBTBase tag = nbt.getTag(label);
            String prefix = tagPrefix(tag);
            jw.name(prefix + label);
            switch(tag.getId()) {
                case 1:  writeByte(     jw,      (NBTTagByte) tag); break;
                case 2:  writeShort(    jw,     (NBTTagShort) tag); break;
                case 3:  writeInt(      jw,       (NBTTagInt) tag); break;
                case 4:  writeLong(     jw,      (NBTTagLong) tag); break;
                case 5:  writeFloat(    jw,     (NBTTagFloat) tag); break;
                case 6:  writeDouble(   jw,    (NBTTagDouble) tag); break;
                case 7:  writeByteArray(jw, (NBTTagByteArray) tag); break;
                case 8:  writeString(   jw,    (NBTTagString) tag); break;
                case 9:  writeList(     jw,      (NBTTagList) tag); break;
                case 10: writeCompound( jw,  (NBTTagCompound) tag); break;
                case 11: writeIntArray( jw,  (NBTTagIntArray) tag); break;
                default: SimpleSkyGrid.logger.warn("Unrecognised tag type in NBT data");
            }
        }
        jw.endObject();
    }

    private static void writeByte(JsonWriter jw, NBTTagByte tag) throws IOException {
        jw.value(tag.func_150290_f());
    }

    private static void writeShort(JsonWriter jw, NBTTagShort tag) throws IOException {
        jw.value(tag.func_150289_e());
    }

    private static void writeInt(JsonWriter jw, NBTTagInt tag) throws IOException {
        jw.value(tag.func_150287_d());
    }

    private static void writeLong(JsonWriter jw, NBTTagLong tag) throws IOException {
        jw.value(tag.func_150291_c());
    }

    private static void writeFloat(JsonWriter jw, NBTTagFloat tag) throws IOException {
        jw.value(tag.func_150288_h());
    }

    private static void writeDouble(JsonWriter jw, NBTTagDouble tag) throws IOException {
        jw.value(tag.func_150286_g());
    }

    private static void writeString(JsonWriter jw, NBTTagString tag) throws IOException {
        jw.value(tag.func_150285_a_());
    }

    private static void writeByteArray(JsonWriter jw, NBTTagByteArray tag) throws IOException {
        jw.beginArray();
        for(byte b : tag.func_150292_c())
            jw.value(b);
        jw.endArray();
    }

    private static void writeIntArray(JsonWriter jw, NBTTagIntArray tag) throws IOException {
        jw.beginArray();
        for(int i : tag.func_150302_c())
            jw.value(i);
        jw.endArray();
    }

    private static void writeList(JsonWriter jw, NBTTagList tag) throws IOException {
        jw.beginArray();
        for(NBTBase b : (List<NBTBase>) ReflectionHelper.getPrivateValue(NBTTagList.class, tag, "tagList", "field_74747_a"))
            writeTag(b);
        jw.endArray();
    }

    private static void writeTag(NBTBase tag) {

    }

    private static String tagPrefix(NBTBase tag) {
        switch(tag.getId()) {
            case 1:  return "b_";
            case 2:  return "s_";
            case 3:  return "i_";
            case 4:  return "l_";
            case 5:  return "f_";
            case 6:  return "d_";
            case 7:  return "B_";
            case 8:  return "S_";
            case 9:  return "L_" + listPrefix((NBTTagList) tag);
            case 10: return "C_";
            case 11: return "I_";
            default: return null;
        }
    }

    private static String listPrefix(NBTTagList tag) {
        if(tag.tagCount() == 0)
            return "e_";
        else
            return tagPrefix(((List<NBTBase>) ReflectionHelper.getPrivateValue(NBTTagList.class, tag, "tagList", "field_74747_a")).get(0));
    }

    private static NBTTagCompound readCompound(SimpleSkyGridConfigReader jr) {
        NBTTagCompound nbt = new NBTTagCompound();
        jr.beginObject();
        while(jr.hasNext()) {
            readTag(jr, nbt);
        }
        jr.endObject();
        return nbt;
    }

    private static void readTag(SimpleSkyGridConfigReader jr, NBTTagCompound nbt) {
        NBTBase tag;
        String label = jr.nextName();
        switch(label.substring(0, 2)) {
            case "b_": tag = readByte(jr);        break;
            case "s_": tag = readShort(jr);       break;
            case "i_": tag = readInt(jr);         break;
            case "l_": tag = readLong(jr);        break;
            case "f_": tag = readFloat(jr);       break;
            case "d_": tag = readDouble(jr);      break;
            case "S_": tag = readString(jr);      break;
            case "B_": tag = readByteArray(jr);   break;
            case "I_": tag = readIntArray(jr);    break;
            case "L_": tag = readList(jr, label); break;
            case "C_": tag = readCompound(jr);    break;
            default:
                SimpleSkyGrid.logger.fatal(String.format("Tag prefix expected in NBT data, found %s", label));
                throw new RuntimeException(String.format("Tag prefix expected in NBT data, found %s", label));
        }
        while(label.startsWith("L_")) {
            label = label.substring(2);
        }
        nbt.setTag(label.substring(2), tag);
    }

    private static NBTTagByte readByte(SimpleSkyGridConfigReader jr) {
        long expected = jr.nextLong();
        byte actual = (byte) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given byte value in NBT data does not fit in byte");
            throw new RuntimeException("Given byte value in NBT data does not fit in byte");
        }
        return new NBTTagByte(actual);
    }

    private static NBTTagShort readShort(SimpleSkyGridConfigReader jr) {
        long expected = jr.nextLong();
        short actual = (short) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given short value in NBT data does not fit in short");
            throw new RuntimeException("Given short value in NBT data does not fit in short");
        }
        return new NBTTagShort(actual);
    }

    private static NBTTagInt readInt(SimpleSkyGridConfigReader jr) {
        long expected = jr.nextLong();
        int actual = (int) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given int value in NBT data does not fit in int");
            throw new RuntimeException("Given int value in NBT data does not fit in int");
        }
        return new NBTTagInt(actual);
    }

    private static NBTTagLong readLong(SimpleSkyGridConfigReader jr) {
        return new NBTTagLong(jr.nextLong());
    }

    private static NBTTagFloat readFloat(SimpleSkyGridConfigReader jr) {
        double expected = jr.nextDouble();
        float actual = (float) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given float value in NBT data does not fit in float");
            throw new RuntimeException("Given float value in NBT data does not fit in float");
        }
        return new NBTTagFloat(actual);
    }

    private static NBTBase readDouble(SimpleSkyGridConfigReader jr) {
        return new NBTTagDouble(jr.nextDouble());
    }

    private static NBTTagByteArray readByteArray(SimpleSkyGridConfigReader jr) {
        ArrayList<Byte> byteList = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()) {
            long expected = jr.nextLong();
            byte actual = (byte) expected;
            if(expected != actual) {
                SimpleSkyGrid.logger.fatal("Given byte value in NBT data does not fit in byte");
                throw new RuntimeException("Given byte value in NBT data does not fit in byte");
            }
            byteList.add(actual);
        }
        jr.endArray();
        byte[] bytes = new byte[byteList.size()];
        for(int i=0; i<byteList.size(); ++i)
            bytes[i] = byteList.get(i);
        return new NBTTagByteArray(bytes);
    }

    private static NBTTagIntArray readIntArray(SimpleSkyGridConfigReader jr) {
        ArrayList<Integer> intList = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()) {
            long expected = jr.nextLong();
            int actual = (int) expected;
            if(expected != actual) {
                SimpleSkyGrid.logger.fatal("Given int value in NBT data does not fit in int");
                throw new RuntimeException("Given int value in NBT data does not fit in int");
            }
            intList.add(actual);
        }
        jr.endArray();
        int[] ints = new int[intList.size()];
        for(int i=0; i<intList.size(); ++i)
            ints[i] = intList.get(i);
        return new NBTTagIntArray(ints);
    }

    private static NBTTagString readString(SimpleSkyGridConfigReader jr) {
        return new NBTTagString(jr.nextString());
    }

    private static NBTTagList readList(SimpleSkyGridConfigReader jr, String label) {
        String prefix = label.substring(2, 4);
        NBTTagList list = new NBTTagList();
        jr.beginArray();
        while(jr.hasNext()) {
            switch(prefix) {
                case "b_": list.appendTag(readByte(jr));                     break;
                case "s_": list.appendTag(readShort(jr));                    break;
                case "i_": list.appendTag(readInt(jr));                      break;
                case "l_": list.appendTag(readLong(jr));                     break;
                case "f_": list.appendTag(readFloat(jr));                    break;
                case "d_": list.appendTag(readDouble(jr));                   break;
                case "S_": list.appendTag(readString(jr));                   break;
                case "B_": list.appendTag(readByteArray(jr));                break;
                case "I_": list.appendTag(readIntArray(jr));                 break;
                case "L_": list.appendTag(readList(jr, label.substring(2))); break; //Abandon all hope, ye who enter here
                case "C_": list.appendTag(readCompound(jr));                 break;
                default:
                    SimpleSkyGrid.logger.fatal(String.format("Tag prefix expected in NBT data, found %s", label));
                    throw new RuntimeException(String.format("Tag prefix expected in NBT data, found %s", label));
            }
        }
        jr.endArray();
        return list;
    }
}
