package net.timenation.timespigotapi.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.thesimplecloud.module.permission.PermissionPool;
import eu.thesimplecloud.module.permission.player.IPermissionPlayer;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.color.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;


@SuppressWarnings("ALL")
// Recode this class
public class TablistManager {

    private static TablistManager instance;
    private final File[] rankFiles;
    private final Gson gson;
    private final HashMap<Player, String> rankTeams;

    public TablistManager() {
        this.rankTeams = new HashMap<>();
        this.rankFiles = new File("ranks").listFiles();
        this.gson = new Gson();
    }

    public void registerRankTeam(Player player, String prefix, String suffix, ChatColor color, int level) {
        String teamName = level + player.getUniqueId().toString().subSequence(0, 6).toString();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(teamName);

        if(team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        team.setPrefix(prefix);
        team.setSuffix(suffix);
        team.setColor(color);

        team.addEntry(player.getName());
    }

    public void setPlayersPrefix(Player player) {
        IPermissionPlayer iPermissionPlayer = PermissionPool.getInstance().getPermissionPlayerManager().getCachedPermissionPlayer(player.getUniqueId());
        for (File file : this.rankFiles) {
            if (file.isDirectory()) continue;
            if(!file.getName().endsWith(".json")) continue;

            if(file.getName().equalsIgnoreCase(iPermissionPlayer.getHighestPermissionGroup().getName() + ".json")) {
                try {
                    JsonObject jsonObject = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
                    registerRankTeam(player, TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("rankPrefix").getAsString()), "", ChatColor.valueOf(jsonObject.get("rankChatColor").getAsString()), jsonObject.get("rankPropety").getAsInt());
                    if(jsonObject.get("hex").getAsBoolean()) {
                        player.setPlayerListName(TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("tabPrefix").getAsString()) + TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("hexStart").getAsString() + player.getName() + jsonObject.get("hexEnd").getAsString()));
                        player.setDisplayName(TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("tabPrefix").getAsString()) + TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("hexStart").getAsString() + player.getName() + jsonObject.get("hexEnd").getAsString()));
                        player.setCustomName(TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("tabPrefix").getAsString()) + TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("hexStart").getAsString() + player.getName() + jsonObject.get("hexEnd").getAsString()));
                        player.setCustomNameVisible(true);
                    } else {
                        player.setPlayerListName(TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("tabPrefix").getAsString()) + player.getName());
                        player.setDisplayName(TimeSpigotAPI.getInstance().getColorAPI().process(jsonObject.get("tabPrefix").getAsString()) + player.getName());
                    }
                } catch (FileNotFoundException ignored) { }
            }
        }

        if(TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player).isNicked()) {
            registerRankTeam(player, "", "", ChatColor.GRAY, 24);
            return;
        }
    }
}