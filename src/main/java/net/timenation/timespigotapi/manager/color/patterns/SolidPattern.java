package net.timenation.timespigotapi.manager.color.patterns;

import net.timenation.timespigotapi.TimeSpigotAPI;

import java.util.regex.Matcher;

public class SolidPattern implements Pattern {

    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<SOLID:([0-9A-Fa-f]{6})>|#\\{([0-9A-Fa-f]{6})}");

    public String process(String string) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String color = matcher.group(1);
            if (color == null) color = matcher.group(2);

            string = string.replace(matcher.group(), TimeSpigotAPI.getInstance().getColorAPI().getColor(color) + "");
        }
        return string;
    }

}
