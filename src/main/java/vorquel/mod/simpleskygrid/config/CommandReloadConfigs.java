package vorquel.mod.simpleskygrid.config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.ChunkGeneratorSkyGrid;

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
    public boolean checkPermission(MinecraftServer server, ICommandSender commandSender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] options) {
        Config.loadConfigs();
        Ref.createGenerators();
        for(ChunkGeneratorSkyGrid provider : ChunkGeneratorSkyGrid.providers.keySet())
            provider.resetProperties();
        System.gc();
        notifyCommandListener(commandSender, this, "commands.ssgReloadConfigs.success");
    }
}
