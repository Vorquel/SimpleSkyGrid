package vorquel.mod.simpleskygrid.helper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.item.Item;
import net.minecraft.nbt.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBT2JSON {

    public static void sanitizeBlock(NBTTagCompound tag) {
        tag.removeTag("x");
        tag.removeTag("y");
        tag.removeTag("z");
        sanitizeItems(tag);
    }

    public static void localizeBlock(NBTTagCompound tag, int x, int y, int z) {
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
        localizeItems(tag);
    }

    public static void sanitizeEntity(NBTTagCompound tag) {
        tag.removeTag("Pos");
        sanitizeItems(tag);
    }

    public static void localizeEntity(NBTTagCompound tag, double x, double y, double z) {
        NBTTagList list = new NBTTagList();
        list.appendTag(new NBTTagDouble(x));
        list.appendTag(new NBTTagDouble(y));
        list.appendTag(new NBTTagDouble(z));
        tag.setTag("Pos", list);
        localizeItems(tag);
    }

    public static void sanitizeItems(NBTTagCompound nbt) {
        for(Object key : nbt.func_150296_c()) {
            String label = (String) key;
            NBTBase tag = nbt.getTag(label);
            switch(tag.getId()) {
                case 9: if(isInventoryLabel(label)) sanitizeInventory((NBTTagList) tag); break;
                case 10: sanitizeItems((NBTTagCompound) tag);
            }
        }
    }

    private static void sanitizeInventory(NBTTagList list) {
        if(list.getCompoundTagAt(0).hasKey("id"))
            for(int i=0; i<list.tagCount(); ++i) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                int id = compound.getShort("id");
                Item item = GameData.getItemRegistry().getObjectById(id);
                String name = GameData.getItemRegistry().getNameForObject(item);
                compound.setString("id", name);
            }
    }

    public static void localizeItems(NBTTagCompound nbt) {
        for(Object key : nbt.func_150296_c()) {
            String label = (String) key;
            NBTBase tag = nbt.getTag(label);
            switch(tag.getId()) {
                case 9: if(isInventoryLabel(label)) localizeInventory((NBTTagList) tag); break;
                case 10: localizeItems((NBTTagCompound) tag);
            }
        }
    }

    private static void localizeInventory(NBTTagList list) {
        if(list.getCompoundTagAt(0).hasKey("id"))
            for(int i=0; i<list.tagCount(); ++i) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                String name = compound.getString("id");
                int id = GameData.getItemRegistry().getId(name);
                compound.setShort("id", (short) id);
            }
    }

    private static boolean isInventoryLabel(String label) {
        switch(label) { //this structure is for ease of adding new indicative labels
            case "Items": return true;
            default: return false;
        }
    }

    public static void writeCompound(JsonWriter jw, NBTTagCompound nbt) throws IOException {
        jw.beginObject();
        for(Object key : nbt.func_150296_c()) {
            String label = (String) key;
            NBTBase tag = nbt.getTag(label);
            String prefix = tagPrefix(tag);
            jw.name(prefix + label);
            writeTag(jw, tag);
        }
        jw.endObject();
    }

    public static void writeByte(JsonWriter jw, NBTTagByte tag) throws IOException {
        jw.value(tag.func_150290_f());
    }

    public static void writeShort(JsonWriter jw, NBTTagShort tag) throws IOException {
        jw.value(tag.func_150289_e());
    }

    public static void writeInt(JsonWriter jw, NBTTagInt tag) throws IOException {
        jw.value(tag.func_150287_d());
    }

    public static void writeLong(JsonWriter jw, NBTTagLong tag) throws IOException {
        jw.value(tag.func_150291_c());
    }

    public static void writeFloat(JsonWriter jw, NBTTagFloat tag) throws IOException {
        jw.value(tag.func_150288_h());
    }

    public static void writeDouble(JsonWriter jw, NBTTagDouble tag) throws IOException {
        jw.value(tag.func_150286_g());
    }

    public static void writeString(JsonWriter jw, NBTTagString tag) throws IOException {
        jw.value(tag.func_150285_a_());
    }

    public static void writeByteArray(JsonWriter jw, NBTTagByteArray tag) throws IOException {
        jw.beginArray();
        for(byte b : tag.func_150292_c())
            jw.value(b);
        jw.endArray();
    }

    public static void writeIntArray(JsonWriter jw, NBTTagIntArray tag) throws IOException {
        jw.beginArray();
        for(int i : tag.func_150302_c())
            jw.value(i);
        jw.endArray();
    }

    public static void writeList(JsonWriter jw, NBTTagList tag) throws IOException {
        jw.beginArray();
        //noinspection unchecked
        for(NBTBase b : (List<NBTBase>) ReflectionHelper.getPrivateValue(NBTTagList.class, tag, "tagList", "field_74747_a"))
            writeTag(jw, b);
        jw.endArray();
    }

    public static void writeTag(JsonWriter jw, NBTBase tag) throws IOException {
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
            default: Log.warn("Unrecognised tag type in NBT data");
        }
    }

    public static String tagPrefix(NBTBase tag) {
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

    public static String listPrefix(NBTTagList tag) {
        if(tag.tagCount() == 0)
            return "e_";
        else
            //noinspection unchecked
            return tagPrefix(((List<NBTBase>) ReflectionHelper.getPrivateValue(NBTTagList.class, tag, "tagList", "field_74747_a")).get(0));
    }

    public static NBTTagCompound readCompound(JsonReader jr) throws IOException {
        NBTTagCompound nbt = new NBTTagCompound();
        jr.beginObject();
        while(jr.hasNext()) {
            readTag(jr, nbt);
        }
        jr.endObject();
        return nbt;
    }

    public static void readTag(JsonReader jr, NBTTagCompound nbt) throws IOException {
        NBTBase tag = null;
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
                Log.kill("Tag prefix expected in NBT data, found %s", label);
        }
        while(label.startsWith("L_")) {
            label = label.substring(2);
        }
        nbt.setTag(label.substring(2), tag);
    }

    public static NBTTagByte readByte(JsonReader jr) throws IOException {
        long expected = jr.nextLong();
        byte actual = (byte) expected;
        if(expected != actual) {
            Log.kill("Given byte value in NBT data does not fit in byte");
        }
        return new NBTTagByte(actual);
    }

    public static NBTTagShort readShort(JsonReader jr) throws IOException {
        long expected = jr.nextLong();
        short actual = (short) expected;
        if(expected != actual) {
            Log.kill("Given short value in NBT data does not fit in short");
        }
        return new NBTTagShort(actual);
    }

    public static NBTTagInt readInt(JsonReader jr) throws IOException {
        long expected = jr.nextLong();
        int actual = (int) expected;
        if(expected != actual) {
            Log.kill("Given int value in NBT data does not fit in int");
        }
        return new NBTTagInt(actual);
    }

    public static NBTTagLong readLong(JsonReader jr) throws IOException {
        return new NBTTagLong(jr.nextLong());
    }

    public static NBTTagFloat readFloat(JsonReader jr) throws IOException {
        double expected = jr.nextDouble();
        float actual = (float) expected;
        if(expected != actual) {
            Log.kill("Given float value in NBT data does not fit in float");
        }
        return new NBTTagFloat(actual);
    }

    public static NBTBase readDouble(JsonReader jr) throws IOException {
        return new NBTTagDouble(jr.nextDouble());
    }

    public static NBTTagByteArray readByteArray(JsonReader jr) throws IOException {
        ArrayList<Byte> byteList = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()) {
            long expected = jr.nextLong();
            byte actual = (byte) expected;
            if(expected != actual) {
                Log.kill("Given byte value in NBT data does not fit in byte");
            }
            byteList.add(actual);
        }
        jr.endArray();
        byte[] bytes = new byte[byteList.size()];
        for(int i=0; i<byteList.size(); ++i)
            bytes[i] = byteList.get(i);
        return new NBTTagByteArray(bytes);
    }

    public static NBTTagIntArray readIntArray(JsonReader jr) throws IOException {
        ArrayList<Integer> intList = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()) {
            long expected = jr.nextLong();
            int actual = (int) expected;
            if(expected != actual) {
                Log.kill("Given int value in NBT data does not fit in int");
            }
            intList.add(actual);
        }
        jr.endArray();
        int[] ints = new int[intList.size()];
        for(int i=0; i<intList.size(); ++i)
            ints[i] = intList.get(i);
        return new NBTTagIntArray(ints);
    }

    public static NBTTagString readString(JsonReader jr) throws IOException {
        return new NBTTagString(jr.nextString());
    }

    public static NBTTagList readList(JsonReader jr, String label) throws IOException {
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
                    Log.kill("Tag prefix expected in NBT data, found %s", label);
            }
        }
        jr.endArray();
        return list;
    }
}
