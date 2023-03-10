package fr.aerwyn81.randomcommands.commands.list;

import fr.aerwyn81.randomcommands.commands.Cmd;
import fr.aerwyn81.randomcommands.commands.RCAnnotations;
import fr.aerwyn81.randomcommands.commands.RCCommand;
import fr.aerwyn81.randomcommands.services.LanguageService;
import fr.aerwyn81.randomcommands.utils.chat.ChatPageUtils;
import fr.aerwyn81.randomcommands.utils.message.MessageUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@RCAnnotations(command = "help", permission = "randomcommands.use")
public class Help implements Cmd {
    private final ArrayList<RCCommand> registeredCommands;

    public Help() {
        this.registeredCommands = new ArrayList<>();
    }

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        ChatPageUtils cpu = new ChatPageUtils(sender)
                .entriesCount(registeredCommands.size())
                .currentPage(args);

        String message = LanguageService.getMessage("Chat.LineTitle");
        if (sender instanceof Player) {
            TextComponent titleComponent = new TextComponent(message);
            cpu.addTitleLine(titleComponent);
        } else {
            sender.sendMessage(message);
        }

        for (int i = cpu.getFirstPos(); i < cpu.getFirstPos() + cpu.getPageHeight() && i < cpu.getSize() ; i++) {
            String command = StringUtils.capitalize(registeredCommands.get(i).getCommand())
                    .replaceAll("all", "All");

            if (!LanguageService.hasMessage("Help." + command)) {
                sender.sendMessage(MessageUtils.colorize("&6/hb " + registeredCommands.get(i).getCommand() + " &8: &c&oNo help message found. Please report to developer!"));
            } else {
                message = LanguageService.getMessage("Help." + command);
                if (sender instanceof Player) {
                    cpu.addLine(new TextComponent(message));
                } else {
                    sender.sendMessage(message);
                }
            }
        }

        cpu.addPageLine("help");
        cpu.build();
        return true;
    }

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    public void addCommand(RCCommand command) {
        registeredCommands.add(command);
    }
}
