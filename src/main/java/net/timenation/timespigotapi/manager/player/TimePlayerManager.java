package net.timenation.timespigotapi.manager.player;

import com.google.gson.JsonObject;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.player.TimePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TimePlayerManager {

    private final Map<Player, TimePlayer> playerCache;

    public TimePlayerManager() {
        this.playerCache = new HashMap<>();
    }

    public TimePlayer getTimePlayer(Player player) {
        if (playerCache.containsKey(player)) {
            return playerCache.get(player);
        }

        ResultSet resultSet = TimeSpigotAPI.getInstance().getMySQL().getDatabaseResult("SELECT * FROM playerData WHERE playerUuid='" + player.getUniqueId() + "'");

        try {
            if (resultSet.next()) {
                TimePlayer timePlayer = new TimePlayer();

                JsonObject jsonObject = TimeSpigotAPI.getInstance().getRequestManager().getHttpResponse(player.getUniqueId());
                timePlayer.setPlayerUuid(player.getUniqueId());
                timePlayer.setPlayerName(player.getName());
                timePlayer.setLanguage(jsonObject.get("playerData").getAsJsonObject().get("language").getAsString());
                timePlayer.setCrystals(jsonObject.get("playerData").getAsJsonObject().get("crystals").getAsInt());
                timePlayer.setLootboxes(jsonObject.get("playerData").getAsJsonObject().get("lootboxes").getAsInt());
                timePlayer.setNickTool(false);

                playerCache.put(player, timePlayer);
                return timePlayer;
            }

            TimePlayer timePlayer = new TimePlayer();
            timePlayer.setPlayerUuid(player.getUniqueId());
            timePlayer.setPlayerName(player.getName());
            timePlayer.setLanguage("de");
            timePlayer.setCrystals(100);
            timePlayer.setLootboxes(0);
            timePlayer.setNickTool(false);
            TimeSpigotAPI.getInstance().getRequestManager().sendHttpRequestPost(player.getName(), player.getUniqueId(), player.getAddress().getAddress().toString());

            playerCache.put(player, timePlayer);
            return timePlayer;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void updateTimePlayer(TimePlayer timePlayer) {
        TimeSpigotAPI.getInstance().getRequestManager().sendHttpRequestPut(timePlayer.getPlayerUuid(), timePlayer.getPlayerName(), timePlayer.getCrystals(), timePlayer.getLootboxes(), "null", "null", timePlayer.getLanguage());
    }
}