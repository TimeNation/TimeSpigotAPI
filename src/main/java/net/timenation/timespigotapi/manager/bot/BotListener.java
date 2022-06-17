package net.timenation.timespigotapi.manager.bot;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class BotListener implements Listener {

    private final boolean soup;

    public BotListener(boolean soup) {
        this.soup = soup;
    }

    @EventHandler
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.MAGIC) && event.getEntity().hasMetadata("pvpBot")) event.setCancelled(true);

        if (event.getEntity().hasMetadata("pvpBot") && soup) {
            CraftZombie craftZombie = (CraftZombie) event.getEntity();

            craftZombie.getHandle().setItemSlot(EquipmentSlot.MAINHAND, ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.MUSHROOM_STEW)));
            Bukkit.getScheduler().runTaskLaterAsynchronously(TimeSpigotAPI.getInstance(), () -> {
                craftZombie.getHandle().setItemSlot(EquipmentSlot.MAINHAND, ItemStack.fromBukkitCopy(new ItemManager(Material.STONE_SWORD, 1).setUnbreakable(true).build()));
            }, 7);
        }
    }

    @EventHandler
    public void handleEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("pvpBot")) {
            event.getDrops().clear();
        }
    }
}
