package net.timenation.timespigotapi.manager.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PacketEntityInteractEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final int entityId;
    private final EnumWrappers.EntityUseAction enumEntityUseAction;

    public PacketEntityInteractEvent(Player player, int entityId, EnumWrappers.EntityUseAction enumEntityUseAction) {
        this.player = player;
        this.entityId = entityId;
        this.enumEntityUseAction = enumEntityUseAction;
    }

    public Player getPlayer() {
        return player;
    }

    public int getEntityId() {
        return entityId;
    }

    public EnumWrappers.EntityUseAction getEnumEntityUseAction() {
        return enumEntityUseAction;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
