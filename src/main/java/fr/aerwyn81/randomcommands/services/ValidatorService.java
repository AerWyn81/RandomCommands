package fr.aerwyn81.randomcommands.services;

import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.util.SplittableRandom;

public class ValidatorService {

    public static boolean hasEnoughPlayer(Integer required) {
        return Bukkit.getOnlinePlayers().stream().filter(p -> !p.isOp()).count() >= required;
    }

    public static boolean isIntervalValid(LocalDateTime nextAvailableTrigger) {
        return LocalDateTime.now().isAfter(nextAvailableTrigger);
    }

    public static boolean isLucky(Integer number) {
        if (number < 0)
            return false;

        if (number > 100)
            return true;

        return new SplittableRandom().nextInt(1, 101) <= number;
    }


}
