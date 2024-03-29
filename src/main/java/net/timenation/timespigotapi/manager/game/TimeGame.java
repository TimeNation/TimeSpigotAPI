package net.timenation.timespigotapi.manager.game;

import com.google.gson.JsonParser;
import eu.thesimplecloud.api.CloudAPI;
import lombok.Getter;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.game.defaultitems.DefaultGameQuitItem;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.game.manager.ConfigManager;
import net.timenation.timespigotapi.manager.game.scoreboard.ScoreboardManager;
import net.timenation.timespigotapi.manager.game.team.TeamManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class TimeGame extends JavaPlugin {
    public GameState gameState;
    public ArrayList<Player> specatePlayer = new ArrayList<>();
    public ArrayList<Player> players = new ArrayList<>();
    public HashMap<Player, String> playerKit = new HashMap<>();
    public ConfigManager configManager = new ConfigManager(getGameName(), "");
    public TeamManager teamManager = new TeamManager();
    public DefaultGameQuitItem defaultGameQuitItem;
    private String gameMap;
    private String builder;
    private World world;
    private String prefix;
    private String color;
    private String secoundColor;
    private int countdown;
    private boolean gameWithKits;
    private ScoreboardManager scoreboardManager;

    public abstract ArrayList<Player> getPlayers();
    public abstract ArrayList<Player> getSpecatePlayers();
    public abstract GameState getGameState();
    public abstract int getNeededPlayers();
    public abstract ConfigManager getConfigManager();
    public abstract TeamManager getTeamManager();
    public abstract DefaultGameQuitItem getDefaultGameQuitItem();
    public String getGameMap() {
        return gameMap;
    }
    public String getBuilder() {
        return builder;
    }
    public World getWorld() {
        return world;
    }
    public String getPrefix() {
        return TimeSpigotAPI.getInstance().getColorAPI().process(prefix + secoundColor + " » §7");
    }
    public String getColor() {
        return TimeSpigotAPI.getInstance().getColorAPI().process(color);
    }
    public String getSecoundColor() {
        return TimeSpigotAPI.getInstance().getColorAPI().process(secoundColor);
    }
    public String getGameName() {return prefix;}
    public HashMap<Player, String> getPlayerKit() {
        return playerKit;
    }
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    public int getCountdown() {
        return countdown;
    }
    public boolean isGameWithKits() {
        return gameWithKits;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setSecoundColor(String secoundColor) {
        this.secoundColor = secoundColor;
    }
    public void setScoreboardManager(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }
    public void setDefaultGameQuitItem(DefaultGameQuitItem defaultGameQuitItem) {
        this.defaultGameQuitItem = defaultGameQuitItem;
    }
    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }
    public void setGameWithKits(boolean gameWithKits) {
        this.gameWithKits = gameWithKits;
    }
    public void setGameMap(String gameMap, String builder, World world) {
        this.gameMap = getColor() + gameMap;
        this.builder = getColor() + builder;
        this.world = world;
        CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).setMOTD(gameMap);
    }
    public void setRandomGameMap(String gameName) {
        int i = ThreadLocalRandom.current().nextInt(0, Arrays.stream(new File("plugins/" + gameName + "/maps").listFiles()).toList().size());
        List<File> maps = Arrays.stream(new File("plugins/" + gameName + "/maps").listFiles()).toList();
        configManager = new ConfigManager(gameName, maps.get(i).getName());
        setGameMap(configManager.getString("mapName"), configManager.getString("mapBuilder"), Bukkit.getWorld(configManager.getString("mapWorld")));
    }

    public void loadMaps(String gameName) {
        for (File file : new File("plugins/" + gameName + "/maps").listFiles()) {
            try {
                Bukkit.createWorld(new WorldCreator(new JsonParser().parse(new FileReader(file)).getAsJsonObject().get("mapWorld").getAsString()));
            } catch (FileNotFoundException ignored) {}
        }

        setRandomGameMap(gameName);

        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.EASY);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            world.setTime(6000);
            world.setStorm(false);
        }
    }

    public GameState setGameState(GameState gameState) {
        return this.gameState = gameState;
    }
}
