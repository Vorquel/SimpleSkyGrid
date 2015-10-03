package vorquel.mod.simpleskygrid.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import vorquel.mod.simpleskygrid.helper.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Transformer implements IClassTransformer {

    private static boolean useNotchNames;
    private static HashMap<String, Boolean> instanceOfWorldProviderMap = new HashMap<>();

    public static void initialize(boolean isSrgNames) {
        useNotchNames = isSrgNames;
        instanceOfWorldProviderMap.put(getGoodClassName(WorldProviderAdapter.cWorldProvider), true);
    }

    private static String getGoodClassName(String className) {
        if(useNotchNames)
            return FMLDeobfuscatingRemapper.INSTANCE.unmap(className);
        else
            return className;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] classData) {
        if(classData == null) return null;
        if(isInstanceOfWorldProvider(name.replace('.', '/'))) {
            ClassReader cr = new ClassReader(classData);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            WorldProviderAdapter wpa = new WorldProviderAdapter(cw);
            cr.accept(wpa, 0);
            return cw.toByteArray();
        } else
            return classData;
    }

    private boolean isInstanceOfWorldProvider(String className) {
        className = getGoodClassName(className);
        if(instanceOfWorldProviderMap.containsKey(className))
            return instanceOfWorldProviderMap.get(className);
        InputStream classStream = this.getClass().getClassLoader().getResourceAsStream(className + ".class");
        boolean isInstanceOfWorldProvider = false;
        try {
            String superClassName = new ClassReader(classStream).getSuperName();
            classStream.close();
            if(superClassName != null)
                isInstanceOfWorldProvider = isInstanceOfWorldProvider(superClassName);
        } catch (IOException e) {
            Log.error("Exception determining superclass of %s: %s", className, e.getMessage());
        }
        instanceOfWorldProviderMap.put(className, isInstanceOfWorldProvider);
        return isInstanceOfWorldProvider;
    }
}
