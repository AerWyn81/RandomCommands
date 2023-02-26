package fr.aerwyn81.randomcommands.services;

import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.datas.Requirements;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class ConfigService {
    private static File configFile;
    private static FileConfiguration config;

    public static void initialize(File file) {
        configFile = file;
        load();
    }

    public static void load() {
        config = YamlConfiguration.loadConfiguration(configFile);
        commands = new ArrayList<>();
    }

    public static String getLanguage() {
        return config.getString("language", "en").toLowerCase();
    }

    public static boolean isTimerEnabled() {
        return config.getBoolean("enable", true);
    }

    private static ArrayList<Command> commands;
    public static ArrayList<Command> getCommands() {
        if (!commands.isEmpty())
            return commands;

        var commandsSection = config.getConfigurationSection("groups");
        if (commandsSection == null)
            return commands;

        for (String cmdGrp : commandsSection.getKeys(false)) {
            Optional<Integer> minPlayers = getOptionalIntegerFromConfig("groups." + cmdGrp + ".requirements.minPlayers");
            Optional<Integer> interval = getOptionalIntegerFromConfig("groups." + cmdGrp + ".requirements.interval");
            Optional<Integer> chance = getOptionalIntegerFromConfig("groups." + cmdGrp + ".requirements.chance");

            var command = new Command(
                    cmdGrp,
                    new Requirements(minPlayers, interval, chance),
                    new ArrayList<>(config.getStringList("groups." + cmdGrp + ".commands"))
            );

            commands.add(command);
        }

        return commands;
    }

    private static Optional<Integer> getOptionalIntegerFromConfig(String path) {
        Optional<Integer> optional = Optional.empty();
        if (config.contains(path))
            optional = Optional.of(config.getInt(path));

        return optional;
    }
}
