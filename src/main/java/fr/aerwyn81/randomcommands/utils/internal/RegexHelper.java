package fr.aerwyn81.randomcommands.utils.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    private static final Pattern pattern = Pattern.compile("\\d+");

    public static int extractIntFromDelay(String str) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String numberStr = matcher.group();
            return Integer.parseInt(numberStr);
        } else {
            return 0;
        }
    }
}
