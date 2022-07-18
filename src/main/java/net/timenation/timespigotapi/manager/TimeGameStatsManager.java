package net.timenation.timespigotapi.manager;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.game.TimeGameStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TimeGameStatsManager {

    private final Map<String, TimeGameStats> playerCache;

    public TimeGameStatsManager() {
        this.playerCache = new HashMap<>();
    }

    public TimeGameStats getTimeGameStats(String gameName) {
        if (playerCache.containsKey(gameName)) {
            return playerCache.get(gameName);
        }

        ResultSet resultSet = TimeSpigotAPI.getInstance().getMySQL().getDatabaseResult("SELECT * FROM gameStats WHERE game_name='" + gameName + "'");

        try {
            if (resultSet.next()) {
                TimeGameStats timeGameStats = new TimeGameStats();

                timeGameStats.setGameName(gameName);
                timeGameStats.setLastPatchLink(resultSet.getString("last_update_link"));
                timeGameStats.setUniquePlayers(resultSet.getInt("unique_players"));
                timeGameStats.setWeeklyPlayers(resultSet.getInt("weekly_players"));

                playerCache.put(gameName, timeGameStats);
                return timeGameStats;
            }

            TimeGameStats timeGameStats = new TimeGameStats();
            timeGameStats.setGameName(gameName);
            timeGameStats.setLastPatchLink("-/-");
            timeGameStats.setUniquePlayers(0);
            timeGameStats.setWeeklyPlayers(0);
            TimeSpigotAPI.getInstance().getMySQL().updateDatabase("INSERT INTO gameStats (game_name, last_update_link, unique_players, weekly_players) VALUES " +
                    "('" + gameName + "', '-/-', '0', '0')");

            playerCache.put(gameName, timeGameStats);
            return timeGameStats;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void updateTimeGameStatsForPlugin(String gameName) {
        ResultSet resultSet = TimeSpigotAPI.getInstance().getMySQL().getDatabaseResult("SELECT * FROM gameStats WHERE game_name='" + gameName + "'");

        try {
            if (resultSet.next()) {
                TimeGameStats timeGameStats = new TimeGameStats();

                timeGameStats.setGameName(gameName);
                timeGameStats.setLastPatchLink(resultSet.getString("last_update_link"));
                timeGameStats.setUniquePlayers(resultSet.getInt("unique_players"));
                timeGameStats.setWeeklyPlayers(resultSet.getInt("weekly_players"));

                playerCache.put(gameName, timeGameStats);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateTimeGameStats(TimeGameStats timeGameStats) {
        TimeSpigotAPI.getInstance().getMySQL().updateDatabase("UPDATE gameStats SET game_name='" + timeGameStats.getGameName() + "', last_update_link='" + timeGameStats.getLastPatchLink() + "', unique_players='" + timeGameStats.getUniquePlayers() + "', weekly_players='" + timeGameStats.getWeeklyPlayers() + "'");
    }
}
