package net.timenation.timespigotapi.manager.game.defaultitems;

import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DefaultGameNavigatorItem implements Listener {

    private final TimeGame game;
    private final int slot;

    public DefaultGameNavigatorItem(TimeGame game, int slot) {
        this.game = game;
        this.slot = slot;

        Bukkit.getPluginManager().registerEvents(this, game);
    }

    public void setItem(Player player) {
        player.getInventory().setItem(slot, new ItemManager(Material.ECHO_SHARD, 1).setDisplayName(I18n.format(player, "game.item.top3", game.getPrefix())).build());
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().setItem(slot, new ItemManager(Material.ECHO_SHARD, 1).setDisplayName(I18n.format(event.getPlayer(), "game.item.top3", game.getPrefix())).build());
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null || event.getItem().getItemMeta() == null || event.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem().getType().equals(Material.ECHO_SHARD) && !game.getGameState().equals(GameState.INGAME)) {
                player.sendMessage(I18n.format(player, "game.title.teleport.bottom", game.getPrefix()));
                player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_HURT, 10, 2);
                player.teleport(new Location(Bukkit.getWorld("world"), 97.5, 107, -237.5, 45, -8));
            }
        }
    }
}
