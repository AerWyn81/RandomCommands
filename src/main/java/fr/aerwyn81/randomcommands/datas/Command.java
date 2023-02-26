package fr.aerwyn81.randomcommands.datas;

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
        return "Command" +
                "id='" + id + '\'' +
                ", requirements=" + requirements +
                ", commands=" + commands +
                ", nextAvailableTrigger=" + nextAvailableTrigger +
                '}';
    }
}