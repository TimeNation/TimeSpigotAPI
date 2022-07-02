package net.timenation.timespigotapi.manager.game.modules;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.TimeGame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class Top3Module {

    private final TimeGame game;
    private final String gameName;
    private final HashMap<Integer, UUID> top3Map;

    public Top3Module(TimeGame game, String gameName) {
        this.game = game;
        this.gameName = gameName;
        this.top3Map = new HashMap<>();

        initTop3();
        setTop3Armorstands();
    }

    public void initTop3() {
        ResultSet resultSet = TimeSpigotAPI.getInstance().getMySQL().getDatabaseResult("SELECT playerUuid FROM playerStats WHERE game='" + gameName + "' ORDER BY wins DESC LIMIT 3");
        int i = 0;

        try {
            while (resultSet.next()) {
                i++;
                top3Map.put(i, UUID.fromString(resultSet.getString("playerUuid")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void setTop3Armorstands() {
        Bukkit.getScheduler().runTaskLater(game, () -> {
            for (Entity entity : Bukkit.getWorld("world").getLivingEntities()) {
                if (entity instanceof ArmorStand armorStand) {
                    if (armorStand.getCustomName().equals("#1")) {
                        armorStand.setHelmet(new ItemManager(Material.PLAYER_HEAD, 1).setSkullOwner(top3Map.get(1)).build());
                        armorStand.setCustomName(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:d1b241>» #1 §8(<SOLID:d1b241>" + TimeSpigotAPI.getInstance().getUuidFetcher().getName(top3Map.get(1)) + "§8)"));
                    }

                    if (armorStand.getCustomName().equals("#2")) {
                        armorStand.setHelmet(new ItemManager(Material.PLAYER_HEAD, 1).setSkullOwner(top3Map.get(2)).build());
                        armorStand.setCustomName(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:9e9e9d>» #2 §8(<SOLID:9e9e9d>" + TimeSpigotAPI.getInstance().getUuidFetcher().getName(top3Map.get(2)) + "§8)"));
                    }

                    if (armorStand.getCustomName().equals("#3")) {
                        armorStand.setHelmet(new ItemManager(Material.PLAYER_HEAD, 1).setSkullOwner(top3Map.get(3)).build());
                        armorStand.setCustomName(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:b87333>» #3 §8(<SOLID:b87333>" + TimeSpigotAPI.getInstance().getUuidFetcher().getName(top3Map.get(3)) + "§8)"));
                    }
                }
            }
        }, 50);
    }
}