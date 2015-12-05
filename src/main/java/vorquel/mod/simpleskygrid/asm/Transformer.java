package vorquel.mod.simpleskygrid.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import vorquel.mod.simpleskygrid.helper.Log;

import java.util.HashMap;
import java.util.Set;

import static vorquel.mod.simpleskygrid.asm.Mappings.cBlock;
import static vorquel.mod.simpleskygrid.asm.Mappings.cWorldProvider;

@SuppressWarnings("unused")
public class Transformer implements IClassTransformer {

    private static HashMap<String, Boolean> instanceOfWorldProviderMap = new HashMap<>();
    private static SuperClassMap superClassMap;

    public static void initialize(boolean isSrgNames) {
        superClassMap = new SuperClassMap(isSrgNames, cBlock, cWorldProvider);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] classData) {
        if(classData == null) return null;
        Set<String> matches = superClassMap.get(name);
        if(matches.contains(cBlock)) {
            Log.info("Found block class: %s", transformedName);
        }
        if(matches.contains(cWorldProvider)) {
            ClassReader cr = new ClassReader(classData);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            WorldProviderAdapter wpa = new WorldProviderAdapter(cw);
            cr.accept(wpa, 0);
            classData = cw.toByteArray();
        }
        return classData;
    }
}
