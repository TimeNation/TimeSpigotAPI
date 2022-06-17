package net.timenation.timespigotapi.manager.game.team;

import net.timenation.timespigotapi.TimeSpigotAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamManager {

    private final List<Team> registedTeams;
    private final HashMap<Player, Team> playerTeamList;

    public TeamManager() {
        this.registedTeams = new ArrayList<>();
        this.playerTeamList = new HashMap<>();
    }

    public void addPlayerToTeam(Player player, Team team) {
        if(getTeamFromPlayer(player) != null) removePlayerFromTeam(player, this.playerTeamList.get(player));
        this.playerTeamList.put(player, team);
        team.getPlayers().add(player);
    }

    public void removePlayerFromTeam(Player player, Team team) {
        this.playerTeamList.remove(player);
        team.getPlayers().remove(player);
    }

    public Team getTeamFromPlayer(Player player) {
        return this.playerTeamList.get(player);
    }

    public void sendTablistPrefix(Player player) {
        TimeSpigotAPI.getInstance().getTablistManager().registerRankTeam(player, this.playerTeamList.get(player).getTeamPrefix(), "", this.playerTeamList.get(player).getTeamColor(), 25);
    }

    public List<Team> getRegistedTeams() {
        return registedTeams;
    }

    public HashMap<Player, Team> getPlayerTeamList() {
        return playerTeamList;
    }
}
