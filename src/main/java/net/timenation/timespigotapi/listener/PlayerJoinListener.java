package net.timenation.timespigotapi.listener;

import net.timenation.timespigotapi.TimeSpigotAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);

        TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player);
        TimeSpigotAPI.getInstance().getTablistManager().setPlayersPrefix(player);
    }
}