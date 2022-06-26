package net.timenation.timespigotapi.manager;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.data.Components;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private final List<String> lines;
    private final List<ArmorStand> entityArmorStandList;
    private final double ABS = 0.23D;
    private Location location;

    public HologramManager() {
        this.lines = new ArrayList<>();
        this.entityArmorStandList = new ArrayList<>();
    }

    private void addLine(String line) {
        lines.add(line);
    }

    private void setLine(int index, String line) {
        lines.set(index, line);
    }

    private void setLocation(Location location) {
        this.location = location;
    }

    private void clear() {
        this.lines.clear();
    }

    public void setHologram(String translateKey, Player player, Location location, Object... arguments) {
        for (String formatLine : I18n.formatLines(player, translateKey, arguments)) {
            addLine(formatLine);
        }

        setLocation(location);
        display(player);
        clear();
    }

    private void display(Player player) {
        Location hologramLocation = location.clone().add(0, (ABS * lines.size()), 0);
        for (String line : lines) {
            ArmorStand entityArmorStand = new ArmorStand(((CraftWorld) hologramLocation.getWorld()).getHandle(), hologramLocation.getX(), hologramLocation.getY() - 0.23, hologramLocation.getZ());
            entityArmorStand.setInvisible(true);
            entityArmorStand.setNoGravity(true);
            entityArmorStand.setCustomName(Component.literal(line));
            entityArmorStand.setCustomNameVisible(true);

            ((CraftPlayer) player).getHandle().connection.connection.send(new ClientboundAddEntityPacket(entityArmorStand, entityArmorStand.getId()));
            ((CraftPlayer) player).getHandle().connection.connection.send(new ClientboundSetEntityDataPacket(entityArmorStand.getId(), entityArmorStand.getEntityData(), true));
            getEntityArmorStandList().add(entityArmorStand);
            hologramLocation.add(0, -ABS, 0);
        }
    }

    public void destroy(Player player) {
        clear();

        for (ArmorStand entityArmorStand : getEntityArmorStandList()) {
            ClientboundRemoveEntitiesPacket entityDestroy = new ClientboundRemoveEntitiesPacket(entityArmorStand.getId());
            ((CraftPlayer) player).getHandle().connection.connection.send(entityDestroy);
        }
    }

    private List<ArmorStand> getEntityArmorStandList() {
        return entityArmorStandList;
    }
}
