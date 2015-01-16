package vorquel.mod.simpleskygrid.helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;

public class BlockCache {
    public static Block air = GameData.getBlockRegistry().getObject("minecraft:air");
    public static Block bedrock = GameData.getBlockRegistry().getObject("minecraft:bedrock");
    public static Block chest = GameData.getBlockRegistry().getObject("minecraft:chest");
    public static Block endPortalFrame = GameData.getBlockRegistry().getObject("minecraft:end_portal_frame");
}
