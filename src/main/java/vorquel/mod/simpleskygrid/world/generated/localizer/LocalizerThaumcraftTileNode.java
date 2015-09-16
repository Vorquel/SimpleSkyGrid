package vorquel.mod.simpleskygrid.world.generated.localizer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LocalizerThaumcraftTileNode implements INBTLocalizer {
    @Override
    public boolean isNeeded(NBTTagCompound tag) {
        return tag.getString("id").equals("TileNode");
    }

    @Override
    public void localize(NBTTagCompound tag, World world, int x, int y, int z) {
        String nodeId = String.format("%d:%d:%d:%d",world.provider.dimensionId,x,y,z);
        tag.setString("nodeId", nodeId);
    }
}
