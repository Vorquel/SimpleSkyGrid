package vorquel.mod.simpleskygrid.world.generated;

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
    private int meta;
    private NBTTagCompound nbt;

    public GeneratedBlock(Block block, int meta, NBTTagCompound nbt) {
        this.block = block;
        this.meta = meta;
        this.nbt = nbt;
    }

    @Override
    public void provideObject(Random random, World world, int x, int y, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        chunk.getBlockStorageArray()[y >> 4].func_150818_a(x & 15, y & 15, z & 15, block);
        chunk.setBlockMetadata(x & 15, y, z & 15, meta);
        if(block.hasTileEntity(meta)) {
            if(nbt != null) {
                TileEntity tileEntity = block.createTileEntity(world, meta);
                NBTString.localizeNBT(nbt, x, y, z);
                tileEntity.readFromNBT(nbt);
                chunk.addTileEntity(tileEntity);
            } else if(block == Blocks.chest) {
                TileEntityChest te = new TileEntityChest();
                te.xCoord = x;
                te.yCoord = y;
                te.zCoord = z;
                WeightedRandomChestContent.generateChestContents(random, ChestGenHooks.getItems(ChestGenHooks.DUNGEON_CHEST, random), te, ChestGenHooks.getCount(ChestGenHooks.DUNGEON_CHEST, random));
                chunk.addTileEntity(te);
            }
        }
    }
}
