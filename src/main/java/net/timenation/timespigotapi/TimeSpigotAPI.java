package net.timenation.timespigotapi;

import lombok.Getter;
import net.timenation.timespigotapi.data.Components;
import net.timenation.timespigotapi.data.Logger;
import net.timenation.timespigotapi.data.UUIDFetcher;
import net.timenation.timespigotapi.listener.AsyncPlayerChatListener;
import net.timenation.timespigotapi.listener.PlayerJoinListener;
import net.timenation.timespigotapi.listener.PlayerQuitListener;
import net.timenation.timespigotapi.manager.*;
import net.timenation.timespigotapi.manager.backend.RequestManager;
import net.timenation.timespigotapi.manager.bot.BotListener;
import net.timenation.timespigotapi.manager.color.ColorAPI;
import net.timenation.timespigotapi.manager.player.TimePlayerManager;
import net.timenation.timespigotapi.manager.player.TimeStatsPlayerManager;
import net.timenation.timespigotapi.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

@Getter
public final class TimeSpigotAPI extends JavaPlugin {

    private static TimeSpigotAPI instance;
    private MySQL mySQL;
    private TimePlayerManager timePlayerManager;
    private TimeStatsPlayerManager timeStatsPlayerManager;
    private TimeGameStatsManager timeGameStatsManager;
    private RankManager rankManager;
    private ColorAPI colorAPI;
    private CloudManager cloudManager;
    private UUIDFetcher uuidFetcher;
    private RequestManager requestManager;
    private Logger timeLogger;
    private NickManager nickManager;
    private TablistManager tablistManager;
    private Components components;

    @Override
    public void onEnable() {
        instance = this;

        timeLogger = new Logger();
        mySQL = new MySQL("Storage");
        timePlayerManager = new TimePlayerManager();
        timeStatsPlayerManager = new TimeStatsPlayerManager();
        timeGameStatsManager = new TimeGameStatsManager();
        rankManager = new RankManager();
        colorAPI = new ColorAPI();
        cloudManager = new CloudManager();
        uuidFetcher = new UUIDFetcher();
        requestManager = new RequestManager();
        nickManager = new NickManager();
        tablistManager = new TablistManager();
        components = new Components();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new AsyncPlayerChatListener(), this);
    }

    @Override
    public void onDisable() {
        mySQL.disconnectFromDatabase();
    }

    public static TimeSpigotAPI getInstance() {
        return instance;
    }
}
