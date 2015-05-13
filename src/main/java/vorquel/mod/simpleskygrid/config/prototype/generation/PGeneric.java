package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

public class PGeneric extends Prototype<IGeneratedObject> {

    private String name;

    public PGeneric(SimpleSkyGridConfigReader reader) {
        super(reader);
    }

    @Override
    protected void readLabel(SimpleSkyGridConfigReader reader, String label) {
        if(label.equals("name")) name = reader.nextString();
        else reader.unknownOnce("label " + label, "generic block definition");
    }

    @Override
    public boolean isComplete() {
        return name != null;
    }

    @Override
    public IGeneratedObject getObject() {
        for(ItemStack stack : OreDictionary.getOres(name)) {
            if(stack == null)
                continue;
            Item item = stack.getItem();
            Block block = Block.getBlockFromItem(item);
            if(block == Blocks.air)
                continue;
            int meta = item.getMetadata(stack.getItemDamage());
            return new GeneratedBlock(block, meta, null, null);
        }
        return null;
    }
}
