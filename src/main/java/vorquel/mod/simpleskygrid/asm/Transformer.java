package vorquel.mod.simpleskygrid.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import static vorquel.mod.simpleskygrid.asm.WorldProviderAdapter.cWorldProvider;

@SuppressWarnings("unused")
public class Transformer implements IClassTransformer {

    private String worldProvider = WorldProviderAdapter.cWorldProvider.replace('/', '.');

    @Override
    public byte[] transform(String name, String transformedName, byte[] clazz) {
        ClassReader cr = new ClassReader(clazz);
        if(transformedName.equals(worldProvider) || cr.getSuperName().equals(cWorldProvider)) {
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            WorldProviderAdapter wpa = new WorldProviderAdapter(cw);
            cr.accept(wpa, 0);
            return cw.toByteArray();
        } else
            return clazz;
    }
}
