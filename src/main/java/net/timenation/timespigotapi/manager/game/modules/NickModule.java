package net.timenation.timespigotapi.manager.game.modules;

import eu.thesimplecloud.api.CloudAPI;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.game.TimeGame;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NickModule implements Listener {

    private final TimeGame timeGame;

    public NickModule(TimeGame timeGame) {
        this.timeGame = timeGame;

        Bukkit.getPluginManager().registerEvents(this, timeGame);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(event.getPlayer()).isNickTool()) {
            if (!CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).isLobby()) {
                TimeSpigotAPI.getInstance().getNickManager().nickPlayer(event.getPlayer());
            }
        }
    }
}
