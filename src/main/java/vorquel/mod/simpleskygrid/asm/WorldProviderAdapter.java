package vorquel.mod.simpleskygrid.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vorquel.mod.simpleskygrid.helper.Log;

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
