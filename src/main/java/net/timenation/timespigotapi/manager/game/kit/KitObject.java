package net.timenation.timespigotapi.manager.game.kit;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class KitObject {

    private final String permission;
    private final ChatColor color;
    private PotionEffect[] potionEffect;
    private final ItemStack item;
    private final ItemStack[] content;
    private final ItemStack[] armor;

    public KitObject(String permission, ChatColor color, ItemStack item, ItemStack[] content, ItemStack[] armor) {
        this.permission = permission;
        this.color = color;
        this.content = content;
        this.armor = armor;
        this.item = item;
    }

    public KitObject(String permission, ChatColor color, PotionEffect[] potionEffect, ItemStack item, ItemStack[] content, ItemStack[] armor) {
        this.permission = permission;
        this.color = color;
        this.potionEffect = potionEffect;
        this.content = content;
        this.armor = armor;
        this.item = item;
    }

    public String getPermission() {
        return permission;
    }

    public ChatColor getColor() {
        return color;
    }

    public PotionEffect[] getPotionEffect() {
        return potionEffect;
    }

    public ItemStack[] getContent() {
        return content;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack getItem() {
        return item;
    }

}
