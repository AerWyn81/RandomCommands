package fr.aerwyn81.randomcommands.commands.list;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.commands.Cmd;
import fr.aerwyn81.randomcommands.commands.RCAnnotations;
import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.services.LanguageService;
import fr.aerwyn81.randomcommands.utils.internal.RunningCommandsHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RCAnnotations(command = "reset", permission = "randomcommands.admin", args = "id")
public class Reset implements Cmd {

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        var runningCommands = RandomCommands.runningCommands;

        if (runningCommands.size() == 0) {
            sender.sendMessage(LanguageService.getMessage("Messages.NoRunningTimer"));
            return true;
        }

        var id = args[1];

        var command = RunningCommandsHelper.getCommandById(id);
        if (command.isEmpty()) {
            sender.sendMessage(LanguageService.getMessage("Messages.NoCommandMatchingId")
                    .replaceAll("%id%", id));
            return true;
        }

        var plugin = RandomCommands.getInstance();
        Bukkit.getScheduler().runTaskLater(plugin, () ->  command.get().commandsOnReset().forEach(cmd ->
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd)), 1L);

        runningCommands.remove(command.get());

        sender.sendMessage(LanguageService.getMessage("Messages.RunningCommandReseted")
                .replaceAll("%id%", id));
        return true;
    }

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        return args.length == 2 ? new ArrayList<>(StreamSupport.stream(RandomCommands.runningCommands.spliterator(), false).map(Command::id).filter(s -> s.startsWith(args[1])).collect(Collectors.toList())) : new ArrayList<>();
    }
}
