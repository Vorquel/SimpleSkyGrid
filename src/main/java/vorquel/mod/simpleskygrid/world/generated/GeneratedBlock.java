package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import vorquel.mod.simpleskygrid.entity.EntityStasis;
import vorquel.mod.simpleskygrid.helper.JSON2NBT;

import java.util.Random;

public class GeneratedBlock implements IGeneratedObject {

    private Block block;
    private int meta;
    private NBTTagCompound nbt;
    private boolean stasis;

    public GeneratedBlock(Block block, int meta, NBTTagCompound nbt, boolean stasis) {
        this.block = block;
        this.meta = meta;
        this.nbt = nbt;
        this.stasis = stasis;
    }

    @Override
    public void provideObject(Random random, World world, BlockPos pos) {
        IBlockState state = block.getStateFromMeta(meta);
        if(block.hasTileEntity(state)) {
            world.setBlockState(pos, state);
            TileEntity te = world.getTileEntity(pos);
            if(nbt != null)
                te.deserializeNBT(nbt);
        } else {
            Chunk chunk = world.getChunkFromBlockCoords(pos);
            ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[pos.getY() >> 4];
            ebs.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, state);
        }
        if(stasis) {
            EntityStasis stasis = new EntityStasis(world);
            stasis.setPosition(pos.getX() + .5, pos.getY() - .125, pos.getZ() + .5);
            world.spawnEntityInWorld(stasis);
        }
    }
}
