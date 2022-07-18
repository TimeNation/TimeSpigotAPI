package net.timenation.timespigotapi.manager.game.countdown;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;

public class Timer {

    private TimeGame game;
    private int time;

    public Timer(TimeGame game, int time, boolean runnable) {
        this.game = game;
        this.time = time;
    }

    public Timer(TimeGame game, int time) {
        this.game = game;
        this.time = time;

        Bukkit.getScheduler().runTaskTimerAsynchronously(game, () -> {
            if (game.getGameState().equals(GameState.INGAME)) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(I18n.format(player, game.getPrefix(), "game.actionbar.ingame", getTimeInFormatWithoutHours(this.time), game.getGameMap())));
                });
                this.time--;
            }
        }, 0, 20);
    }

    public Timer(TimeGame game) {
        this.game = game;
        this.time = 0;

        Bukkit.getScheduler().runTaskTimerAsynchronously(game, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(I18n.format(player, game.getPrefix(), "private.actionbar.timer", getTimeInFormatWithHours(this.time), game.getGameMap())));
            });
            this.time++;
        }, 0, 20);
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void stopTimer() {
        this.time = -1;
    }

    public String getTimeInFormatWithoutHours(int duration) {
        String string = "";
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (duration / 60 / 60 / 24 >= 1) {
            duration -= duration / 60 / 60 / 24 * 60 * 60 * 24;
        }
        if (duration / 60 / 60 >= 1) {
            hours = duration / 60 / 60;
            duration -= duration / 60 / 60 * 60 * 60;
        }
        if (duration / 60 >= 1) {
            minutes = duration / 60;
            duration -= duration / 60 * 60;
        }
        if (duration >= 1)
            seconds = duration;
        if (minutes <= 9) {
            string = String.valueOf(string) + "0" + minutes + ":";
        } else {
            string = String.valueOf(string) + minutes + ":";
        }
        if (seconds <= 9) {
            string = String.valueOf(string) + "0" + seconds;
        } else {
            string = String.valueOf(string) + seconds;
        }
        return string;
    }

    private String getTimeInFormatWithHours(int duration) {
        String string = "";
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (duration / 60 / 60 / 24 >= 1) {
            duration -= duration / 60 / 60 / 24 * 60 * 60 * 24;
        }
        if (duration / 60 / 60 >= 1) {
            hours = duration / 60 / 60;
            duration -= duration / 60 / 60 * 60 * 60;
        }
        if (duration / 60 >= 1) {
            minutes = duration / 60;
            duration -= duration / 60 * 60;
        }
        if (duration >= 1)
            seconds = duration;
        if (hours <= 9) {
            string = String.valueOf(string) + "0" + hours + ":";
        } else {
            string = String.valueOf(string) + hours + ":";
        }
        if (minutes <= 9) {
            string = String.valueOf(string) + "0" + minutes + ":";
        } else {
            string = String.valueOf(string) + minutes + ":";
        }
        if (seconds <= 9) {
            string = String.valueOf(string) + "0" + seconds;
        } else {
            string = String.valueOf(string) + seconds;
        }
        return string;
    }
}
