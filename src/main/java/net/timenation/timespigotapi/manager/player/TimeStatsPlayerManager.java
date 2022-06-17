package net.timenation.timespigotapi.manager.player;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.player.TimeStatsPlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TimeStatsPlayerManager {

    private final Map<Player, TimeStatsPlayer> playerCache;

    public TimeStatsPlayerManager() {
        this.playerCache = new HashMap<>();
    }

    public TimeStatsPlayer getTimeStatsPlayer(Player player, String game) {
        if (playerCache.containsKey(player)) {
            return playerCache.get(player);
        }

        ResultSet resultSet = TimeSpigotAPI.getInstance().getMySQL().getDatabaseResult("SELECT * FROM playerStats WHERE playerUuid='" + player.getUniqueId() + "' AND game='" + game + "'");

        try {
            if (resultSet.next()) {
                TimeStatsPlayer timeStatsPlayer = new TimeStatsPlayer();

                timeStatsPlayer.setPlayerUuid(player.getUniqueId().toString());
                timeStatsPlayer.setPlayerName(TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player).getPlayerName());
                timeStatsPlayer.setGame(game);
                timeStatsPlayer.setWins(resultSet.getInt("wins"));
                timeStatsPlayer.setLooses(resultSet.getInt("looses"));
                timeStatsPlayer.setGames(resultSet.getInt("games"));
                timeStatsPlayer.setKills(resultSet.getInt("kills"));
                timeStatsPlayer.setDeaths(resultSet.getInt("deaths"));

                playerCache.put(player, timeStatsPlayer);
                return timeStatsPlayer;
            }

            TimeStatsPlayer timeStatsPlayer = new TimeStatsPlayer();
            timeStatsPlayer.setPlayerUuid(player.getUniqueId().toString());
            timeStatsPlayer.setPlayerName(TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player).getPlayerName());
            timeStatsPlayer.setGame(game);
            timeStatsPlayer.setWins(0);
            timeStatsPlayer.setLooses(0);
            timeStatsPlayer.setGames(0);
            timeStatsPlayer.setKills(0);
            timeStatsPlayer.setDeaths(0);
            TimeSpigotAPI.getInstance().getMySQL().updateDatabase("INSERT INTO playerStats (playerUuid, playerName, game, wins, looses, games, kills, deaths) VALUES" +
                    " ('" + player.getUniqueId() + "', '" + player.getName() + "', '" + game + "', '0', '0', '0', '0', '0')");

            playerCache.put(player, timeStatsPlayer);
            return timeStatsPlayer;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void updateTimeStatsPlayer(TimeStatsPlayer player) {
        TimeSpigotAPI.getInstance().getMySQL().updateDatabase("UPDATE playerStats SET wins='" + player.getWins() + "', looses= '" + player.getLooses() + "', games= '" + player.getGames() + "', kills= '" + player.getKills() + "', deaths= '" + player.getDeaths() + "', playerName='" + player.getPlayerName() + "' WHERE playerUuid= '" + player.getPlayerUuid() + "' AND game='" + player.getGame() + "'");
    }
}
