package fr.aerwyn81.randomcommands.utils.bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static boolean hasPermission(CommandSender sender, String permission) {
        if (!(sender instanceof Player)) {
            return true;
        }

        return sender.hasPermission(permission) || sender.isOp();
    }
}
