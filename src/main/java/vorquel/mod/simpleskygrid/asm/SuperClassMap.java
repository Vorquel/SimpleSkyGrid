package vorquel.mod.simpleskygrid.asm;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import vorquel.mod.simpleskygrid.helper.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class SuperClassMap {
    
    private boolean useNotchNames;
    private Map<String, String> toMatch;
    private Map<String, Set<String>> map = new HashMap<>();
    
    SuperClassMap(boolean useNotchNames, String... toMatch) {
        this.useNotchNames = useNotchNames;
        this.toMatch = Maps.newHashMap();
        for(String className : toMatch) {
            this.toMatch.put(getGoodClassName(className), className);
        }
    }
    
    Set<String> get(String className) {
        className = getGoodClassName(className);
        if(map.containsKey(className))
            return map.get(className);
        else {
            InputStream classStream = this.getClass().getClassLoader().getResourceAsStream(className + ".class");
            Set<String> matching = Sets.newHashSet();
            if(toMatch.containsKey(className))
                matching.add(toMatch.get(className));
            try {
                String superClassName = new ClassReader(classStream).getSuperName();
                classStream.close();
                if(superClassName != null)
                    matching.addAll(get(superClassName));
            } catch (IOException e) {
                Log.error("Exception determining superclass of %s: %s", className, e.getMessage());
            }
            map.put(className, matching);
            return matching;
        }
    }
    
    private String getGoodClassName(String className) {
        if(className.indexOf('.') != -1)
            className = className.replace('.', '/');
        if(useNotchNames)
            return FMLDeobfuscatingRemapper.INSTANCE.unmap(className);
        else
            return className;
    }
}
