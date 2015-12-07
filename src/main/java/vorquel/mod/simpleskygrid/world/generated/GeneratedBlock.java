package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import vorquel.mod.simpleskygrid.helper.JSON2NBT;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;

import java.util.Random;

public class GeneratedBlock implements IGeneratedObject {

    private Block block;
    private int meta;
    private NBTTagCompound nbt;
    private ILootSource lootSource;

    public GeneratedBlock(Block block, int meta, NBTTagCompound nbt, ILootSource lootSource) {
        this.block = block;
        this.meta = meta;
        this.nbt = nbt;
        this.lootSource = lootSource;
    }

    @Override
    public void provideObject(Random random, World world, int x, int y, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[y >> 4];
        ebs.setExtBlockID(x & 15, y & 15, z & 15, block);
        ebs.setExtBlockMetadata(x & 15, y & 15, z & 15, meta);
        if(block.hasTileEntity(meta)) {
            TileEntity te = block.createTileEntity(world, meta);
            if(te != null) {
                if(nbt != null) {
                    te.readFromNBT(JSON2NBT.localizeBlock(nbt, world, x, y, z));
                } else {
                    te.xCoord = x;
                    te.yCoord = y;
                    te.zCoord = z;
                }
                chunk.addTileEntity(te);
                if(te instanceof IInventory && lootSource != null)
                    lootSource.provideLoot(random, (IInventory) world.getTileEntity(x, y, z));
            }
        }
    }
}
