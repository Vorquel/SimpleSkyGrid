package vorquel.mod.simpleskygrid.world.provider;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

import java.io.IOException;
import java.util.HashMap;

public class ClassLoaderWorldProvider extends ClassLoader implements Opcodes {

    private static final String thisClassOld = WorldProviderSkyGrid.class.getCanonicalName().replace('.', '/');
    private static final String superClassOld = WorldProviderSurface.class.getCanonicalName().replace('.', '/');

    public static ClassLoaderWorldProvider that = new ClassLoaderWorldProvider(WorldProvider.class.getClassLoader());
    private HashMap<Class<? extends WorldProvider>, Class<? extends WorldProvider>> proxyMap = new HashMap<>();
    private int count = 0;

    private ClassLoaderWorldProvider() {}

    private ClassLoaderWorldProvider(ClassLoader classLoader) {
        super(classLoader);
    }

    public Class<? extends WorldProvider> getWorldProviderProxy(Class<? extends WorldProvider> superClass) {
        if(proxyMap.containsKey(superClass))
            return proxyMap.get(superClass);
        String className = "vorquel.mod.simpleskygrid.world.provider.WorldProviderSkyGrid$" + count++;
        byte[] classBytes = generateClass(className, superClass.getCanonicalName());
        Class<? extends WorldProvider> thisClass = (Class<? extends WorldProvider>) defineClass(className, classBytes, 0, classBytes.length);
        proxyMap.put(superClass, thisClass);
        return thisClass;
    }

    private byte[] generateClass(String className, String superClassName) {
        String thisClass = className.replace('.', '/');
        String superClass = superClassName.replace('.', '/');
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put(thisClassOld, thisClass);
        nameMap.put(superClassOld, superClass);
        ClassWriter classWriter = new ClassWriter(0);
        SimpleRemapper simpleRemapper = new SimpleRemapper(nameMap);
        RemappingClassAdapter remappingClassAdapter = new RemappingClassAdapter(classWriter, simpleRemapper);
        try {
            ClassReader classReader = new ClassReader(WorldProviderSkyGrid.class.getCanonicalName());
            classReader.accept(remappingClassAdapter, ClassReader.EXPAND_FRAMES);
        } catch(IOException e) {
            SimpleSkyGrid.logger.fatal("Unable to create Simple Sky Grid World Providers");
            throw new RuntimeException("Unable to create Simple Sky Grid World Providers");
        }
        return classWriter.toByteArray();
    }

    public boolean hasProxy(Class<? extends WorldProvider> superClass) {
        return proxyMap.containsKey(superClass);
    }
}
