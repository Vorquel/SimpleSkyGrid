package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ChestGenHooks;
import vorquel.mod.simpleskygrid.helper.NBT2JSON;
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
        chunk.getBlockStorageArray()[y >> 4].func_150818_a(x & 15, y & 15, z & 15, block);
        chunk.setBlockMetadata(x & 15, y, z & 15, meta);
        if(block.hasTileEntity(meta)) {
            TileEntity te = null;
            if(nbt != null) {
                te = block.createTileEntity(world, meta);
                NBT2JSON.localizeBlock(nbt, x, y, z);
                te.readFromNBT(nbt);
                chunk.addTileEntity(te);
            } else {
                te = new TileEntityChest();
                te.xCoord = x;
                te.yCoord = y;
                te.zCoord = z;
                chunk.addTileEntity(te);
            }
            if(te instanceof IInventory && lootSource != null)
                lootSource.provideLoot(random, (IInventory) world.getTileEntity(x, y, z));
        }
    }
}
