package fr.aerwyn81.randomcommands.utils.internal;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.datas.Command;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class RunningCommandsHelper {

    public static Optional<Command> getCommandById(String id) {
        return StreamSupport.stream(RandomCommands.runningCommands.spliterator(), false).filter(s -> Objects.equals(s.id(), id)).findFirst();
    }

    public static void runCommand(Command command) {
        var plugin = RandomCommands.getInstance();

        Bukkit.getScheduler().runTaskLater(plugin, () -> command.commands().forEach(cmd ->
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd)), 1L);
    }
}
