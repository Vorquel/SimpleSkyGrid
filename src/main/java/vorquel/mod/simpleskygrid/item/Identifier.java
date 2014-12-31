package vorquel.mod.simpleskygrid.item;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

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
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xIn, float yIn, float zIn) {
        if(!world.isRemote)
            return true;
        String name = GameData.getBlockRegistry().getNameForObject(world.getBlock(x, y, z))
                + "::" + world.getBlockMetadata(x, y, z);
        player.addChatMessage(new ChatComponentText(name));
        if(player.isSneaking())
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(name), null);
        return true;
    }
}
