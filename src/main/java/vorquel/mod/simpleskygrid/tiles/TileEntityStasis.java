package vorquel.mod.simpleskygrid.tiles;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityStasis extends TileEntity {
    
    public Block block;
    public int meta;
    public TileEntity tileEntity;
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        block = Block.getBlockFromName(tag.getString("block"));
        meta = tag.getByte("meta");
        NBTTagCompound nbt = tag.getCompoundTag("nbt");
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 2);
        if(block.hasTileEntity(meta)) {
            super.writeToNBT(nbt);
            tileEntity = block.createTileEntity(worldObj, meta);
            tileEntity.readFromNBT(nbt);
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("block", GameData.getBlockRegistry().getNameForObject(block));
        if(tileEntity != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            tileEntity.writeToNBT(nbt);
            tag.setTag("nbt", nbt);
        }
    }
}
