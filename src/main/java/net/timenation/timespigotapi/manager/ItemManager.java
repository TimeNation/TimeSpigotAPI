package net.timenation.timespigotapi.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author NachGecodet
 * @version 1.0
 * <p>
 * IMPORTANT!
 * This ItemManager is only for 1.16
 * Added some methodes for TimeNation.net by ByRaudy
 */
public class ItemManager {

    private final ExecutorService pool = Executors.newFixedThreadPool(2);
    private final Map<UUID, String> textureCache;
    private final ItemStack item;
    private final ItemMeta meta;

    public ItemManager(final ItemStack item) {
        this.textureCache = new HashMap<>();
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemManager(final Material material) {
        this(material, 1, 0);
    }

    public ItemManager(final Material material, final int amount) {
        this(material, amount, 0);
    }

    public ItemManager(final Material material, final int amount, final int data) {
        this.textureCache = new HashMap<>();
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    public ItemManager setDurability(final int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemManager addEnchantment(final Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemManager addEnchantment(final Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this::addEnchantment);
        return this;
    }

    public ItemManager removeEnchantment(final Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public ItemManager addItemFlags(final ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemManager removeItemFlags(final ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    public ItemManager setCustomModleData(final int customModleData) {
        meta.setCustomModelData(customModleData);
        return this;
    }

    public ItemManager setSkullOwner(final String texture) {
        if (item.getType() == Material.PLAYER_HEAD) {
            final SkullMeta skullMeta = (SkullMeta) meta;
            final GameProfile profile = new GameProfile(UUID.randomUUID(), null);

            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                final Field field = skullMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(skullMeta, profile);
                field.setAccessible(false);
                item.setItemMeta(skullMeta);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    public ItemManager setSkullOwner(final OfflinePlayer offlinePlayer) {
        return setSkullOwner(((CraftPlayer) offlinePlayer).getProfile().getProperties().get("textures").iterator().next().getValue());
    }

    public ItemManager setSkullOwner(final UUID uuid) {
        if (textureCache.containsKey(uuid)) {
            setSkullOwner(textureCache.get(uuid));
            return this;
        }

        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).openConnection();
            connection.setReadTimeout(5000);

            final JsonObject result = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            final JsonArray properties = result.get("properties").getAsJsonArray();
            final String texture = properties.get(0).getAsJsonObject().get("value").getAsString();
            textureCache.put(uuid, texture);
            return setSkullOwner(texture);
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public ItemManager setPattern(final int position, final Pattern pattern) {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            final BannerMeta bannerMeta = (BannerMeta) meta;

            bannerMeta.setPattern(position, pattern);
            item.setItemMeta(bannerMeta);
        }

        return this;
    }

    public ItemManager addPattern(final Pattern pattern) {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            final BannerMeta bannerMeta = (BannerMeta) meta;

            bannerMeta.addPattern(pattern);
            item.setItemMeta(bannerMeta);
        }

        return this;
    }

    public ItemManager removePattern(final int position) {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            final BannerMeta bannerMeta = (BannerMeta) meta;

            bannerMeta.removePattern(position);
            item.setItemMeta(bannerMeta);
        }

        return this;
    }

    public ItemManager setBookPage(final int page, final String content) {
        if (item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {
            final BookMeta bookMeta = (BookMeta) meta;

            bookMeta.setPage(page, content);
            item.setItemMeta(bookMeta);
        }
        return this;
    }

    public ItemManager addBookPage(final String... pages) {
        if (item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {
            final BookMeta bookMeta = (BookMeta) meta;

            bookMeta.addPage(pages);
            item.setItemMeta(bookMeta);
        }

        return this;
    }

    /**
     * Add a effect to a potion
     *
     * @param type      The potion type
     * @param strength  The amplifier
     * @param duration  The duration
     * @param overwrite Should the current effects be overwritten
     * @return {@link ItemManager}
     */
    public ItemManager addPotionEffect(final PotionEffectType type, final int strength, final int duration, final boolean overwrite) {
        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION) {
            final PotionMeta potionMeta = (PotionMeta) meta;

            potionMeta.addCustomEffect(new PotionEffect(type, strength, duration), overwrite);
            item.setItemMeta(potionMeta);
        }
        return this;
    }

    /**
     * The effect type
     *
     * @param type The potion type
     * @return {@link ItemManager}
     */
    public ItemManager removePotionEffect(final PotionEffectType type) {
        if (item.getType() == Material.POTION) {
            final PotionMeta potionMeta = (PotionMeta) meta;

            potionMeta.removeCustomEffect(type);
            item.setItemMeta(potionMeta);
        }

        return this;
    }

    /**
     * @return The Material from the ItemStack
     */
    public Material getType() {
        return item.getType();
    }

    /**
     * Set the Material for the ItemStack
     *
     * @param material new Material for the ItemStack
     * @return {@link ItemManager}
     */
    public ItemManager setType(final Material material) {
        item.setType(material);
        return this;
    }

    /**
     * @return The Display Name from the ItemStack
     */
    public String getDisplayName() {
        return meta.hasDisplayName() ? meta.getDisplayName() : "";
    }

    /**
     * Set the Display Name for the ItemStack
     *
     * @param displayName new Display Name for the ItemStack
     * @return {@link ItemManager}
     */
    public ItemManager setDisplayName(final String displayName) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    /**
     * @return The Amount from the ItemStack
     */
    public int getAmount() {
        return item.getAmount();
    }

    /**
     * Set the Amount for the ItemStack
     *
     * @param amount new Amount for the ItemStack
     * @return {@link ItemManager}
     */
    public ItemManager setAmount(final int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * @return The Lore List from the ItemStack
     */
    public List<String> getLore() {
        return meta.hasLore() ? meta.getLore() : new ArrayList<>();
    }

    /**
     * Set the Lore for the ItemStack
     *
     * @param lore List with Strings for the Item Lore
     * @return {@link ItemManager}
     */
    public ItemManager setLore(final List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    /**
     * Set the Lore for the ItemStack
     *
     * @param lore String Array for the Item Lore
     * @return {@link ItemManager}
     */
    public ItemManager setLore(final String... lore) {
        setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * @return The Enchantments from the ItemStack
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return meta.hasEnchants() ? meta.getEnchants() : new HashMap<>();
    }

    /**
     * @return The Item Flags from the ItemStack
     */
    public Set<ItemFlag> getItemFlags() {
        return meta.getItemFlags().isEmpty() ? new HashSet<>() : meta.getItemFlags();
    }

    /**
     * @return The Base Color from the Banner
     */
    public DyeColor getBaseColor() {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            return ((BannerMeta) meta).getBaseColor();
        }

        return null;
    }

    /**
     * Set the Base color for the Banner
     *
     * @param color The Color for the Banner
     * @return {@link ItemManager}
     */
    public ItemManager setBaseColor(final DyeColor color) {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            final BannerMeta bannerMeta = (BannerMeta) meta;

            bannerMeta.setBaseColor(color);
            item.setItemMeta(bannerMeta);
        }

        return this;
    }

    /**
     * @return A List of Patterns from the Banner
     */
    public List<Pattern> getPatterns() {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            return ((BannerMeta) meta).getPatterns();
        }

        return null;
    }

    /**
     * Set the Banner patterns
     *
     * @param patterns The patterns for the Banner
     * @return {@link ItemManager}
     */
    public ItemManager setPatterns(final List<Pattern> patterns) {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            final BannerMeta bannerMeta = (BannerMeta) meta;

            bannerMeta.setPatterns(patterns);
            item.setItemMeta(bannerMeta);
        }

        return this;
    }

    /**
     * Set the Banner patterns
     *
     * @param patterns The patterns for the Banner
     * @return {@link ItemManager}
     */
    public ItemManager setPatterns(final Pattern... patterns) {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            final BannerMeta bannerMeta = (BannerMeta) meta;

            bannerMeta.setPatterns(Arrays.asList(patterns));
            item.setItemMeta(bannerMeta);
        }

        return this;
    }

    /**
     * @param position The position from the Banner
     * @return A Pattern of a specific position
     */
    public Pattern getPattern(int position) {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            return ((BannerMeta) meta).getPattern(position);
        }

        return null;
    }

    /**
     * @return The Count of Patterns from the Banner
     */
    public int getPatternCount() {
        if (item.getType().name().contains("BANNER") && !item.getType().name().contains("PATTERN")) {
            return ((BannerMeta) meta).numberOfPatterns();
        }

        return 0;
    }

    /**
     * @return The Color from the Leather Armor
     */
    public Color getLeatherArmorColor() {
        return item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_HELMET ? ((LeatherArmorMeta) meta).getColor() : null;
    }

    /**
     * Set a Color for the Leather Armor
     *
     * @param color The Color for the Leather Armor
     * @return {@link ItemManager}
     */
    public ItemManager setLeatherArmorColor(final Color color) {
        if (item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_HELMET) {
            final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;

            leatherArmorMeta.setColor(color);
            item.setItemMeta(leatherArmorMeta);
        }

        return this;
    }

    /**
     * @return The book title
     */
    public String getBookTitle() {
        return item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK ? ((BookMeta) meta).getTitle() : "";
    }

    /**
     * Set the book title
     *
     * @param title The title for the book
     * @return {@link ItemManager}
     */
    public ItemManager setBookTitle(final String title) {
        if (item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {
            final BookMeta bookMeta = (BookMeta) meta;

            bookMeta.setTitle(title);
            item.setItemMeta(bookMeta);
        }
        return this;
    }

    /**
     * @return The book author
     */
    public String getBookAuthor() {
        return item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK ? ((BookMeta) meta).getAuthor() : "";
    }

    /**
     * Set the book author
     *
     * @param author The author of the book
     * @return {@link ItemManager}
     */
    public ItemManager setBookAuthor(final String author) {
        if (item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {

            final BookMeta bookMeta = (BookMeta) meta;

            bookMeta.setAuthor(author);
            item.setItemMeta(bookMeta);
        }
        return this;
    }

    /**
     * @return The book pages
     */
    public List<String> getBookPages() {
        return item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK ? ((BookMeta) meta).getPages() : new ArrayList<>();
    }

    /**
     * Set the book pages
     *
     * @param pages The book pages
     * @return {@link ItemManager}
     */
    public ItemManager setBookPages(final List<String> pages) {
        if (item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {
            final BookMeta bookMeta = (BookMeta) meta;

            bookMeta.setPages(pages);
            item.setItemMeta(bookMeta);
        }
        return this;
    }

    /**
     * Set the book pages
     *
     * @param pages The book pages
     * @return {@link ItemManager}
     */
    public ItemManager setBookPages(final String... pages) {
        if (item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.WRITTEN_BOOK) {
            setBookPages(Arrays.asList(pages));
        }

        return this;
    }

    /**
     * @return The book page count
     */
    public int getBookPageCount() {
        return item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK ? ((BookMeta) meta).getPageCount() : 0;
    }

    /**
     * @return The a list with all the potion effects
     */
    public List<PotionEffect> getPotionEffects() {
        return item.getType() == Material.POTION ? ((PotionMeta) meta).getCustomEffects() : new ArrayList<>();
    }

    /**
     * @param type The potion type
     * @return If the potion has a specify effect
     */
    public boolean hasPotionEffect(final PotionEffectType type) {
        return item.getType() == Material.POTION && ((PotionMeta) meta).hasCustomEffect(type);
    }

    /**
     * @return If the map scaling enabled
     */
    public boolean isMapScaling() {
        return item.getType() == Material.MAP && ((MapMeta) meta).isScaling();
    }

    /**
     * Set the map scaling
     *
     * @param scaling The scaling value
     * @return {@link ItemManager}
     */
    public ItemManager setMapScaling(final boolean scaling) {
        if (item.getType() == Material.MAP) {
            final MapMeta mapMeta = (MapMeta) meta;

            mapMeta.setScaling(scaling);
            item.setItemMeta(mapMeta);
        }

        return this;
    }

    /**
     * @return If the ItemStack Unbreakable
     */
    public boolean isUnbreakable() {
        return meta.isUnbreakable();
    }

    /**
     * Make the Item Unbreakable
     *
     * @param unbreakable make the ItemStack Unbreakable or not
     * @return {@link ItemManager}
     */
    public ItemManager setUnbreakable(final boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Repair the ItemStack
     *
     * @return {@link ItemManager}
     */
    public ItemManager repair() {
        if (item.getType().getMaxDurability() > 0)
            item.setDurability((short) 0);
        return this;
    }

    /**
     * Build the ItemManager to a ItemStack
     *
     * @return {@link ItemStack}
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}