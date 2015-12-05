package vorquel.mod.simpleskygrid.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import vorquel.mod.simpleskygrid.helper.Log;

import static vorquel.mod.simpleskygrid.asm.Mappings.*;

public class BlockAdapter extends ClassVisitor implements Opcodes {
    
    public BlockAdapter(ClassVisitor cv) {
        super(ASM5, cv);
        if(uninitialized)
            Log.kill("obfuscation has not been set.");
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if(isAdaptedMethod(name, desc))
            mv = new MethodAdapter(mv);
        return mv;
    }
    
    private boolean isAdaptedMethod(String name, String desc) {
        return name.equals(mOnNeighborBlockChange) && desc.equals("(L" + cWorld + ";IIIL" + cBlock + ";)V")
                       || name.equals(mUpdateTick) && desc.equals("(L" + cWorld + ";IIILjava/util/Random;)V");
    }
    
    private class MethodAdapter extends MethodVisitor {
        
        public MethodAdapter(MethodVisitor mv) {
            super(ASM5, mv);
        }
        
        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitVarInsn(ILOAD, 3);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitMethodInsn(INVOKESTATIC, "vorquel/mod/simpleskygrid/entity/EntityStasis", "shouldCancelUpdate", "(L" + cWorld + ";III)Z", false);
            Label label = new Label();
            mv.visitJumpInsn(IFEQ, label);
            mv.visitInsn(RETURN);
            mv.visitLabel(label);
        }
    }
}
