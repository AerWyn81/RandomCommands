package fr.aerwyn81.randomcommands.commands.list;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.commands.Cmd;
import fr.aerwyn81.randomcommands.commands.RCAnnotations;
import fr.aerwyn81.randomcommands.runnables.GlobalTask;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.services.LanguageService;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

@RCAnnotations(command = "reload", permission = "randomcommands.admin")
public class Reload implements Cmd {

    @Override
    public boolean perform(CommandSender sender, String[] args) {
        var plugin = RandomCommands.getInstance();

        if (plugin.getGlobalTask().isPresent()) {
            plugin.getGlobalTask().get().cancel();
        }

        plugin.reloadConfig();
        ConfigService.load();

        LanguageService.setLanguage(ConfigService.getLanguage());
        LanguageService.pushMessages();

        if (ConfigService.isTimerEnabled()) {
            plugin.setGlobalTask(new GlobalTask());
            plugin.getGlobalTask().get().runTaskTimer(plugin, 0, ConfigService.getInternalCheckTime() * 1000L);
        }

        sender.sendMessage(LanguageService.getMessage("Messages.ReloadComplete"));
        return true;
    }

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
