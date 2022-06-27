package net.timenation.timespigotapi.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.thesimplecloud.clientserverapi.lib.promise.ICommunicationPromise;
import eu.thesimplecloud.module.permission.PermissionPool;
import eu.thesimplecloud.module.permission.player.IPermissionPlayer;
import lombok.SneakyThrows;
import net.timenation.timespigotapi.TimeSpigotAPI;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.util.UUID;

public class RankManager {

    private final File[] files;
    private final Gson gson;

    public RankManager() {
        this.files = new File("ranks").listFiles();
        this.gson = new Gson();
    }

    @SneakyThrows
    public Rank getPlayersRank(UUID uuid) {
        ICommunicationPromise<IPermissionPlayer> iPermissionPlayer = PermissionPool.getInstance().getPermissionPlayerManager().getPermissionPlayer(uuid);

        for (File file : this.files) {
            if (file.isDirectory()) continue;
            if (!file.getName().endsWith(".json")) continue;

            if (Bukkit.getPlayer(uuid) != null) {
                if (TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(Bukkit.getPlayer(uuid)).isNicked() && file.getName().equals("default.json")) {
                    JsonObject jsonObject = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
                    return gson.fromJson(jsonObject, Rank.class);
                }
            }

            if (file.getName().equalsIgnoreCase(iPermissionPlayer.getBlockingOrNull().getHighestPermissionGroup().getName() + ".json")) {
                JsonObject jsonObject = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
                return gson.fromJson(jsonObject, Rank.class);
            }
        }

        return null;
    }

    public static class Rank {
        private String rankName;
        private String rankPrefix;
        private String tabPrefix;
        private String rankColor;
        private String hexStart;
        private String hexEnd;

        public Rank() {
        }

        public String getRankName() {
            return TimeSpigotAPI.getInstance().getColorAPI().process(tabPrefix.replace(" §8| ", ""));
        }

        public String getRankPrefix() {
            return TimeSpigotAPI.getInstance().getColorAPI().process(tabPrefix);
        }

        public String getRankScoreName() {
            return TimeSpigotAPI.getInstance().getColorAPI().process(hexStart + rankName + hexEnd);
        }

        public String getRankColor() {
            return TimeSpigotAPI.getInstance().getColorAPI().process(rankColor);
        }

        public String getHexStart() {
            return hexStart;
        }

        public String getHexEnd() {
            return hexEnd;
        }

        @SneakyThrows
        public String getPlayersRankAndName(UUID uuid) {
            return TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(Bukkit.getPlayer(uuid)).isNicked() ? new JsonParser().parse(new FileReader(new File("ranks/default.json"))).getAsJsonObject().get("rankPrefix").getAsString() + TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(Bukkit.getPlayer(uuid)).getPlayerNickName() : rankPrefix + TimeSpigotAPI.getInstance().getUuidFetcher().getName(uuid);
        }

        public String getPlayersNameWithRankColor(UUID uuid) {
            if (Bukkit.getPlayer(uuid) != null)
                return TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(Bukkit.getPlayer(uuid)).isNicked() ? rankColor + TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(Bukkit.getPlayer(uuid)).getPlayerNickName() : rankColor + TimeSpigotAPI.getInstance().getUuidFetcher().getName(uuid);
            return "§cERROR";
        }
    }
}