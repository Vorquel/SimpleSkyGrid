package vorquel.mod.simpleskygrid.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.helper.Ref;

import java.util.Map;

@SuppressWarnings("unused")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("vorquel.mod.simpleskygrid.asm.")
public class Plugin implements IFMLLoadingPlugin {

    static {
        Log.setLogger(LogManager.getLogger(Ref.MOD_ID));
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"vorquel.mod.simpleskygrid.asm.Transformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        WorldProviderAdapter.initialize((Boolean) data.get("runtimeDeobfuscationEnabled"));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
