package fr.aerwyn81.randomcommands.runnables;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.services.ValidatorService;
import fr.aerwyn81.randomcommands.utils.internal.RunningCommandsHelper;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class GlobalTask extends BukkitRunnable {

    @Override
    public void run() {
        var runningCommands = RandomCommands.runningCommands;
        var commands = ConfigService.getCommands();

        for (Command command : commands) {

            // Check to clean old available trigger time
            if (command.getNextAvailableTrigger().isPresent() && command.getNextAvailableTrigger().get().isBefore(LocalDateTime.now())) {
                command.setNextAvailableTrigger(null);
                command.setStartTriggerTime(null);
            }
            //

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
            if (optNotWhile.isPresent()) {
                var existingNotWhileCommands = optNotWhile.get().stream()
                        .map(RunningCommandsHelper::getCommandById)
                        .flatMap(Optional::stream).collect(Collectors.toCollection(ArrayList::new));

                if (runningCommands.disjoint(existingNotWhileCommands)) {
                    continue;
                }
            }

            RunningCommandsHelper.runCommand(command);

            if (optInterval.isPresent()) {
                command.setStartTriggerTime(LocalDateTime.now());
                command.setNextAvailableTrigger(command.getStartTriggerTime().plusSeconds(optInterval.get()));
                runningCommands.add(command, optInterval.get() * 1000L);
            } else {
                runningCommands.add(command, Long.MAX_VALUE);
            }
        }
    }
}