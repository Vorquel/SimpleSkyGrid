package vorquel.mod.simpleskygrid.helper;

import com.google.gson.stream.JsonReader;
import net.minecraft.nbt.*;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.io.IOException;
import java.util.ArrayList;

public class NBT2JSON {

    public static String toString(NBTBase tag) {
        return null; //todo
    }

    public static NBTTagCompound toNBT(JsonReader jsonReader) throws IOException {
        return readCompound(jsonReader);
    }

    private static NBTTagCompound readCompound(JsonReader jsonReader) throws IOException {
        NBTTagCompound nbt = new NBTTagCompound();
        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            readTag(jsonReader, nbt);
        }
        jsonReader.endObject();
        return nbt;
    }

    private static void readTag(JsonReader jsonReader, NBTTagCompound nbt) throws IOException {
        NBTBase tag;
        String label = jsonReader.nextName();
        switch(label.substring(0, 2)) {
            case "b_": tag = readByte(jsonReader);        break;
            case "s_": tag = readShort(jsonReader);       break;
            case "i_": tag = readInt(jsonReader);         break;
            case "l_": tag = readLong(jsonReader);        break;
            case "f_": tag = readFloat(jsonReader);       break;
            case "d_": tag = readDouble(jsonReader);      break;
            case "S_": tag = readString(jsonReader);      break;
            case "B_": tag = readByteArray(jsonReader);   break;
            case "I_": tag = readIntArray(jsonReader);    break;
            case "L_": tag = readList(jsonReader, label); break;
            case "C_": tag = readCompound(jsonReader);    break;
            default:
                SimpleSkyGrid.logger.fatal(String.format("Tag prefix expected in NBT data, found %s", label));
                throw new RuntimeException(String.format("Tag prefix expected in NBT data, found %s", label));
        }
        while(label.startsWith("L_")) {
            label = label.substring(2);
        }
        label = label.substring(2);
        nbt.setTag(label.substring(2), tag);
    }

    private static NBTTagByte readByte(JsonReader jsonReader) throws IOException {
        long expected = jsonReader.nextLong();
        byte actual = (byte) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given byte value in NBT data does not fit in byte");
            throw new RuntimeException("Given byte value in NBT data does not fit in byte");
        }
        return new NBTTagByte(actual);
    }

    private static NBTTagShort readShort(JsonReader jsonReader) throws IOException {
        long expected = jsonReader.nextLong();
        short actual = (short) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given short value in NBT data does not fit in short");
            throw new RuntimeException("Given short value in NBT data does not fit in short");
        }
        return new NBTTagShort(actual);
    }

    private static NBTTagInt readInt(JsonReader jsonReader) throws IOException {
        long expected = jsonReader.nextLong();
        int actual = (int) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given int value in NBT data does not fit in int");
            throw new RuntimeException("Given int value in NBT data does not fit in int");
        }
        return new NBTTagInt(actual);
    }

    private static NBTTagLong readLong(JsonReader jsonReader) throws IOException {
        return new NBTTagLong(jsonReader.nextLong());
    }

    private static NBTTagFloat readFloat(JsonReader jsonReader) throws IOException {
        double expected = jsonReader.nextDouble();
        float actual = (float) expected;
        if(expected != actual) {
            SimpleSkyGrid.logger.fatal("Given float value in NBT data does not fit in float");
            throw new RuntimeException("Given float value in NBT data does not fit in float");
        }
        return new NBTTagFloat(actual);
    }

    private static NBTBase readDouble(JsonReader jsonReader) throws IOException {
        return new NBTTagDouble(jsonReader.nextDouble());
    }

    private static NBTTagByteArray readByteArray(JsonReader jsonReader) throws IOException {
        ArrayList<Byte> byteList = new ArrayList<>();
        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            long expected = jsonReader.nextLong();
            byte actual = (byte) expected;
            if(expected != actual) {
                SimpleSkyGrid.logger.fatal("Given byte value in NBT data does not fit in byte");
                throw new RuntimeException("Given byte value in NBT data does not fit in byte");
            }
            byteList.add(actual);
        }
        jsonReader.endArray();
        byte[] bytes = new byte[byteList.size()];
        for(int i=0; i<byteList.size(); ++i)
            bytes[i] = byteList.get(i);
        return new NBTTagByteArray(bytes);
    }

    private static NBTTagIntArray readIntArray(JsonReader jsonReader) throws IOException {
        ArrayList<Integer> intList = new ArrayList<>();
        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            long expected = jsonReader.nextLong();
            int actual = (int) expected;
            if(expected != actual) {
                SimpleSkyGrid.logger.fatal("Given int value in NBT data does not fit in int");
                throw new RuntimeException("Given int value in NBT data does not fit in int");
            }
            intList.add(actual);
        }
        jsonReader.endArray();
        int[] ints = new int[intList.size()];
        for(int i=0; i<intList.size(); ++i)
            ints[i] = intList.get(i);
        return new NBTTagIntArray(ints);
    }

    private static NBTTagString readString(JsonReader jsonReader) throws IOException {
        return new NBTTagString(jsonReader.nextString());
    }

    private static NBTTagList readList(JsonReader jsonReader, String label) throws IOException {
        String prefix = label.substring(2, 4);
        NBTTagList list = new NBTTagList();
        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            switch(prefix) {
                case "b_": list.appendTag(readByte(jsonReader));                     break;
                case "s_": list.appendTag(readShort(jsonReader));                    break;
                case "i_": list.appendTag(readInt(jsonReader));                      break;
                case "l_": list.appendTag(readLong(jsonReader));                     break;
                case "f_": list.appendTag(readFloat(jsonReader));                    break;
                case "d_": list.appendTag(readDouble(jsonReader));                   break;
                case "S_": list.appendTag(readString(jsonReader));                   break;
                case "B_": list.appendTag(readByteArray(jsonReader));                break;
                case "I_": list.appendTag(readIntArray(jsonReader));                 break;
                case "L_": list.appendTag(readList(jsonReader, label.substring(2))); break; //Abandon all hope, ye who enter here
                case "C_": list.appendTag(readCompound(jsonReader));                 break;
                default:
                    SimpleSkyGrid.logger.fatal(String.format("Tag prefix expected in NBT data, found %s", label));
                    throw new RuntimeException(String.format("Tag prefix expected in NBT data, found %s", label));
            }
        }
        jsonReader.endArray();
        return list;
    }
}
