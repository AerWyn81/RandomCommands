package fr.aerwyn81.randomcommands.runnables;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.services.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class GlobalTask extends BukkitRunnable {

    @Override
    public void run() {
        var plugin = RandomCommands.getInstance();

        for (Command command : ConfigService.getCommands()) {

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

            Bukkit.getScheduler().runTaskLater(plugin, () -> command.commands().forEach(cmd ->
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd)), 1L);

            if (optInterval.isPresent()) {
                var nextAvailableTrigger = LocalDateTime.now().plusSeconds(optInterval.get());
                command.setNextAvailableTrigger(nextAvailableTrigger);
            }
        }
    }
}