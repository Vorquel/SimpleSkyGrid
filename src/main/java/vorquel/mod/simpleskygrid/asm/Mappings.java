package vorquel.mod.simpleskygrid.asm;

public class Mappings {
    
    //class names
    public static String cBlock          = "net/minecraft/block/Block";
    public static String cIChunkProvider = "net/minecraft/world/chunk/IChunkProvider";
    public static String cWorld          = "net/minecraft/world/World";
    public static String cWorldInfo      = "net/minecraft/world/storage/WorldInfo";
    public static String cWorldProvider  = "net/minecraft/world/WorldProvider";
    public static String cWorldType      = "net/minecraft/world/WorldType";
    //method names
    public static String mCreateChunkGenerator;
    public static String mGetSeed;
    public static String mGetTerrainType;
    public static String mOnNeighborBlockChange;
    public static String mUpdateTick;
    public static String mGetWorldInfo;
    //field names
    public static String fDimensionId;
    public static String fWorldObj;
    static boolean uninitialized = true;
    
    public static void initialize(boolean isSrgNames) {
        if(isSrgNames) {
            mCreateChunkGenerator  = "func_76555_c";
            mGetSeed               = "func_72905_C";
            mGetTerrainType        = "func_76067_t";
            mOnNeighborBlockChange = "func_149695_a";
            mUpdateTick            = "func_149674_a";
            mGetWorldInfo          = "func_72912_H";
            
            fDimensionId = "field_76574_g";
            fWorldObj    = "field_76579_a";
        } else {
            mCreateChunkGenerator  = "createChunkGenerator";
            mGetSeed               = "getSeed";
            mGetTerrainType        = "getTerrainType";
            mOnNeighborBlockChange = "onNeighborBlockChange";
            mUpdateTick            = "updateTick";
            mGetWorldInfo          = "getWorldInfo";
            
            fDimensionId = "dimensionId";
            fWorldObj    = "worldObj";
        }
        uninitialized = false;
    }
}
