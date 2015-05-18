package vorquel.mod.simpleskygrid.network;

import com.google.common.base.Strings;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.io.output.StringBuilderWriter;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
import vorquel.mod.simpleskygrid.item.Identifier;

import java.io.BufferedWriter;
import java.io.IOException;

public class MessageHandlerIdentify implements IMessageHandler<MessageIdentify, IMessage> {

    @Override
    public IMessage onMessage(MessageIdentify message, MessageContext ctx) {
        String info = "Problem retrieving block data";
        try {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            switch(message.damage) {
                case 0:  info = writeBlockJson(world,  message.x,  message.y,  message.z, 2); break;
                case 1:  info = writeBlockJson(world,  message.x,  message.y,  message.z, 0); break;
                default: info = writeOreNames(world,  message.x,  message.y,  message.z);
            }
        } catch(IOException ignored) {}
        Identifier.sendMultiLineChat(ctx.getServerHandler().playerEntity, info);
        return new MessageClipboard(info);
    }

    private String writeBlockJson(World world, int x, int y, int z, int indent) throws IOException {
        StringBuilderWriter sbw = new StringBuilderWriter();
        JsonWriter jw = new JsonWriter(new BufferedWriter(sbw));
        jw.setIndent(Strings.repeat(" ", indent));
        jw.beginObject();
        jw.name("type");
        jw.value("block");
        jw.name("name");
        jw.value(GameData.getBlockRegistry().getNameForObject(world.getBlock(x, y, z)));
        int meta = world.getBlockMetadata(x, y, z);
        if(meta != 0) {
            jw.name("meta");
            jw.value(meta);
        }
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity != null) {
            jw.name("nbt");
            NBTTagCompound nbt = new NBTTagCompound();
            tileEntity.writeToNBT(nbt);
            NBT2JSON.writeCompound(jw, NBT2JSON.sanitizeBlock(nbt));
        }
        jw.endObject();
        jw.flush();
        return sbw.toString();
    }

    private String writeOreNames(World world, int x, int y, int z) {
        StringBuilder sb = new StringBuilder();
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        ItemStack itemStack = new ItemStack(block, 1, meta);
        int[] ids = OreDictionary.getOreIDs(itemStack);
        for(int id : ids) {
            String oreName = OreDictionary.getOreName(id);
            sb.append(oreName);
            sb.append(" ");
        }
        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);
        else
            sb.append("<No Ore Dictionary Names Exist>");
        return sb.toString();
    }
}
