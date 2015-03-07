package vorquel.mod.simpleskygrid.world.igenerated;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import vorquel.mod.simpleskygrid.helper.NBTString;

import java.util.Random;

public class GeneratedBlock implements IGeneratedObject {

    private Block block;
    private int meta;
    private NBTTagCompound nbt;

    public GeneratedBlock(Block block, int meta, NBTTagCompound nbt) {
        this.block = block;
        this.meta = meta;
        this.nbt = nbt;
    }

    @Override
    public void provideObject(Random random, World world, int x, int y, int z) {
        world.setBlock(x, y, z, block, meta, 2);
        if(nbt != null) {
            TileEntity tileEntity = block.createTileEntity(world, meta);
            NBTString.localizeNBT(nbt, x, y, z);
            tileEntity.readFromNBT(nbt);
            world.addTileEntity(tileEntity);
        } else if(block == Blocks.chest) {
            TileEntityChest te = new TileEntityChest();
            te.xCoord = x;
            te.yCoord = y;
            te.zCoord = z;
            WeightedRandomChestContent.generateChestContents(random, ChestGenHooks.getItems(ChestGenHooks.DUNGEON_CHEST, random), te, ChestGenHooks.getCount(ChestGenHooks.DUNGEON_CHEST, random));
            world.addTileEntity(te);
        }
    }
}
