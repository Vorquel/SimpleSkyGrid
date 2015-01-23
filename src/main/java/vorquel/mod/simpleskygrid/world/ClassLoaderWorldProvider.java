package vorquel.mod.simpleskygrid.world;

import net.minecraft.world.WorldProvider;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;

public class ClassLoaderWorldProvider extends ClassLoader implements Opcodes {

    public static ClassLoaderWorldProvider that = new ClassLoaderWorldProvider(WorldProvider.class.getClassLoader());
    private HashMap<Class<? extends WorldProvider>, Class<? extends WorldProvider>> proxyMap = new HashMap<Class<? extends WorldProvider>, Class<? extends WorldProvider>>();
    private int count = 0;

    private ClassLoaderWorldProvider() {}

    private ClassLoaderWorldProvider(ClassLoader classLoader) {
        super(classLoader);
    }

    public Class<? extends WorldProvider> getWorldProviderProxy(Class<? extends WorldProvider> superClass) {
        if(proxyMap.containsKey(superClass))
            return proxyMap.get(superClass);
        String className = "vorquel.mod.simpleskygrid.world.SkyGridWorldProvider$" + count++;
        byte[] classBytes = generateClass(className, superClass.getCanonicalName());
        Class<? extends WorldProvider> thisClass = (Class<? extends WorldProvider>) defineClass(className, classBytes, 0, classBytes.length);
        proxyMap.put(superClass, thisClass);
        return thisClass;
    }

    private byte[] generateClass(String className, String superClassName) {
        String thisClass = className.replace('.', '/');
        String superClass = superClassName.replace('.', '/');
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, thisClass, null, superClass, null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, superClass, "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "createChunkGenerator", "()Lnet/minecraft/world/chunk/IChunkProvider;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, "worldObj", "Lnet/minecraft/world/World;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "getWorldInfo", "()Lnet/minecraft/world/storage/WorldInfo;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/storage/WorldInfo", "getTerrainType", "()Lnet/minecraft/world/WorldType;", false);
            mv.visitFieldInsn(GETSTATIC, "vorquel/mod/simpleskygrid/helper/Ref", "worldType", "Lvorquel/mod/simpleskygrid/world/WorldTypeSkyGrid;");
            Label l0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, l0);
            mv.visitTypeInsn(NEW, "vorquel/mod/simpleskygrid/world/ChunkProviderSkyGrid");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, "worldObj", "Lnet/minecraft/world/World;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, "worldObj", "Lnet/minecraft/world/World;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "getSeed", "()J", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, "dimensionId", "I");
            mv.visitMethodInsn(INVOKESPECIAL, "vorquel/mod/simpleskygrid/world/ChunkProviderSkyGrid", "<init>", "(Lnet/minecraft/world/World;JI)V", false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l0);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, superClass, "createChunkGenerator", "()Lnet/minecraft/world/chunk/IChunkProvider;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(6, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    public boolean hasProxy(Class<? extends WorldProvider> superClass) {
        return proxyMap.containsKey(superClass);
    }
}
