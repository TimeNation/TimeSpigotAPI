package net.timenation.timespigotapi.manager.game.defaultitems;

import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.game.team.Team;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DefaultGameSelectTeamItem implements Listener {

    private final TimeGame game;
    private final int slot;

    public DefaultGameSelectTeamItem(TimeGame game, int slot) {
        this.game = game;
        this.slot = slot;

        Bukkit.getPluginManager().registerEvents(this, game);
    }

    public void setItem(Player player) {
        player.getInventory().setItem(this.slot, new ItemManager(Material.BEEHIVE, 1).setDisplayName(I18n.format(player, "game.item.selectteam", game.getPrefix())).build());
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().setItem(this.slot, new ItemManager(game.getCountdown() > 10 ? Material.BEEHIVE : Material.BARRIER, 1).setDisplayName(I18n.format(event.getPlayer(), "game.item.selectteam", game.getPrefix())).build());
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem().getType().equals(Material.BEEHIVE) && !game.getGameState().equals(GameState.INGAME)) {
                Inventory inventory = Bukkit.createInventory(null, 9 * 3, I18n.format(player, "game.item.selectteam.inventory.title", game.getPrefix()));
                ItemStack blackGlass = new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build();

                for (int i = 0; i < 9; i++) {
                    inventory.setItem(i, blackGlass);
                }

                for (int i = 18; i < 27; i++) {
                    inventory.setItem(i, blackGlass);
                }

                for (Team registedTeam : game.teamManager.getRegistedTeams()) {
                    List<String> teamPlayers = new ArrayList<>();
                    registedTeam.getPlayers().forEach(teamPlayer -> teamPlayers.add("  §8- " + registedTeam.getTeamColor() + teamPlayer.getName()));
                    inventory.addItem(new ItemManager(Material.LEATHER_CHESTPLATE, 1).setDisplayName(I18n.format(player, game.getPrefix(), "game.item.selectteam.inventory.item.team", registedTeam.getTeamColor() + registedTeam.getTeamName())).setLore(teamPlayers).setLeatherArmorColor(registedTeam.getTeamBaseColor()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE).build());
                }

                player.openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null)
            return;

        if (event.getView().getTitle().equals(I18n.format(player, "game.item.selectteam.inventory.title", game.getPrefix()))) {
            if (event.getCurrentItem().getType().equals(Material.LEATHER_CHESTPLATE)) {
                Team selectedTeam = game.teamManager.getRegistedTeams().get(event.getSlot() - 9);
                if (game.teamManager.getTeamFromPlayer(player) != selectedTeam && selectedTeam.getPlayers().size() < selectedTeam.getPlayerSize()) {
                    game.teamManager.addPlayerToTeam(player, selectedTeam);
                    player.sendMessage(I18n.format(player, game.getPrefix(), "game.messages.selectteam", selectedTeam.getTeamColor() + selectedTeam.getTeamName()));
                    game.teamManager.sendTablistPrefix(player);
                    player.closeInventory();
                }
            }
        }
    }
}
