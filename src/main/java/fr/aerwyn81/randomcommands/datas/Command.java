package fr.aerwyn81.randomcommands.datas;

import fr.aerwyn81.randomcommands.utils.message.MessageUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public final class Command {
    private final String id;
    private final Requirements requirements;
    private final ArrayList<String> commands;

    private LocalDateTime nextAvailableTrigger;

    public Command(String id, Requirements requirements, ArrayList<String> commands) {
        this.id = id;
        this.requirements = requirements;
        this.commands = commands;
    }

    public String id() {
        return id;
    }

    public Requirements requirements() {
        return requirements;
    }

    public ArrayList<String> commands() {
        return commands;
    }

    public Optional<LocalDateTime> getNextAvailableTrigger() {
        return Optional.ofNullable(nextAvailableTrigger);
    }

    public void setNextAvailableTrigger(LocalDateTime lastTrigger) {
        this.nextAvailableTrigger = lastTrigger;
    }

    public String display() {
        return MessageUtils.colorize("&7Command:" +
                ("\n" + " &fRequirements: &7" + "\n" + requirements.display()) +
                ("\n" + " &fCommands: &7" + String.join(" \n", commands)) +
                ("\n" + " &fNext Available Trigger: &7" + nextAvailableTrigger));
    }
}