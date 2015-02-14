package vorquel.mod.simpleskygrid.world.igenerated;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
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
    public void provideObject(Random random, World world, Chunk chunk, int x, int y, int z) {
        Entity entity = EntityList.createEntityByName(name, world);
        double realX = chunk.xPosition * 16 + x + .5;
        double realZ = chunk.zPosition * 16 + z + .5;
        if(nbt != null) {
            NBTString.localizeNBT(nbt, realX, y, realZ);
            entity.readFromNBT(nbt);
        }
        entity.setPosition(realX, y, realZ);
        chunk.addEntity(entity);
    }
}
