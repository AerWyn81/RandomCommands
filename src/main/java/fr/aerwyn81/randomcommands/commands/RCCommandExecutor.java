package fr.aerwyn81.randomcommands.commands;

import fr.aerwyn81.randomcommands.commands.list.*;
import fr.aerwyn81.randomcommands.services.LanguageService;
import fr.aerwyn81.randomcommands.utils.bukkit.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class RCCommandExecutor implements CommandExecutor, TabCompleter {
    private final HashMap<String, RCCommand> registeredCommands;

    private final Help helpCommand;

    public RCCommandExecutor() {
        this.registeredCommands = new HashMap<>();

        this.helpCommand = new Help();

        this.register(helpCommand);
        this.register(new Reload());
        this.register(new List());
        this.register(new Version());
        this.register(new Reset());
        this.register(new Force());
    }

    private void register(Cmd c) {
        RCCommand command = new RCCommand(c);

        registeredCommands.put(command.getCommand(), command);

        if (command.isVisible()) {
            helpCommand.addCommand(command);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(LanguageService.getMessage("Messages.ErrorCommand"));
            return false;
        }

        RCCommand command = registeredCommands.get(args[0].toLowerCase());

        if (command == null) {
            sender.sendMessage(LanguageService.getMessage("Messages.ErrorCommand"));
            return false;
        }

        if (!PlayerUtils.hasPermission(sender, command.getPermission())) {
            sender.sendMessage(LanguageService.getMessage("Messages.NoPermission"));
            return false;
        }

        if (command.isPlayerCommand() && !(sender instanceof Player)) {
            sender.sendMessage(LanguageService.getMessage("Messages.PlayerOnly"));
            return false;
        }

        int argsWithoutCmd = Arrays.copyOfRange(args, 1, args.length).length;

        if (argsWithoutCmd < command.getArgs().length) {
            sender.sendMessage(LanguageService.getMessage("Messages.ErrorCommand"));
            return false;
        }

        return registeredCommands.get(args[0].toLowerCase()).getCmdClass().perform(sender, args);
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command c, String s, String[] args) {
        if(args.length == 1) {
            return registeredCommands.keySet().stream()
                    .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                    .filter(arg -> registeredCommands.get(arg).isVisible())
                    .filter(arg -> PlayerUtils.hasPermission(sender, registeredCommands.get(arg).getPermission())).distinct()
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (!registeredCommands.containsKey(args[0].toLowerCase())) {
            return new ArrayList<>();
        }

        RCCommand command = registeredCommands.get(args[0].toLowerCase());

        if (!PlayerUtils.hasPermission(sender, command.getPermission())) {
            return new ArrayList<>();
        }

        return command.getCmdClass().tabComplete(sender, args);
    }
}