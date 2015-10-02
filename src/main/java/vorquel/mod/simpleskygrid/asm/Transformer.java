package vorquel.mod.simpleskygrid.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import vorquel.mod.simpleskygrid.helper.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Transformer implements IClassTransformer {

    private String worldProvider = WorldProviderAdapter.cWorldProvider.replace('/', '.');
    private HashMap<String, Boolean> instanceOfWorldProviderMap = new HashMap<>();

    {
        instanceOfWorldProviderMap.put(worldProvider, true);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] classData) {
        if(classData == null) return null;
        if(isInstanceOfWorldProvider(transformedName, new ByteArrayInputStream(classData))) {
            ClassReader cr = new ClassReader(classData);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            WorldProviderAdapter wpa = new WorldProviderAdapter(cw);
            cr.accept(wpa, 0);
            return cw.toByteArray();
        } else
            return classData;
    }

    private boolean isInstanceOfWorldProvider(String className, InputStream classStream) {
        if(instanceOfWorldProviderMap.containsKey(className))
            return instanceOfWorldProviderMap.get(className);
        boolean isInstanceOfWorldProvider = false;
        try {
            String superClassName = new ClassReader(classStream).getSuperName();
            if(superClassName != null) {
                InputStream superClassStream = this.getClass().getClassLoader().getResourceAsStream(superClassName + ".class");
                isInstanceOfWorldProvider = isInstanceOfWorldProvider(superClassName.replace('/', '.'), superClassStream);
            }
        } catch (IOException e) {
            Log.kill("Exception determining class hierarchy for %s: %s", className, e.getMessage());
        }
        instanceOfWorldProviderMap.put(className, isInstanceOfWorldProvider);
        return isInstanceOfWorldProvider;
    }
}
