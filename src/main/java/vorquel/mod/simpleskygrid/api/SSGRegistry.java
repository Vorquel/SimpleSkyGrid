package vorquel.mod.simpleskygrid.api;

import vorquel.mod.simpleskygrid.helper.JSON2NBT;
import vorquel.mod.simpleskygrid.helper.Log;

public class SSGRegistry {

    public static void registerBlockLocalizer(String className) {
        try {
            INBTLocalizer blockLocalizer = (INBTLocalizer) Class.forName(className).newInstance();
            JSON2NBT.addBlockLocalizer(blockLocalizer);
        } catch (InstantiationException e) {
            Log.error("Could not instantiate BlockLocalizer %s: %s", className, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.error("Could not access constructor for BlockLocalizer %s: %s", className, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.error("Could not find BlockLocalizer %s: %s", className, e.getMessage());
        }
    }
}
