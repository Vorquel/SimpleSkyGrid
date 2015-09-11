package vorquel.mod.simpleskygrid.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface INBTLocalizer {
    boolean isNeeded(NBTTagCompound tag);
    void localize(NBTTagCompound tag, World world, int x, int y, int z);
}
