package fr.aerwyn81.randomcommands.datas;

public enum EnumRunError {
    OK("Ok"),
    MIN_PLAYER("MinPlayer"),
    INTERVAL("Interval"),
    CHANCE("Chance"),
    NOT_WHILE("NotWhile");

    public final String value;

    EnumRunError(String value) {
        this.value = value;
    }
}
