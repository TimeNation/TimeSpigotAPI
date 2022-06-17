package net.timenation.timespigotapi.manager.game.team;

import net.timenation.timespigotapi.manager.game.TimeGame;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private final String teamName;
    private final String teamPrefix;
    private final ChatColor teamColor;
    private final Color teamBaseColor;
    private final int playerSize;
    private boolean isAlive;
    private final TimeGame game;
    private final List<Player> players;

    public Team(String teamName, String teamPrefix, ChatColor teamColor, Color teamBaseColor, int playerSize, boolean isAlive, TimeGame game) {
        this.teamName = teamName;
        this.teamPrefix = teamPrefix;
        this.teamColor = teamColor;
        this.teamBaseColor = teamBaseColor;
        this.playerSize = playerSize;
        this.isAlive = true;
        this.game = game;
        this.players = new ArrayList<>();

        game.teamManager.getRegistedTeams().add(this);
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamPrefix() {
        return teamPrefix;
    }

    public ChatColor getTeamColor() {
        return teamColor;
    }

    public Color getTeamBaseColor() {
        return teamBaseColor;
    }

    public int getPlayerSize() {
        return playerSize;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public TimeGame getGame() {
        return game;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
