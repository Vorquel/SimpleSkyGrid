package vorquel.mod.simpleskygrid.world.generated.localizer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vorquel.mod.simpleskygrid.api.INBTLocalizer;

public class LocalizerThaumcraftTileNode implements INBTLocalizer {
    @Override
    public boolean isNeeded(NBTTagCompound tag) {
        return tag.getString("id").equals("TileNode");
    }

    @Override
    public void localize(NBTTagCompound tag, World world, int x, int y, int z) {

    }
}
