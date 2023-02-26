package fr.aerwyn81.randomcommands.commands;

public class RCCommand {
    private final Cmd cmdClass;
    private final String command;
    private final String permission;
    private final boolean isPlayerCommand;
    private final String[] args;
    private final boolean visible;

    public RCCommand(Cmd command) {
        this.cmdClass = command;
        this.command = cmdClass.getClass().getAnnotation(RCAnnotations.class).command();
        this.permission = cmdClass.getClass().getAnnotation(RCAnnotations.class).permission();
        this.isPlayerCommand = cmdClass.getClass().getAnnotation(RCAnnotations.class).isPlayerCommand();
        this.args = cmdClass.getClass().getAnnotation(RCAnnotations.class).args();
        this.visible = cmdClass.getClass().getAnnotation(RCAnnotations.class).isVisible();
    }

    public Cmd getCmdClass() {
        return cmdClass;
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isPlayerCommand() { return isPlayerCommand; }

    public String[] getArgs() { return args; }

    public boolean isVisible() { return visible; }
}
