package net.timenation.timespigotapi.manager.game.defaultitems;

import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultGameNavigatorItem implements Listener {

    private final TimeGame game;
    private final int slot;

    public DefaultGameNavigatorItem(TimeGame game, int slot) {
        this.game = game;
        this.slot = slot;

        Bukkit.getPluginManager().registerEvents(this, game);
    }

    public void setItem(Player player) {
        player.getInventory().setItem(slot, new ItemManager(Material.COMPASS, 1).setDisplayName(I18n.format(player, game.getPrefix(), "api.game.item.navigator", game.getColor())).build());
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().setItem(slot, new ItemManager(Material.COMPASS, 1).setDisplayName(I18n.format(event.getPlayer(), game.getPrefix(), "api.game.item.navigator", game.getColor())).build());
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getItem() == null || event.getItem().getItemMeta() == null || event.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getItem().getType().equals(Material.COMPASS) && event.getItem().getItemMeta().getDisplayName().equals(I18n.format(player, "api.game.item.navigator", game.getPrefix()))) {
                Inventory inventory = Bukkit.createInventory(null, 9*5, I18n.format(player, "api.game.item.navigator.inventory.title", game.getPrefix()));
                ItemStack blackGlass = new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build();

                for (int i = 0; i < 9; i++) {
                    inventory.setItem(i, blackGlass);
                }

                for (int i = 36; i < 45; i++) {
                    inventory.setItem(i, blackGlass);
                }

                inventory.setItem(18, blackGlass);
                inventory.setItem(26, blackGlass);
                inventory.setItem(38, blackGlass);
                inventory.setItem(42, blackGlass);

                inventory.setItem(9, blackGlass);
                inventory.setItem(17, blackGlass);
                inventory.setItem(27, blackGlass);
                inventory.setItem(35, blackGlass);

                inventory.setItem(20, new ItemManager(Material.SLIME_BLOCK, 1).setDisplayName(I18n.format(player, "api.game.item.navigator.inventory.item.trampoline", game.getPrefix())).build());
                inventory.setItem(24, new ItemManager(Material.BARRIER, 1).setDisplayName(I18n.format(player, "api.game.item.navigator.inventory.item.soon", game.getPrefix())).build());

                player.openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(event.getView().getTitle().equals(I18n.format(player, "api.game.item.navigator.inventory.title", game.getPrefix()))) {
            if (event.getCurrentItem().getType() == Material.SLIME_BLOCK) {
                player.teleport(new Location(Bukkit.getWorld("world"), 138.5, 109, -237.5, -27, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_FOX_TELEPORT, 1, 0);
            }
        }
    }
}
