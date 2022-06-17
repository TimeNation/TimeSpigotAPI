package net.timenation.timespigotapi.manager.language;

import net.timenation.timespigotapi.TimeSpigotAPI;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class I18n {

    private static final ConfigManager languageDE = new ConfigManager("de");
    private static final ConfigManager languageEN = new ConfigManager("en");

    public static String format(Player player, String translateKey, Object... arguments) {
        try {
            translateKey = TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player).getLanguage().equals("de") ? languageDE.getString(translateKey) : languageEN.getString(translateKey);
            MessageFormat messageFormat = new MessageFormat(translateKey);
            return TimeSpigotAPI.getInstance().getColorAPI().process(messageFormat.format(arguments));
        } catch (NullPointerException ignored) { }
        return translateKey;
    }

    public static List<String> formatLines(Player player, String translateKey, Object... arguments) {
        try {
            translateKey = TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player).getLanguage().equals("de") ? languageDE.getString(translateKey) : languageEN.getString(translateKey);
            return TimeSpigotAPI.getInstance().getColorAPI().process(formatLines(translateKey, arguments));
        } catch (NullPointerException ignored) { }
        return Collections.singletonList(translateKey);
    }

    public static List<String> formatLines(String translateKey, Object[] arguments) {
        return formatMessage(translateKey, arguments).lines().collect(Collectors.toList());
    }

    public static String formatMessage(String translateKey, Object... arguments) {
        try {
            MessageFormat messageFormat = new MessageFormat(translateKey);
            return TimeSpigotAPI.getInstance().getColorAPI().process(messageFormat.format(arguments));
        } catch (NullPointerException ignored) { }
        return translateKey;
    }

    public static String format(Player player, String prefix, String translateKey, Object... arguments) {
        try {
            translateKey = prefix + (TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player).getLanguage().equals("de") ? languageDE.getString(translateKey) : languageEN.getString(translateKey));
            MessageFormat messageFormat = new MessageFormat(translateKey);
            return TimeSpigotAPI.getInstance().getColorAPI().process(messageFormat.format(arguments));
        } catch (NullPointerException ignored) { }
        return translateKey;
    }

    public static String format(Player player, String translateKey, String prefix) {
        try {
            translateKey = prefix + (TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player).getLanguage().equals("de") ? languageDE.getString(translateKey) : languageEN.getString(translateKey));
            return TimeSpigotAPI.getInstance().getColorAPI().process(translateKey);
        } catch (NullPointerException ignored) { }
        return translateKey;
    }
}
