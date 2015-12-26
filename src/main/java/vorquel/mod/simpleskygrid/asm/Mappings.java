package vorquel.mod.simpleskygrid.asm;

public class Mappings {
    
    //class names
    public static String cBlock          = "net/minecraft/block/Block";
    public static String cIChunkProvider = "net/minecraft/world/chunk/IChunkProvider";
    public static String cWorld          = "net/minecraft/world/World";
    public static String cWorldProvider  = "net/minecraft/world/WorldProvider";
    //method names
    public static String mCreateChunkGenerator;
    public static String mOnNeighborBlockChange;
    public static String mUpdateTick;
    //field names
    //(none right now)
    static boolean uninitialized = true;
    
    public static void initialize(boolean isSrgNames) {
        if(isSrgNames) {
            mCreateChunkGenerator  = "func_76555_c";
            mOnNeighborBlockChange = "func_149695_a";
            mUpdateTick            = "func_149674_a";
        } else {
            mCreateChunkGenerator  = "createChunkGenerator";
            mOnNeighborBlockChange = "onNeighborBlockChange";
            mUpdateTick            = "updateTick";
        }
        uninitialized = false;
    }
}
