package vorquel.mod.simpleskygrid.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vorquel.mod.simpleskygrid.helper.Log;

public class WorldProviderAdapter extends ClassVisitor implements Opcodes{

    private static boolean uninitialized = true;

    //class names
    public static String cIChunkProvider = "net/minecraft/world/chunk/IChunkProvider";
    public static String cWorld          = "net/minecraft/world/World";
    public static String cWorldInfo      = "net/minecraft/world/storage/WorldInfo";
    public static String cWorldProvider  = "net/minecraft/world/WorldProvider";
    public static String cWorldType      = "net/minecraft/world/WorldType";

    //method names
    public static String mCreateChunkGenerator;
    public static String mGetSeed;
    public static String mGetTerrainType;
    public static String mGetWorldInfo;

    //field names
    public static String fDimensionId;
    public static String fWorldObj;

    public static void initialize(boolean isSrgNames) {
        if(isSrgNames) {
            mCreateChunkGenerator = "func_76555_c";
            mGetSeed              = "func_72905_C";
            mGetTerrainType       = "func_76067_t";
            mGetWorldInfo         = "func_72912_H";

            fDimensionId = "field_76574_g";
            fWorldObj    = "field_76579_a";
        } else {
            mCreateChunkGenerator = "createChunkGenerator";
            mGetSeed              = "getSeed";
            mGetTerrainType       = "getTerrainType";
            mGetWorldInfo         = "getWorldInfo";

            fDimensionId = "dimensionId";
            fWorldObj    = "worldObj";
        }
        uninitialized = false;
    }

    public WorldProviderAdapter(ClassVisitor cv) {
        super(ASM5, cv);
        if(uninitialized)
            Log.kill("obfuscation has not been set.");
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if(name.equals(mCreateChunkGenerator) && desc.equals("()L" + cIChunkProvider + ";"))
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
            mv.visitFieldInsn(GETFIELD, cWorldProvider, fWorldObj, "L" + cWorld + ";");
            mv.visitMethodInsn(INVOKEVIRTUAL, cWorld, mGetWorldInfo, "()L" + cWorldInfo + ";", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, cWorldInfo, mGetTerrainType, "()L" + cWorldType + ";", false);
            mv.visitFieldInsn(GETSTATIC, "vorquel/mod/simpleskygrid/helper/Ref", "worldType", "Lvorquel/mod/simpleskygrid/world/WorldTypeSkyGrid;");
            Label label = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label);
            mv.visitFieldInsn(GETSTATIC, "vorquel/mod/simpleskygrid/config/Config", "dimensionPropertiesMap", "Ljava/util/HashMap;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, cWorldProvider, fDimensionId, "I");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "containsKey", "(Ljava/lang/Object;)Z", false);
            mv.visitJumpInsn(IFEQ, label);
            mv.visitTypeInsn(NEW, "vorquel/mod/simpleskygrid/world/ChunkProviderSkyGrid");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, cWorldProvider, fWorldObj, "L" + cWorld + ";");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, cWorldProvider, fWorldObj, "L" + cWorld + ";");
            mv.visitMethodInsn(INVOKEVIRTUAL, cWorld, mGetSeed, "()J", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, cWorldProvider, fDimensionId, "I");
            mv.visitMethodInsn(INVOKESPECIAL, "vorquel/mod/simpleskygrid/world/ChunkProviderSkyGrid", "<init>", "(L" + cWorld + ";JI)V", false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label);
        }
    }
}
