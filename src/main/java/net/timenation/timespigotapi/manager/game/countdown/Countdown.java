package net.timenation.timespigotapi.manager.game.countdown;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.service.ServiceState;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Countdown<timeGame extends TimeGame> {

    public int countdown;
    public int tpcount;
    public final String gameName;
    public BukkitTask bukkitTask;
    public TimeGame game;

    public Countdown(TimeGame timeGame, String gameName) {
        this.countdown = 60;
        this.tpcount = 0;
        this.gameName = gameName;
        this.game = timeGame;
    }

    public abstract void at0();

    public abstract void before0();

    public abstract void at10();

    public abstract void atEnd();

    public void startCountdown() {
        game.setGameState(GameState.STARTING);
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                switch (countdown) {
                    case 60, 50, 40, 30, 20, 15 -> {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.sendMessage(I18n.format(player, game.getPrefix(), "game.messages.countdown", game.getSecoundColor(), countdown));
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0);
                        });
                    }
                    case 10 -> {
                        at10();
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            if (player.hasPermission("timenation.forcemap"))
                                player.getInventory().getItem(22).setType(Material.BARRIER);
                            player.sendMessage(I18n.format(player, game.getPrefix(), "game.messages.countdown", game.getSecoundColor(), countdown));
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0);
                        });
                    }
                    case 1 -> before0();
                    case 0 -> {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.sendMessage(I18n.format(player, "game.messages.gamestart", game.getPrefix()));
                            player.getInventory().clear();
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 0);
                            player.setLevel(0);
                            player.setExp(0);
                            game.getPlayers().add(player);
                            TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, game.getGameName()).setGames(TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, game.getGameName()).getGames() + 1);
                            game.setGameState(GameState.INGAME);
                            CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).setState(ServiceState.INVISIBLE);
                        });

                        at0();
                    }
                }

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (countdown > 0 && countdown < 6) {
                        player.sendMessage(I18n.format(player, game.getPrefix(), "game.messages.countdown", game.getSecoundColor(), countdown));
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0);
                    }

                    if (countdown > 0 && countdown < 60) {
                        player.setLevel(countdown);
                        player.setExp(countdown / 60F);
                        game.setCountdown(countdown);
                        game.getScoreboardManager().updateLobbyScoreboard(player, countdown);
                    }

                    if (countdown < 0) {
                        cancel();
                    }
                });

                countdown--;
            }
        }.runTaskTimer(game, 0, 20);
    }

    public void startEndCountdown() {
        countdown = 10;
        game.setGameState(GameState.ENDING);
        atEnd();
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(player);
                    game.getSpecatePlayers().remove(player);

                    if (countdown != 0) {
                        player.sendMessage(I18n.format(player, game.getPrefix(), "game.messages.countdown.stop", game.getSecoundColor(), countdown));
                    }
                    if (countdown == 0) {
                        player.sendMessage(I18n.format(player, game.getPrefix(), "game.messages.countdown.stop", game.getSecoundColor(), countdown));
                        TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().updateTimeStatsPlayer(TimeSpigotAPI.getInstance().getTimeStatsPlayerManager().getTimeStatsPlayer(player, gameName));
                        CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId()).sendToLobby();
                        Bukkit.shutdown();
                    }
                    player.setLevel(countdown);
                    player.setExp(countdown / 10F);
                }

                countdown--;
            }
        }.runTaskTimer(game, 0, 20);
    }

    public void stopCountdown() {
        bukkitTask.cancel();
        countdown = 60;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setLevel(countdown);
            player.setExp(countdown / 60F);
            game.getScoreboardManager().updateLobbyScoreboard(player, countdown);
        });
        game.setGameState(GameState.LOBBY);
    }

    public int getCountdown() {
        return countdown;
    }

    public void startGame() {
        countdown = 10;
    }
}
