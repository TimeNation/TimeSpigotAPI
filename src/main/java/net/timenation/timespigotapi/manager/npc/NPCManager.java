package net.timenation.timespigotapi.manager.npc;

import eu.thesimplecloud.api.servicegroup.ICloudServiceGroup;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.language.I18n;
import net.timenation.timespigotapi.manager.npc.event.PlayerNPCShowEvent;
import net.timenation.timespigotapi.manager.npc.modifier.*;
import net.timenation.timespigotapi.manager.npc.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

@Getter
public class NPCManager implements Listener {

    private final NPCPool npcPool;
    private final NPC npc;
    private final String maintitle;

    public NPCManager(Plugin plugin, Location location, UUID skinUid, String maintitle, boolean currentPlayerTitle, ICloudServiceGroup iCloudServiceGroup, String value, String signature) {
        this.maintitle = maintitle;
        this.npcPool = NPCPool.builder(plugin)
                .spawnDistance(60)
                .actionDistance(20)
                .tabListRemoveTicks(20)
                .build();

        npc = NPC.builder()
                .profile(this.createProfile(skinUid, value, signature))
                .location(location)
                .imitatePlayer(false)
                .lookAtPlayer(true)
                .build(this.npcPool);

        startSubTitleUpdate(iCloudServiceGroup, location, currentPlayerTitle);

        ArmorStand title = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, 2, 0), EntityType.ARMOR_STAND);
        title.setInvisible(true);
        title.setMarker(true);
        title.setCustomName(TimeSpigotAPI.getInstance().getColorAPI().process(maintitle));
        title.setCustomNameVisible(true);
        npc.labymod().queue(LabyModModifier.LabyModAction.EMOTE, 3);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void startSubTitleUpdate(ICloudServiceGroup cloudServiceGroup, Location location, boolean playerTitle) {
        net.minecraft.world.entity.decoration.ArmorStand armorStand = new net.minecraft.world.entity.decoration.ArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY() - 0.23, location.getZ());
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);

        Bukkit.getScheduler().runTaskTimer(TimeSpigotAPI.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if(playerTitle) armorStand.setCustomName(Component.nullToEmpty(I18n.format(player, "npc.totalplayers", cloudServiceGroup.getOnlinePlayerCount())));
                else armorStand.setCustomName(Component.nullToEmpty(I18n.format(player, "npc.rightclick")));

                ((CraftPlayer) player).getHandle().connection.connection.send(new ClientboundAddEntityPacket(armorStand, armorStand.getId()));
                ((CraftPlayer) player).getHandle().connection.connection.send(new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData(), true));
            });
        }, 0, 20);
    }

    public Profile createProfile(UUID uuid, String value, String signature) {
        var profile = new Profile(uuid);
        profile.complete();
        profile.setName("");
        profile.setUniqueId(UUID.randomUUID());
        profile.setProperty(new Profile.Property("textures", value, signature));
        return profile;
    }

    @EventHandler
    public void handleNPCShow(PlayerNPCShowEvent event) {
        event.send(event.getNPC().metadata().queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, true), event.getNPC().animation().queue(AnimationModifier.EntityAnimation.SWING_MAIN_ARM));
    }

    public void remove() {
        npcPool.removeNPC(npc.getEntityId());
    }
}

