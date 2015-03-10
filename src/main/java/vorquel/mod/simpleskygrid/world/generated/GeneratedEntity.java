package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vorquel.mod.simpleskygrid.helper.NBTString;

import java.util.Random;

public class GeneratedEntity implements IGeneratedObject {
    private String name;
    private NBTTagCompound nbt;

    public GeneratedEntity(String name, NBTTagCompound nbt) {
        this.name = name;
        this.nbt = nbt;
    }

    @Override
    public void provideObject(Random random, World world, int x, int y, int z) {
        Entity entity = EntityList.createEntityByName(name, world);
        double realX = x + .5;
        double realZ = z + .5;
        if(nbt != null) {
            NBTString.localizeNBT(nbt, realX, y, realZ);
            entity.readFromNBT(nbt);
        }
        entity.setPosition(realX, y, realZ);
        world.spawnEntityInWorld(entity);
    }
}
