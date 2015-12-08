package vorquel.mod.simpleskygrid.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityStasis extends Entity {
    
    public EntityStasis(World world) {
        super(world);
    }
    
    @SuppressWarnings("unused")
    public static boolean shouldCancelUpdate(World world, int x, int y, int z) {
        return !world.getEntitiesWithinAABB(EntityStasis.class,
                AxisAlignedBB.getBoundingBox(x + .25, y + .25, z + .25, x + .75, y + .75, z + .75)).isEmpty();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if(source.damageType.equals("player"))
            this.kill();
        return false;
    }
    
    @Override
    protected void entityInit() {
        width = 1.25f;
        height = 1.25f;
        yOffset = .5f;
        yOffset2 = 0;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompound) {}
    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
    @Override
    public void onUpdate() {}
}
