package vorquel.mod.simpleskygrid.world;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.world.WorldProvider;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.util.HashMap;

public class ClassLoaderWorldProvider extends ClassLoader implements Opcodes {

    public static ClassLoaderWorldProvider that = new ClassLoaderWorldProvider(WorldProvider.class.getClassLoader());
    private HashMap<Class<? extends WorldProvider>, Class<? extends WorldProvider>> proxyMap = new HashMap<Class<? extends WorldProvider>, Class<? extends WorldProvider>>();
    private int count = 0;

    private String cIChunkProvider;
    private String cWorld;
    private String cWorldInfo;
    private String cWorldType;
    private String mCreateChunkGenerator;
    private String mGetSeed;
    private String mGetTerrainType;
    private String mGetWorldInfo;
    private String fDimensionId;
    private String fWorldObj;

    {
        String test = ReflectionHelper.findField(WorldProvider.class, "dimensionId", "field_76574_g", "i").getName();
        if(test.equals("dimensionId")) { //mcp names
            cIChunkProvider =       "net/minecraft/world/chunk/IChunkProvider";
            cWorld =                "net/minecraft/world/World";
            cWorldInfo =            "net/minecraft/world/storage/WorldInfo";
            cWorldType =            "net/minecraft/world/WorldType";
            mCreateChunkGenerator = "createChunkGenerator";
            mGetSeed =              "getSeed";
            mGetTerrainType =       "getTerrainType";
            mGetWorldInfo =         "getWorldInfo";
            fDimensionId =          "dimensionId";
            fWorldObj =             "worldObj";
        } else if(test.equals("field_76574_g")) { //srg names
            cIChunkProvider =       "net/minecraft/world/chunk/IChunkProvider";
            cWorld =                "net/minecraft/world/World";
            cWorldInfo =            "net/minecraft/world/storage/WorldInfo";
            cWorldType =            "net/minecraft/world/WorldType";
            mCreateChunkGenerator = "func_76555_c";
            mGetSeed =              "func_72905_C";
            mGetTerrainType =       "func_76067_t";
            mGetWorldInfo =         "func_72912_H";
            fDimensionId =          "field_76574_g";
            fWorldObj =             "field_76579_a";
        } else { //unknown names
            SimpleSkyGrid.logger.fatal("Unknown obfuscation detected, unable to create world providers.");
            throw new RuntimeException("Unknown obfuscation detected, unable to create world providers.");
        }
    }

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
        String cChunkProviderSkyGrid = "vorquel/mod/simpleskygrid/world/ChunkProviderSkyGrid";
        String cRef =                  "vorquel/mod/simpleskygrid/helper/Ref";
        String cWorldTypeSkyGrid =     "vorquel/mod/simpleskygrid/world/WorldTypeSkyGrid";
        String fWorldType =            "worldType";

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
            mv = cw.visitMethod(ACC_PUBLIC, mCreateChunkGenerator, "()L" + cIChunkProvider + ";", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, fWorldObj, "L" + cWorld + ";");
            mv.visitMethodInsn(INVOKEVIRTUAL, cWorld, mGetWorldInfo, "()L" + cWorldInfo + ";", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, cWorldInfo, mGetTerrainType, "()L" + cWorldType + ";", false);
            mv.visitFieldInsn(GETSTATIC, cRef, fWorldType, "L" + cWorldTypeSkyGrid + ";");
            Label l0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, l0);
            mv.visitTypeInsn(NEW, cChunkProviderSkyGrid);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, fWorldObj, "L" + cWorld + ";");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, fWorldObj, "L" + cWorld + ";");
            mv.visitMethodInsn(INVOKEVIRTUAL, cWorld, mGetSeed, "()J", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisClass, fDimensionId, "I");
            mv.visitMethodInsn(INVOKESPECIAL, cChunkProviderSkyGrid, "<init>", "(L" + cWorld + ";JI)V", false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l0);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, superClass, mCreateChunkGenerator, "()L" + cIChunkProvider + ";", false);
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
