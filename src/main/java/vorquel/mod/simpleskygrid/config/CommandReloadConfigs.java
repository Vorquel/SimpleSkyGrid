package vorquel.mod.simpleskygrid.config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.ChunkProviderSkyGrid;

public class CommandReloadConfigs extends CommandBase {

    @Override
    public String getCommandName() {
        return "ssgReloadConfigs";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "commands.ssgReloadConfigs.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] options) {
        Config.loadConfigs();
        Ref.createGenerators();
        for(ChunkProviderSkyGrid provider : ChunkProviderSkyGrid.providers.keySet())
            provider.resetProperties();
        System.gc();
        func_152373_a(commandSender, this, "commands.ssgReloadConfigs.success");
    }
}
