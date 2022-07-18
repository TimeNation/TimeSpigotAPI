package net.timenation.timespigotapi.manager.game.commands;

import eu.thesimplecloud.api.CloudAPI;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.player.TimePlayer;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CommandUnnick<timeGame extends TimeGame> implements CommandExecutor {

    private final TimeGame timeGame;

    public CommandUnnick(TimeGame timeGame) {
        this.timeGame = timeGame;

        Bukkit.createWorld(new WorldCreator("NICK"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        TimePlayer timePlayer = TimeSpigotAPI.getInstance().getTimePlayerManager().getTimePlayer(player);

        if (!CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId()).getConnectedServer().isLobby()) {
            if (timePlayer.isNicked()) {
                TimeSpigotAPI.getInstance().getNickManager().unnick(player, timeGame);
            }
        }

        return false;
    }
}
