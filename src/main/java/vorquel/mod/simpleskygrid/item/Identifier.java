package vorquel.mod.simpleskygrid.item;

import com.google.common.base.Strings;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.apache.commons.io.output.StringBuilderWriter;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
import vorquel.mod.simpleskygrid.network.MessageClipboard;
import vorquel.mod.simpleskygrid.network.MessageIdentify;

import java.io.BufferedWriter;
import java.io.IOException;

public class Identifier extends Item {

    public Identifier() {
        setUnlocalizedName("identifier");
        setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon("SimpleSkyGrid:identifier");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(player.isSneaking())
            switch(side) {
                case 0: --y; break;
                case 1: ++y; break;
                case 2: --z; break;
                case 3: ++z; break;
                case 4: --x; break;
                case 5: ++x; break;
                default:
                    player.addChatComponentMessage(new ChatComponentText("Unknown side shift clicked on."));
                    Log.warn("Unexpected side of block found on shift right clicking block with Identifier");
                    break;
            }
        SimpleSkyGrid.network.sendToServer(new MessageIdentify(x, y, z, stack.getItemDamage()));
        return true;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity) {
        if(entity.worldObj.isRemote)
            return true;
        String info = "Problem retrieving entity data";
        try {
            switch(itemStack.getItemDamage()) {
                case 0:   info = writeEntityJson(entity, 2); break;
                case 1:   info = writeEntityJson(entity, 0); break;
                default:  info = EntityList.getEntityString(entity);
            }
        } catch(IOException ignored) {}
        progressDamage(itemStack);
        progressDamage(itemStack);
        sendMultiLineChat(player, info);
        SimpleSkyGrid.network.sendTo(new MessageClipboard(info), (EntityPlayerMP) player);
        return super.itemInteractionForEntity(itemStack, player, entity);
    }

    private String writeEntityJson(Entity entity, int indent) throws IOException {
        StringBuilderWriter sbw = new StringBuilderWriter();
        JsonWriter jw = new JsonWriter(new BufferedWriter(sbw));
        jw.setIndent(Strings.repeat(" ", indent));
        jw.beginObject();
        jw.name("type");
        jw.value("entity");
        jw.name("name");
        jw.value(EntityList.getEntityString(entity));
        NBTTagCompound nbt = new NBTTagCompound();
        entity.writeToNBT(nbt);
        jw.name("nbt");
        NBT2JSON.writeCompound(jw, NBT2JSON.sanitizeEntity(nbt));
        jw.endObject();
        jw.flush();
        return sbw.toString();
    }

    public static void sendMultiLineChat(EntityPlayer player, String message) {
        int length = message.length();
        for(int start = 0, next; start <= length; start = next + 1) {
            next = message.indexOf('\n', start);
            next = next == -1 ? length : next;
            player.addChatComponentMessage(new ChatComponentText(message.substring(start, next)));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(world.isRemote)
            return itemStack;
        progressDamage(itemStack);
        return itemStack;
    }

    public static void progressDamage(ItemStack itemStack) {
        itemStack.setItemDamage(itemStack.getItemDamage() + 1);
        if(itemStack.getItemDamage() > 2)
            itemStack.setItemDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        switch(itemStack.getItemDamage()) {
            case 0: return  "item.identifier.jsonPretty";
            case 1: return  "item.identifier.jsonCompact";
            default: return "item.identifier.oreDictionary";
        }
    }
}
