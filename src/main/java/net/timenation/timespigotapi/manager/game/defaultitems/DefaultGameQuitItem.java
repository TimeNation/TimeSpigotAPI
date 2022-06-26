package net.timenation.timespigotapi.manager.game.defaultitems;

import eu.thesimplecloud.api.CloudAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DefaultGameQuitItem implements Listener {

    private final TimeGame game;
    private final int slot;

    public DefaultGameQuitItem(TimeGame game, int slot) {
        this.game = game;
        this.slot = slot;

        Bukkit.getPluginManager().registerEvents(this, game);
    }

    public void setItem(Player player) {
        player.getInventory().setItem(slot, new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName(I18n.format(player, game.getPrefix(), "game.item.quit", (Object) game.getColor())).setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0=").build());
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().setItem(slot, new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName(I18n.format(event.getPlayer(), game.getPrefix(), "game.item.quit", (Object) game.getColor())).setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0=").build());
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getItem() == null || event.getItem().getItemMeta() == null || event.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getItem().getItemMeta().getDisplayName().equals(I18n.format(player, game.getPrefix(), "game.item.quit", (Object) game.getColor()))) {
                CloudAPI.getInstance().getCloudPlayerManager().connectPlayer(CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId()), CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName("Lobby-1"));
                player.sendMessage(I18n.format(player, "game.message.quit.player", game.getPrefix()));
            }
        }
    }
}
