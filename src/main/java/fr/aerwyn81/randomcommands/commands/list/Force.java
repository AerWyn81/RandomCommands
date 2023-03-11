package fr.aerwyn81.randomcommands.commands.list;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.commands.Cmd;
import fr.aerwyn81.randomcommands.commands.RCAnnotations;
import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.services.LanguageService;
import fr.aerwyn81.randomcommands.utils.internal.RunningCommandsHelper;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RCAnnotations(command = "force", permission = "randomcommands.admin", args = "id")
public class Force implements Cmd {

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        var commands = ConfigService.getCommands();

        if (commands.size() == 0) {
            sender.sendMessage(LanguageService.getMessage("Messages.ListCommandsEmpty"));
            return true;
        }

        var id = args[1];
        var command = commands.stream().filter(c -> Objects.equals(c.id(), id)).findFirst();
        if (command.isEmpty()) {
            sender.sendMessage(LanguageService.getMessage("Messages.NoCommandMatchingId").replaceAll("%id%", id));
            return true;
        }

        RunningCommandsHelper.runCommand(command.get());
        sender.sendMessage(LanguageService.getMessage("Messages.CommandForced").replaceAll("%id%", id));
        return true;
    }

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        return args.length == 2 ? new ArrayList<>(ConfigService.getCommands().stream().map(Command::id).filter(s -> s.startsWith(args[1])).collect(Collectors.toList())) : new ArrayList<>();
    }
}
