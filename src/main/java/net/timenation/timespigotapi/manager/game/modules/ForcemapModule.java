package net.timenation.timespigotapi.manager.game.modules;

import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import net.timenation.timespigotapi.manager.game.manager.ConfigManager;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ForcemapModule implements Listener {

    private final TimeGame timeGame;
    private final ItemStack blackGlass;
    private final String gameName;

    public ForcemapModule(TimeGame timeGame, String gameName) {
        this.timeGame = timeGame;
        this.blackGlass = new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build();
        this.gameName = gameName;

        Bukkit.getPluginManager().registerEvents(this, timeGame);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("timenation.forcemap")) {
            event.getPlayer().getInventory().setItem(22, new ItemManager(timeGame.getCountdown() > 10 ? Material.FLOWER_BANNER_PATTERN : Material.BARRIER, 1).setDisplayName(I18n.format(event.getPlayer(), "game.item.forcemap", timeGame.getPrefix())).build());
        }
    }

    private void openForcemapInventory(Player player) {
        var inventory = Bukkit.createInventory(null, 9 * 5, I18n.format(player, "game.inventory.forcemap.title", timeGame.getPrefix()));

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

        for (File files : new File("plugins/" + gameName + "/maps").listFiles()) {
            inventory.addItem(new ItemManager(Material.getMaterial(new ConfigManager(gameName, files.getName()).getString("mapMaterial")), 1).setDisplayName("§8» " + timeGame.getColor() + files.getName().replace(".json", "")).setLore(I18n.formatLines(player, "game.forcemap.item.map.lore", (Object) new ConfigManager(gameName, files.getName()).getString("mapName"), timeGame.getColor(), new ConfigManager(gameName, files.getName()).getString("mapBuilder"))).build());
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equals("Crafting")) {
            if (event.getCurrentItem().getType().equals(Material.FLOWER_BANNER_PATTERN)) {
                openForcemapInventory(player);
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "game.inventory.forcemap.title", timeGame.getPrefix()))) {
            timeGame.configManager = new ConfigManager(gameName, event.getCurrentItem().getItemMeta().getLore().get(0).replace(" §8● §7Name§8: §7", "").replace(" §8● §7Navn§8: §7", "") + ".json");
            timeGame.setGameMap(timeGame.configManager.getString("mapName"), timeGame.configManager.getString("mapBuilder"), Bukkit.getWorld(timeGame.configManager.getString("mapWorld")));

            player.closeInventory();
            player.sendMessage(I18n.format(player, timeGame.getPrefix(), "game.messages.forcemap.complete", timeGame.getGameMap()));

            if (timeGame.isGameWithKits()) {
                timeGame.getScoreboardManager().sendLobbyKitScoreboardToPlayer(player, timeGame.getCountdown(), timeGame.getPlayerKit().get(player));
                return;
            }
            timeGame.getScoreboardManager().sendLobbyScoreboardToPlayer(player, timeGame.getCountdown());
        }
    }
}
