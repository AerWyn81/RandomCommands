package fr.aerwyn81.randomcommands;

import fr.aerwyn81.randomcommands.commands.RCCommandExecutor;
import fr.aerwyn81.randomcommands.runnables.GlobalTask;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.services.LanguageService;
import fr.aerwyn81.randomcommands.utils.message.MessageUtils;
import fr.aerwyn81.randomcommands.utils.message.config.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class RandomCommands extends JavaPlugin {
    public static ConsoleCommandSender log;
    private static RandomCommands instance;

    private GlobalTask globalTask;

    @Override
    public void onEnable() {
        instance = this;
        log = Bukkit.getConsoleSender();

        log.sendMessage(MessageUtils.colorize("&4&lR&candom&6&lC&eommands &einitializing..."));

        File configFile = new File(getDataFolder(), "config.yml");

        saveDefaultConfig();
        try {
            ConfigUpdater.update(this, "config.yml", configFile, List.of("groups"));
        } catch (IOException e) {
            log.sendMessage(MessageUtils.colorize("&cError while loading config file: " + e.getMessage()));
            this.setEnabled(false);
            return;
        }
        reloadConfig();

        ConfigService.initialize(configFile);

        LanguageService.initialize(ConfigService.getLanguage());
        LanguageService.pushMessages();

        if (ConfigService.isTimerEnabled()) {
            globalTask = new GlobalTask();
            globalTask.runTaskTimer(this, 0, 20L);
        }

        getCommand("randomcommands").setExecutor(new RCCommandExecutor());

        log.sendMessage(MessageUtils.colorize("&6&lH&e&lead&6&lB&e&llocks &asuccessfully loaded!"));
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        log.sendMessage(MessageUtils.colorize("&4&lR&candom&6&lC&eommands &cdisabled!"));
    }

    public static RandomCommands getInstance() {
        return instance;
    }

    public Optional<GlobalTask> getGlobalTask() {
        return Optional.ofNullable(globalTask);
    }

    public void setGlobalTask(GlobalTask globalTask) {
        this.globalTask = globalTask;
    }
}
