package vorquel.mod.simpleskygrid.config.prototype.generation;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReader;
import vorquel.mod.simpleskygrid.config.prototype.Prototype;
import vorquel.mod.simpleskygrid.world.generated.GeneratedBlock;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.util.Arrays;

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
        return name != null && Arrays.asList(OreDictionary.getOreNames()).contains(name);
    }

    @Override
    public IGeneratedObject getObject() {
        ItemStack stack = OreDictionary.getOres(name).get(0);
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        int meta = item.getMetadata(stack.getItemDamage());
        return new GeneratedBlock(block, meta, null, null);
    }
}
