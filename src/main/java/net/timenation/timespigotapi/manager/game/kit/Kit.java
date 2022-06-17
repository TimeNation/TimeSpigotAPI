package net.timenation.timespigotapi.manager.game.kit;

import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.entity.Player;

import java.util.List;

public class Kit {

    private final String kitName;
    private final String kitNameTranslateKey;
    private final String kitDescription;
    private final KitObject kitObject;

    Kit(String kitName, String kitNameTranslateKey, String kitDescription, KitObject kitObject) {
        this.kitName = kitName;
        this.kitNameTranslateKey = kitNameTranslateKey;
        this.kitDescription = kitDescription;
        this.kitObject = kitObject;
    }

    public String getKitName() {
        return kitName;
    }

    public KitObject getKitObject() {
        return kitObject;
    }

    public List<String> getKitDescription(Player player) {
        return I18n.formatLines(player, kitDescription);
    }

    public String getKitTranslateKey(Player player) {
        return I18n.format(player, kitNameTranslateKey);
    }
}
