package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import vorquel.mod.simpleskygrid.entity.EntityStasis;
import vorquel.mod.simpleskygrid.helper.JSON2NBT;
import vorquel.mod.simpleskygrid.world.loot.ILootSource;

import java.util.Random;

public class GeneratedBlock implements IGeneratedObject {

    private Block block;
    private int meta;
    private NBTTagCompound nbt;
    private ILootSource lootSource;
    private boolean stasis;

    public GeneratedBlock(Block block, int meta, NBTTagCompound nbt, ILootSource lootSource, boolean stasis) {
        this.block = block;
        this.meta = meta;
        this.nbt = nbt;
        this.lootSource = lootSource;
        this.stasis = stasis;
    }

    @Override
    public void provideObject(Random random, World world, int x, int y, int z) {
    	BlockPos here = new BlockPos(x, y, z);
        Chunk chunk = world.getChunkFromBlockCoords(here);
        ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[y >> 4];
        IBlockState state = block.getStateFromMeta(meta);
        ebs.set(x & 15, y & 15, z & 15, block.getStateFromMeta(meta));
        if(block.hasTileEntity()) {
            TileEntity te = block.createTileEntity(world, state);
            if(te != null) {
                if(nbt != null) {
                    te.readFromNBT(JSON2NBT.localizeBlock(nbt, world, x, y, z));
                } else {
                    te.setPos(here);
                }
                chunk.addTileEntity(te);
                if(te instanceof IInventory && lootSource != null)
                    lootSource.provideLoot(random, (IInventory) world.getTileEntity(here));
            }
        }
        if(stasis) {
            EntityStasis stasis = new EntityStasis(world);
            stasis.setPosition(x + .5, y - .125, z + .5);
            world.spawnEntityInWorld(stasis);
        }
    }
}
