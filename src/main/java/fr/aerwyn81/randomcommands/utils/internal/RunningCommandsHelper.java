package fr.aerwyn81.randomcommands.utils.internal;

import fr.aerwyn81.randomcommands.RandomCommands;
import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.datas.EnumRunError;
import fr.aerwyn81.randomcommands.services.ValidatorService;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RunningCommandsHelper {

    public static Optional<Command> getCommandById(String id) {
        return StreamSupport.stream(RandomCommands.runningCommands.spliterator(), false).filter(s -> Objects.equals(s.id(), id)).findFirst();
    }

    public static EnumRunError runCommand(Command command, boolean checkRequirements) {
        var plugin = RandomCommands.getInstance();
        var runningCommands = RandomCommands.runningCommands;

        if (checkRequirements) {
            var optMinPlayers = command.requirements().minPlayers();
            if (optMinPlayers.isPresent() && !ValidatorService.hasEnoughPlayer(optMinPlayers.get())) {
                return EnumRunError.MIN_PLAYER;
            }

            var optInterval = command.requirements().interval();
            if (optInterval.isPresent() && command.getNextAvailableTrigger().isPresent() &&
                    !ValidatorService.isIntervalValid(command.getNextAvailableTrigger().get())) {
                return EnumRunError.INTERVAL;
            }

            var optChance = command.requirements().chance();
            if (optChance.isPresent() && !ValidatorService.isLucky(optChance.get())) {
                return EnumRunError.CHANCE;
            }

            var optNotWhile = command.requirements().notWhile();
            if (optNotWhile.isPresent()) {
                var existingNotWhileCommands = optNotWhile.get().stream()
                        .map(RunningCommandsHelper::getCommandById)
                        .flatMap(Optional::stream).collect(Collectors.toCollection(ArrayList::new));

                if (runningCommands.disjoint(existingNotWhileCommands)) {
                    return EnumRunError.NOT_WHILE;
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> command.commands().forEach(cmd ->
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd)), 1L);

        var optInterval = command.requirements().interval();
        if (optInterval.isPresent()) {
            command.setStartTriggerTime(LocalDateTime.now());
            command.setNextAvailableTrigger(command.getStartTriggerTime().plusSeconds(optInterval.get()));
            runningCommands.add(command, optInterval.get() * 1000L);
        } else {
            command.setStartTriggerTime(LocalDateTime.now());
            command.setNextAvailableTrigger(LocalDateTime.MAX);
            runningCommands.add(command, Long.MAX_VALUE);
        }

        return EnumRunError.OK;
    }
}
