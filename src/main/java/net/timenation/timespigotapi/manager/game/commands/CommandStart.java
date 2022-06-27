package net.timenation.timespigotapi.manager.game.commands;

import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
public abstract class CommandStart<timegame extends TimeGame> implements CommandExecutor {

    private final TimeGame timeGame;
    public CommandStart(TimeGame timeGame) {
        this.timeGame = timeGame;
    }
    public abstract void startGame();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;

        if(player.hasPermission("timenation.forcestart")) {
            if(Bukkit.getOnlinePlayers().size() >= timeGame.getNeededPlayers()) {
                if(timeGame.getCountdown() > 10) {
                    startGame();
                    player.sendMessage(I18n.format(player, "game.messages.startgame", timeGame.getPrefix()));
                } else {
                    if(timeGame.getGameState().equals(GameState.INGAME)) {
                        player.sendMessage(I18n.format(player, "game.messages.isingame", timeGame.getPrefix()));
                    } else if(timeGame.getGameState().equals(GameState.ENDING)) {
                        player.sendMessage(I18n.format(player, "game.messages.gameisended", timeGame.getPrefix()));
                    } else {
                        player.sendMessage(I18n.format(player, "game.messages.gamealreadystarts", timeGame.getPrefix()));
                    }
                }
            } else {
                player.sendMessage(I18n.format(player, "game.messages.notenoughplayerstostart", timeGame.getPrefix()));
            }
        } else {
            player.sendMessage(I18n.format(player, "velocity.messages.nopermissions", timeGame.getPrefix()));
        }
        return false;
    }
}
