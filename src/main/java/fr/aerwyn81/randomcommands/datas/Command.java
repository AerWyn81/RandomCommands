package fr.aerwyn81.randomcommands.datas;

import fr.aerwyn81.randomcommands.utils.message.MessageUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public final class Command {
    private final String id;
    private final Requirements requirements;
    private final ArrayList<String> commands;
    private final ArrayList<String> commandsOnReset;

    private LocalDateTime startTriggerTime;
    private LocalDateTime nextAvailableTrigger;

    public Command(String id, Requirements requirements, ArrayList<String> commands, ArrayList<String> commandsOnReset) {
        this.id = id;
        this.requirements = requirements;
        this.commands = commands;
        this.commandsOnReset = commandsOnReset;
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

    public ArrayList<String> commandsOnReset() {
        return commandsOnReset;
    }

    public LocalDateTime getStartTriggerTime() {
        return startTriggerTime;
    }

    public void setStartTriggerTime(LocalDateTime startTriggerTime) {
        this.startTriggerTime = startTriggerTime;
    }

    public Optional<LocalDateTime> getNextAvailableTrigger() {
        return Optional.ofNullable(nextAvailableTrigger);
    }

    public void setNextAvailableTrigger(LocalDateTime lastTrigger) {
        this.nextAvailableTrigger = lastTrigger;
    }

    private final DateTimeFormatter SIMPLE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String display() {
        return MessageUtils.colorize("&7Command:" +
                ("\n &fNext Available Trigger: &7" + (nextAvailableTrigger != null ? nextAvailableTrigger.format(SIMPLE_FORMAT) : "Not running")) +
                ("\n" + requirements.display()));
    }
}