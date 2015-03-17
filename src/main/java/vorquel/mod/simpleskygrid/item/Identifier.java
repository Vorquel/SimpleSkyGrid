package vorquel.mod.simpleskygrid.item;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
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
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xIn, float yIn, float zIn) {
        if(world.isRemote)
            return true;
        String name = GameData.getBlockRegistry().getNameForObject(world.getBlock(x, y, z));
        String meta = String.valueOf(world.getBlockMetadata(x, y, z));
        String nbt = "";
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tileEntity.writeToNBT(tag);
            NBT2JSON.sanitizeBlock(tag);
            try {
                nbt = NBT2JSON.toString(tag, true);
            } catch (IOException e) {
                nbt = "Problem decoding NBT data";
            }
        }
        String id = name+"::"+meta+nbt;
        player.addChatMessage(new ChatComponentText(id));
        if(player.isSneaking())
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(id), null);
        return true;
    }
}
