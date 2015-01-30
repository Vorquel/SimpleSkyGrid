package vorquel.mod.simpleskygrid.world;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import vorquel.mod.simpleskygrid.helper.NBTString;

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
    public void provideObject(World world, Chunk chunk, int x, int y, int z) {
        chunk.getBlockStorageArray()[y>>4].func_150818_a(x, y & 15, z, block);
        chunk.setBlockMetadata(x, y, z, metadata);
        if(nbt != null) {
            TileEntity tileEntity = block.createTileEntity(world, metadata);
            NBTString.localizeNBT(nbt, chunk.xPosition * 16 + x, y, chunk.zPosition * 16 + z);
            tileEntity.readFromNBT(nbt);
            chunk.addTileEntity(tileEntity);
        }
    }
}
