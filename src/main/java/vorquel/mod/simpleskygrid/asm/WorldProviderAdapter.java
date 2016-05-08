package vorquel.mod.simpleskygrid.asm;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.ChunkGeneratorSkyGrid;

import static vorquel.mod.simpleskygrid.asm.Mappings.*;

public class WorldProviderAdapter extends ClassVisitor implements Opcodes{
    
    public WorldProviderAdapter(ClassVisitor cv) {
        super(ASM5, cv);
        if(uninitialized)
            Log.kill("obfuscation has not been set.");
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if(name.equals(mCreateChunkGenerator) && desc.equals("()L" + cIChunkGenerator + ";"))
            mv = new CreateChunkGeneratorAdapter(mv);
        return mv;
    }

    private class CreateChunkGeneratorAdapter extends MethodVisitor {

        public CreateChunkGeneratorAdapter(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "vorquel/mod/simpleskygrid/asm/WorldProviderAdapter", "useMyChunkProvider", "(L" + cWorldProvider + ";)Z", false);
            Label label = new Label();
            mv.visitJumpInsn(IFEQ, label);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "vorquel/mod/simpleskygrid/asm/WorldProviderAdapter", "createChunkProviderSkyGrid", "(L" + cWorldProvider + ";)Lvorquel/mod/simpleskygrid/world/ChunkGeneratorSkyGrid;", false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label);
        }
    }
    
    @SuppressWarnings("unused")
    public static boolean useMyChunkProvider(WorldProvider provider) {
        World world = ReflectionHelper.getPrivateValue(WorldProvider.class, provider, "worldObj", "field_76579_a");
        return world.getWorldInfo().getTerrainType() == Ref.worldType && Config.dimensionPropertiesMap.containsKey(provider.getDimension());
    }
    
    @SuppressWarnings("unused")
    public static ChunkGeneratorSkyGrid createChunkProviderSkyGrid(WorldProvider provider) {
        World world = ReflectionHelper.getPrivateValue(WorldProvider.class, provider, "worldObj", "field_76579_a");
        return new ChunkGeneratorSkyGrid(world, provider.getSeed(), provider.getDimension());
    }
}
