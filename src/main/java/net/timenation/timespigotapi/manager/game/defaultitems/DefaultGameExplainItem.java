package net.timenation.timespigotapi.manager.game.defaultitems;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DefaultGameExplainItem implements Listener {

    private final TimeGame game;
    private final int slot;
    private final String translateKey;

    public DefaultGameExplainItem(TimeGame game, int slot, String i18n) {
        this.game = game;
        this.slot = slot;
        this.translateKey = i18n;

        Bukkit.getPluginManager().registerEvents(this, game);
    }

    public void setItem(Player player) {
        player.getInventory().setItem(slot, new ItemManager(Material.BOOK, 1).setDisplayName(I18n.format(player, "game.item.explain", game.getPrefix())).build());
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().setItem(slot, new ItemManager(Material.BOOK, 1).setDisplayName(I18n.format(event.getPlayer(), "game.item.explain", game.getPrefix())).build());
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null || event.getItem().getItemMeta() == null || event.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem().getType().equals(Material.BOOK) && !game.getGameState().equals(GameState.INGAME)) {
                player.sendMessage(" ");
                player.sendMessage(I18n.format(player, TimeSpigotAPI.getInstance().getColorAPI().process(translateKey)));
                player.sendMessage(" ");
                player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 10F, 10F);
            }
        }
    }
}
