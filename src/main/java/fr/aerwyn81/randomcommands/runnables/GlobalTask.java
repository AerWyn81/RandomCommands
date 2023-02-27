package fr.aerwyn81.randomcommands.runnables;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.services.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class GlobalTask extends BukkitRunnable {

    private final ArrayList<String> runningCommands;

    public GlobalTask() {
        this.runningCommands = new ArrayList<>();
    }

    @Override
    public void run() {
        var plugin = RandomCommands.getInstance();
        var commands = ConfigService.getCommands();

        for (Command command : commands) {

            var optMinPlayers = command.requirements().minPlayers();
            if (optMinPlayers.isPresent() && !ValidatorService.hasEnoughPlayer(optMinPlayers.get())) {
                continue;
            }

            var optInterval = command.requirements().interval();
            if (optInterval.isPresent() && command.getNextAvailableTrigger().isPresent() &&
                    !ValidatorService.isIntervalValid(command.getNextAvailableTrigger().get())) {
                continue;
            }

            var optChance = command.requirements().chance();
            if (optChance.isPresent() && !ValidatorService.isLucky(optChance.get())) {
                continue;
            }

            var optNotWhile = command.requirements().notWhile();
            if (optNotWhile.isPresent() && !Collections.disjoint(runningCommands, optNotWhile.get())) {
                continue;
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> command.commands().forEach(cmd ->
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd)), 1L);

            if (optInterval.isPresent()) {
                var nextAvailableTrigger = LocalDateTime.now().plusSeconds(optInterval.get());
                command.setNextAvailableTrigger(nextAvailableTrigger);
            }

            runningCommands.add(command.id());
        }
    }
}