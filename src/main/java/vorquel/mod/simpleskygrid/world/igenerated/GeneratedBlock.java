package vorquel.mod.simpleskygrid.world.igenerated;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ChestGenHooks;
import vorquel.mod.simpleskygrid.helper.NBTString;

import java.util.Random;

public class GeneratedBlock implements IGeneratedObject {

    private Block block;
    private int metadata;
    private NBTTagCompound nbt;

    public GeneratedBlock(Block block, int metadata, NBTTagCompound nbt) {
        this.block = block;
        this.metadata = metadata;
        this.nbt = nbt;
    }

    @Override
    public void provideObject(Random random, World world, Chunk chunk, int x, int y, int z) {
        chunk.getBlockStorageArray()[y>>4].func_150818_a(x, y & 15, z, block);
        chunk.setBlockMetadata(x, y, z, metadata);
        if(nbt != null) {
            TileEntity tileEntity = block.createTileEntity(world, metadata);
            NBTString.localizeNBT(nbt, chunk.xPosition * 16 + x, y, chunk.zPosition * 16 + z);
            tileEntity.readFromNBT(nbt);
            chunk.addTileEntity(tileEntity);
        } else if(block == Blocks.chest) {
            TileEntityChest te = new TileEntityChest();
            te.xCoord = chunk.xPosition * 16 + x;
            te.yCoord = y;
            te.zCoord = chunk.zPosition * 16 + z;
            WeightedRandomChestContent.generateChestContents(random, ChestGenHooks.getItems(ChestGenHooks.DUNGEON_CHEST, random), te, ChestGenHooks.getCount(ChestGenHooks.DUNGEON_CHEST, random));
            chunk.addTileEntity(te);
        }
    }
}
