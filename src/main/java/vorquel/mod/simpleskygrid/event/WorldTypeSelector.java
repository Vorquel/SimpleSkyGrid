package vorquel.mod.simpleskygrid.event;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vorquel.mod.simpleskygrid.config.SimpleSkyGridConfigReaders;
import vorquel.mod.simpleskygrid.helper.Ref;

public class WorldTypeSelector {

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void changeWorldType(GuiScreenEvent.InitGuiEvent.Post event) {
        if(event.getGui() instanceof GuiCreateWorld) {
            if(SimpleSkyGridConfigReaders.skyGridDefault) {
                ReflectionHelper.setPrivateValue(GuiCreateWorld.class, (GuiCreateWorld) event.getGui(), Ref.worldType.getWorldTypeID(), "selectedIndex", "field_146331_K");
                for(Object object : event.getButtonList()) {
                    GuiButton button = (GuiButton) object;
                    if(button.id != 5)
                        continue;
                    button.displayString = I18n.format("selectWorld.mapType") + " " + I18n.format(Ref.worldType.getTranslateName());
                    break;
                }
            }
        }
    }
}
