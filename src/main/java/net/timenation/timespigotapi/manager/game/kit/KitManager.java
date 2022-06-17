package net.timenation.timespigotapi.manager.game.kit;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KitManager {
    private final List<Kit> registedKits = new ArrayList<>();
    private final Map<Player, Kit> playerKitTypeMap = Maps.newHashMap();

    public Kit getKitFromPlayer(Player player) {
        return getPlayerKitTypeMap().get(player);
    }

    public void useKit(Player player, Kit kit) {
        playerKitTypeMap.put(player, kit);
    }

    public boolean isUsingKit(Player player, KitManager kitManager) {
        return getPlayerKitTypeMap().get(player).equals(kitManager);
    }

    public List<Kit> getRegistedKits() {
        return registedKits;
    }

    public Kit getRegistedKit(String kitName) {
        for (Kit registedKit : registedKits) {
            if(registedKit.getKitName().equals(kitName)) return registedKit;
        }
        return null;
    }

    public Map<Player, Kit> getPlayerKitTypeMap() {
        return playerKitTypeMap;
    }
}
