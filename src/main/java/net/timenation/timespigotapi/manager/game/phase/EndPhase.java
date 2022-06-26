package net.timenation.timespigotapi.manager.game.phase;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ThreadLocalRandom;

public abstract class EndPhase<timeGame extends TimeGame> implements Listener {

    private TimeGame timeGame;
    
    public EndPhase(TimeGame timeGame) {
        this.timeGame = timeGame;
    }

    public abstract void stopCountdown();
    public abstract void startEndCountdown();
    
    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().updateTimeStatsPlayer(TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, "SkyWars"));

        if(timeGame.getGameState().equals(GameState.LOBBY) || timeGame.getGameState().equals(GameState.STARTING)) {
            Bukkit.getOnlinePlayers().forEach(current -> {
                current.sendMessage(I18n.format(player, timeGame.getPrefix(), "game.messages.quit", TimeSpigotAPI.getInstance().getRankManager().getPlayersRank(player.getUniqueId()).getPlayersRankAndName(player.getUniqueId()), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()));
            });

            if(Bukkit.getOnlinePlayers().size() - 1 < timeGame.getNeededPlayers()) stopCountdown();
        } else if(timeGame.getGameState() == GameState.ENDING) {
            if(Bukkit.getOnlinePlayers().size() <= 1) {
                Bukkit.shutdown();
            }
        } else {
            timeGame.getPlayers().remove(player);
            TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, "SkyWars").setDeaths(TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, "SkyWars").getDeaths() + 1);
            TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, "SkyWars").setLooses(TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, "SkyWars").getLooses() + 1);
            if(Bukkit.getOnlinePlayers().size() - 1 == 1 || timeGame.getPlayers().size() == 1) {
                Bukkit.getOnlinePlayers().forEach(current -> {
                    int crystals = ThreadLocalRandom.current().nextInt(25, 50);
                    current.teleport(new Location(Bukkit.getWorld("world"), 111.5, 114.00, -262.5, -45, 0));
                    current.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 0);
                    current.getInventory().clear();
                    current.setGameMode(GameMode.SURVIVAL);
                    current.sendTitle(I18n.format(current, "game.title.loose.top"), I18n.format(current, "game.title.loose.bottom"));
                    current.sendMessage(I18n.format(player, "game.messages.playerlostgame", crystals));
                    timeGame.defaultGameQuitItem.setItem(current);
                    TimeSpigotAPI.getInstance().getTimePlayerManager().updateTimePlayer(TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(current));
                });

                for(Player winner : timeGame.getPlayers()) {
                    int crystals = ThreadLocalRandom.current().nextInt(50, 75);

                    winner.sendMessage(I18n.format(player, "game.actionbar.playerhaswongame", timeGame.getPrefix()));
                    winner.sendTitle(I18n.format(player, "game.title.win.top"), I18n.format(player, "game.title.win.bottom"));
                    winner.sendMessage(I18n.format(player, timeGame.getPrefix(), "game.messages.playerhaswongame", crystals));
                    TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(winner).setCrystals(TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(winner).getCrystals() + crystals);
                    TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, "SkyWars").setWins(TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, "SkyWars").getWins() + 1);
                }

                timeGame.setGameState(GameState.ENDING);
                startEndCountdown();
            }
        }
    }
}
