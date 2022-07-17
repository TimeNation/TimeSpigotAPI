package net.timenation.timespigotapi;

import com.google.gson.Gson;
import lombok.Getter;
import net.timenation.timespigotapi.config.MySQLConfigObject;
import net.timenation.timespigotapi.config.TimeConfig;
import net.timenation.timespigotapi.data.Components;
import net.timenation.timespigotapi.data.Logger;
import net.timenation.timespigotapi.data.UUIDFetcher;
import net.timenation.timespigotapi.listener.AsyncPlayerChatListener;
import net.timenation.timespigotapi.listener.PlayerJoinListener;
import net.timenation.timespigotapi.listener.PlayerQuitListener;
import net.timenation.timespigotapi.manager.*;
import net.timenation.timespigotapi.manager.backend.RequestManager;
import net.timenation.timespigotapi.manager.color.ColorAPI;
import net.timenation.timespigotapi.manager.player.TimePlayerManager;
import net.timenation.timespigotapi.manager.player.TimeStatsPlayerManager;
import net.timenation.timespigotapi.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

@Getter
public final class TimeSpigotAPI extends JavaPlugin {

    private static TimeSpigotAPI instance;
    private MySQL mySQL;
    private TimePlayerManager timePlayerManager;
    private TimeStatsPlayerManager timeStatsPlayerManager;
    private TimeGameStatsManager timeGameStatsManager;
    private RankManager rankManager;
    private ParticleManager particleManager;
    private ColorAPI colorAPI;
    private CloudManager cloudManager;
    private UUIDFetcher uuidFetcher;
    private RequestManager requestManager;
    private Logger timeLogger;
    private NickManager nickManager;
    private TablistManager tablistManager;
    private Components components;

    private TimeConfig timeConfig;


    @Override
    public void onEnable() {
        instance = this;


        timeConfig = TimeConfig.loadConfig(new File(getDataFolder() + "/config.json"));
        timeLogger = new Logger();
        mySQL = new MySQL("timenation");
        timePlayerManager = new TimePlayerManager();
        timeStatsPlayerManager = new TimeStatsPlayerManager();
        timeGameStatsManager = new TimeGameStatsManager();
        particleManager = new ParticleManager();
        requestManager = new RequestManager();
        //tablistManager = new TablistManager();
        cloudManager = new CloudManager();
        nickManager = new NickManager();
        rankManager = new RankManager();
        colorAPI = new ColorAPI();
        uuidFetcher = new UUIDFetcher();
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
