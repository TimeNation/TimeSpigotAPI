package net.timenation.timespigotapi.manager.game.phase;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public abstract class LobbyPhase<timeGame extends TimeGame> implements Listener {

    public final TimeGame timeGame;
    private final String gameName;

    public LobbyPhase(TimeGame timeGame, String gameName) {
        this.timeGame = timeGame;
        this.gameName = gameName;
    }
    
    /*
    If game is not a Kit game, let this methode empty
     */
    public abstract void setKitStuff(Player player);
    /*
    If game is not a Kit game, let this methode empty
     */
    public abstract void setDefaultKit(Player player);
    public abstract void startCountdown();
    public abstract void updateScoreboard(Player player);
    
    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, gameName);

        if(timeGame.getGameState().equals(GameState.LOBBY) || timeGame.getGameState().equals(GameState.STARTING)) {
            player.teleport(new Location(Bukkit.getWorld("world"), 111.5, 114.00, -262.5, -45, 0));
            setKitStuff(player);

            if(Bukkit.getOnlinePlayers().size() == timeGame.getNeededPlayers()) startCountdown();

            Bukkit.getOnlinePlayers().forEach(current -> {
                current.sendMessage(I18n.format(current, timeGame.getPrefix(), "game.messages.join", TimeSpigotAPI.getInstance().getRankManager().getPlayersRank(player.getUniqueId()).getPlayersRankAndName(player.getUniqueId()), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()));
                updateScoreboard(player);
            });

            return;
        }

        //timeGame.getPlayers().remove(player);
        timeGame.getSpecatePlayers().add(player);
        setDefaultKit(player);
        Bukkit.getOnlinePlayers().forEach(current -> {
            current.hidePlayer(timeGame, player);
        });
        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemManager(Material.COMPASS, 1).setDisplayName(I18n.format(player, "game.item.teleporter")).build());
        timeGame.defaultGameQuitItem.setItem(player);
        TimeSpigotAPI.getInstance().getTablistManager().registerRankTeam(player, "§c✗ §8» ", "", ChatColor.GRAY, 18);
        player.teleport(new Location(Bukkit.getWorld("world"), 111.5, 114.00, -262.5, -45, 0));
        player.setAllowFlight(true);
    }
}
