package vorquel.mod.simpleskygrid.world.generated;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vorquel.mod.simpleskygrid.helper.JSON2NBT;

import java.util.Random;

public class GeneratedEntity implements IGeneratedObject {
    private String name;
    private NBTTagCompound nbt;

    public GeneratedEntity(String name, NBTTagCompound nbt) {
        this.name = name;
        this.nbt = nbt;
    }

    @Override
    public void provideObject(Random random, World world, BlockPos pos) {
        Entity entity = EntityList.createEntityByName(name, world);
        double realX = pos.getX() + .5;
        double realZ = pos.getZ() + .5;
        if(nbt != null) {
            entity.readFromNBT(JSON2NBT.localizeEntity(nbt, realX, pos.getY(), realZ));
        }
        entity.setPosition(realX, pos.getY(), realZ);
        world.spawnEntityInWorld(entity);
    }
}
