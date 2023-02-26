package fr.aerwyn81.randomcommands.datas;

import fr.aerwyn81.randomcommands.utils.message.MessageUtils;

import java.util.Optional;

public record Requirements(Optional<Integer> minPlayers, Optional<Integer> interval, Optional<Integer> chance) {

    public String display() {
        return MessageUtils.colorize("&7Requirements:" +
                (minPlayers.map(v -> "\n" + " &fMin players: &7" + v).orElse("")) +
                (interval.map(v -> "\n" + " &fInterval: &7" + v).orElse("")) +
                (chance.map(v -> "\n" + " &fChance: &7" + v).orElse("")));
    }
}