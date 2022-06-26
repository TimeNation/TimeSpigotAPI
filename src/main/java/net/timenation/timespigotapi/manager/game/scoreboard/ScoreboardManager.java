package net.timenation.timespigotapi.manager.game.scoreboard;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.language.I18n;
import net.timenation.timespigotapi.manager.scoreboard.ScoreboardBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager {

    private final TimeGame game;
    private final List<ScoreboardBuilder> scoreboardBuilders;

    public ScoreboardManager(TimeGame game) {
        this.game = game;
        this.scoreboardBuilders = new ArrayList<>();
    }

    public void updateLobbyScoreboard(Player player, int count) {
        scoreboardBuilders.forEach(scoreboardBuilder -> scoreboardBuilder.setLine(game.isGameWithKits() ? 12 : 9, "  §8● " + game.getColor() + "§l" + count));
    }

    public void sendLobbyScoreboardToPlayer(Player player, int count) {
        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player);
        scoreboardBuilders.add(scoreboardBuilder);
        scoreboardBuilder.setTitle(I18n.format(player, "game.scoreboard.title", (Object) game.getColor(), game.getGameName(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()));

        scoreboardBuilder.setLine(0, "§8§m                        ");
        scoreboardBuilder.setLine(1, "§1");
        scoreboardBuilder.setLine(2, I18n.format(player, "game.scoreboard.players", (Object) game.getSecoundColor()));
        scoreboardBuilder.setLine(3, "  §8● " + game.getColor() + Bukkit.getOnlinePlayers().size() + "§8/" + game.getColor() + Bukkit.getMaxPlayers() + game.getSecoundColor() + " ☺");
        scoreboardBuilder.setLine(4, "§2");
        scoreboardBuilder.setLine(5, I18n.format(player, "game.scoreboard.map", (Object) game.getSecoundColor()));
        scoreboardBuilder.setLine(6, "  §8● " + game.getColor() + game.getGameMap() + game.getSecoundColor() + " ☁");
        scoreboardBuilder.setLine(7, "§3");
        scoreboardBuilder.setLine(8, I18n.format(player, "game.scoreboard.countdown", (Object) game.getSecoundColor()));
        scoreboardBuilder.setLine(9, "  §8● " + game.getColor() + "§l" + count + "§r " + game.getSecoundColor() + "⌚");
        scoreboardBuilder.setLine(10, "");
        scoreboardBuilder.setLine(11, "§r§8§m                        ");
    }

    public void sendLobbyKitScoreboardToPlayer(Player player, int count, String kitName) {
        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player);
        scoreboardBuilders.add(scoreboardBuilder);
        scoreboardBuilder.setTitle(I18n.format(player, "game.scoreboard.title", (Object) game.getColor(), game.getGameName(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()));

        scoreboardBuilder.setLine(0, "§8§m                        ");
        scoreboardBuilder.setLine(1, "§1");
        scoreboardBuilder.setLine(2, I18n.format(player, "game.scoreboard.players"));
        scoreboardBuilder.setLine(3, "  §8● " + game.getColor() + Bukkit.getOnlinePlayers().size() + "§8/" + game.getColor() + Bukkit.getMaxPlayers() + game.getSecoundColor() + " ☺");
        scoreboardBuilder.setLine(4, "§2");
        scoreboardBuilder.setLine(5,  I18n.format(player, "game.scoreboard.kit", (Object) game.getSecoundColor()));
        scoreboardBuilder.setLine(6, "  §8● " + game.getColor() + kitName);
        scoreboardBuilder.setLine(7, "§3");
        scoreboardBuilder.setLine(8, I18n.format(player, "game.scoreboard.map", (Object) game.getSecoundColor()));
        scoreboardBuilder.setLine(9, "  §8● " + game.getColor() + game.getGameMap() + game.getSecoundColor() + " ☁");
        scoreboardBuilder.setLine(10, "§4");
        scoreboardBuilder.setLine(11, I18n.format(player, "game.scoreboard.countdown", (Object) game.getSecoundColor()));
        scoreboardBuilder.setLine(12, "  §8● " + game.getColor() + "§l" + count + "§r " + game.getSecoundColor() + "⌚");
        scoreboardBuilder.setLine(13, "§5");
        scoreboardBuilder.setLine(14, "§r§8§m                        ");
    }

    public void sendEndScoreboardToPlayer(Player player, Player winner) {
        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player);
        scoreboardBuilder.setTitle(I18n.format(player, "game.scoreboard.title", (Object) game.getColor(), game.getGameName(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()));

        scoreboardBuilder.setLine(0, "§8§m                        ");
        scoreboardBuilder.setLine(1, "§1");
        scoreboardBuilder.setLine(2, I18n.format(player, "game.scoreboard.winner"));
        scoreboardBuilder.setLine(3, "  §8● " + TimeSpigotAPI.getInstance().getRankManager().getPlayersRank(winner.getUniqueId()).getPlayersRankAndName(winner.getUniqueId()) + game.getSecoundColor() + " ✮");
        scoreboardBuilder.setLine(4, "§2");
        scoreboardBuilder.setLine(5, "§r§8§m                        ");
    }
}
