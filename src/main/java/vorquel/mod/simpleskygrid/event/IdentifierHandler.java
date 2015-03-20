package vorquel.mod.simpleskygrid.event;

import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.commons.io.output.StringBuilderWriter;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.network.MessageClipboard;

import java.io.BufferedWriter;
import java.io.IOException;

public class IdentifierHandler {

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRightClickEntity(EntityInteractEvent event) {
        if(shouldLeave(event))
            return;
        event.setCanceled(true);
        String info = "Problem retrieving block data";
        try {
            StringBuilderWriter sbw = new StringBuilderWriter();
            JsonWriter jw = new JsonWriter(new BufferedWriter(sbw));
            jw.beginObject();
            jw.name("type");
            jw.value("entity");
            jw.name("name");
            jw.value(EntityList.getEntityString(event.target));
            NBTTagCompound nbt = new NBTTagCompound();
            event.target.writeToNBT(nbt);
            NBT2JSON.sanitizeEntity(nbt);
            jw.name("nbt");
            NBT2JSON.writeCompound(jw, nbt);
            jw.endObject();
            jw.flush();
            info = sbw.toString();
        } catch(IOException ignored) {}
        event.entityPlayer.addChatComponentMessage(new ChatComponentText(info));
        if(event.entityPlayer.isSneaking())
            SimpleSkyGrid.network.sendTo(new MessageClipboard(info), (EntityPlayerMP) event.entityPlayer);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRightClickBlock(PlayerInteractEvent event) {
        if(shouldLeave(event) || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            return;
        event.setCanceled(true);
        String info = "Problem retrieving block data";
        try {
            StringBuilderWriter sbw = new StringBuilderWriter();
            JsonWriter jw = new JsonWriter(new BufferedWriter(sbw));
            jw.beginObject();
            jw.name("type");
            jw.value("block");
            jw.name("name");
            jw.value(GameData.getBlockRegistry().getNameForObject(event.world.getBlock(event.x, event.y, event.z)));
            int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
            if(meta != 0) {
                jw.name("meta");
                jw.value(meta);
            }
            TileEntity tileEntity = event.world.getTileEntity(event.x, event.y, event.z);
            if(tileEntity != null) {
                jw.name("nbt");
                NBTTagCompound nbt = new NBTTagCompound();
                tileEntity.writeToNBT(nbt);
                NBT2JSON.sanitizeBlock(nbt);
                NBT2JSON.writeCompound(jw, nbt);
            }
            jw.endObject();
            jw.flush();
            info = sbw.toString();
        } catch(IOException ignored) {}
        event.entityPlayer.addChatComponentMessage(new ChatComponentText(info));
        if(event.entityPlayer.isSneaking())
            SimpleSkyGrid.network.sendTo(new MessageClipboard(info), (EntityPlayerMP) event.entityPlayer);
    }

    private boolean shouldLeave(PlayerEvent event) {
        return event.entityPlayer.getCurrentEquippedItem() == null || event.entityPlayer.getCurrentEquippedItem().getItem() != Ref.itemIdentifier || !(event.entityPlayer instanceof EntityPlayerMP);
    }
}