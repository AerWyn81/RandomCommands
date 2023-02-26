package fr.aerwyn81.randomcommands.commands.list;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.commands.Cmd;
import fr.aerwyn81.randomcommands.commands.RCAnnotations;
import fr.aerwyn81.randomcommands.services.LanguageService;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

@RCAnnotations(command = "version", permission = "randomcommands.use")
public class Version implements Cmd {

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        sender.sendMessage(LanguageService.getMessage("Messages.Version")
                .replaceAll("%version%", RandomCommands.getInstance().getDescription().getVersion()));

        return true;
    }

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
