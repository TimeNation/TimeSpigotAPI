package net.timenation.timespigotapi.manager.game.features;

import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class TrampolineFeature implements Listener {

    private TimeGame game;

    public TrampolineFeature(TimeGame game) {
        this.game = game;

        Bukkit.getPluginManager().registerEvents(this, game);
    }

    @EventHandler
    public void handlePlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().subtract(0, 1, 0).getBlock();

        if(!game.getGameState().equals(GameState.INGAME)) {
            if(block.getType().equals(Material.SLIME_BLOCK)) {
                player.setVelocity(new Vector(0, 1.5, 0));
            }
        }
    }
}
