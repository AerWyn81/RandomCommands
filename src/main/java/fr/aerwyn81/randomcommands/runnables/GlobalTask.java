package fr.aerwyn81.randomcommands.runnables;

import fr.aerwyn81.randomcommands.datas.Command;
import fr.aerwyn81.randomcommands.services.ConfigService;
import fr.aerwyn81.randomcommands.utils.internal.RunningCommandsHelper;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class GlobalTask extends BukkitRunnable {

    @Override
    public void run() {
        var commands = ConfigService.getCommands();

        for (Command command : commands) {

            // Check to clean old available trigger time
            if (command.getNextAvailableTrigger().isPresent() && command.getNextAvailableTrigger().get().isBefore(LocalDateTime.now())) {
                command.setNextAvailableTrigger(null);
                command.setStartTriggerTime(null);
            }
            // Check to clean old available trigger time

            RunningCommandsHelper.runCommand(command, true);
        }
    }
}