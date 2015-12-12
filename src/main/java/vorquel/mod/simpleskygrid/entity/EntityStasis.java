package vorquel.mod.simpleskygrid.entity;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
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
                AxisAlignedBB.fromBounds(x + .25, y + .25, z + .25, x + .75, y + .75, z + .75)).isEmpty();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float p_70070_1_) {
        return 0xf000f0;
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if(source.damageType.equals("player")) {
            playSound(Blocks.glass.stepSound.getStepSound(), 1f, .75f + rand.nextFloat() * .5f);
            this.kill();
        }
        return false;
    }
    
    @Override
    protected void entityInit() {
        setSize(1.25f, 1.25f);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        return false;
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompound) {}
    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
    @Override
    protected void doBlockCollisions() {}
    @Override
    public void onUpdate() {}
    @Override
    public void moveEntity(double x, double y, double z) {}
}
