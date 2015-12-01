package vorquel.mod.simpleskygrid.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vorquel.mod.simpleskygrid.tiles.TileEntityStasis;

public class BlockStasis extends Block implements ITileEntityProvider{

    public BlockStasis() {
        super(Material.glass);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityStasis();
    }
}
