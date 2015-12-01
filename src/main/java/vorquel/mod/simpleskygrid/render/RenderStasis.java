package vorquel.mod.simpleskygrid.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import vorquel.mod.simpleskygrid.tiles.TileEntityStasis;

public class RenderStasis extends TileEntitySpecialRenderer {
    
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partial) {
        TileEntityStasis stasis = (TileEntityStasis) tileEntity;
        if(stasis.tileEntity == null) {
            RenderBlocks.getInstance().renderBlockAllFaces(stasis.block, stasis.xCoord, stasis.yCoord, stasis.zCoord);
        } else {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(stasis.tileEntity, x, y, z, partial);
        }
    }
}
