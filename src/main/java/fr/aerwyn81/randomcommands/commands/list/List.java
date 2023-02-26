package fr.aerwyn81.randomcommands.commands.list;

import fr.aerwyn81.randomcommands.commands.Cmd;
import fr.aerwyn81.randomcommands.commands.RCAnnotations;
import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.services.LanguageService;
import fr.aerwyn81.randomcommands.utils.chat.ChatPageUtils;
import fr.aerwyn81.randomcommands.utils.message.MessageUtils;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@RCAnnotations(command = "list", permission = "randomcommands.admin")
public class List implements Cmd {

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        ArrayList<Command> commands = ConfigService.getCommands();

        if (commands.size() == 0) {
            sender.sendMessage(LanguageService.getMessage("Messages.ListCommandsEmpty"));
            return true;
        }

        ChatPageUtils cpu = new ChatPageUtils(sender)
                .entriesCount(commands.size())
                .currentPage(args);

        String message = LanguageService.getMessage("Chat.LineTitle");
        if (sender instanceof Player) {
            TextComponent titleComponent = new TextComponent(message);
            cpu.addTitleLine(titleComponent);
        } else {
            sender.sendMessage(message);
        }

        for (int i = cpu.getFirstPos(); i < cpu.getFirstPos() + cpu.getPageHeight() && i < cpu.getSize(); i++) {
            Command command = commands.get(i);

            TextComponent msg = new TextComponent(MessageUtils.colorize("&7| &6" + (i + 1) + ". &e" + command.id()));

            if (sender instanceof Player) {
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(command.requirements().display())));
                cpu.addLine(msg);

                for (String cmd : command.commands()) {
                    msg = new TextComponent(MessageUtils.colorize("&7|       &7- &f" + cmd));
                    cpu.addLine(msg);
                }
            } else {
                sender.sendMessage(MessageUtils.colorize("&6" + command.toString()));
            }
        }

        cpu.addPageLine("list");
        cpu.build();
        return true;
    }

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
