package net.timenation.timespigotapi.manager;

import com.comphenix.protocol.wrappers.EnumWrappers;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.event.PacketEntityInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectionClass {

    public static void inject(Player player) {
        ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, Packet packet, List<Object> list) throws Exception {
                if(packet.getClass().getSimpleName().equals("PacketPlayInUseEntity")) {
                    PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
                    Bukkit.getScheduler().runTask(TimeSpigotAPI.getInstance(), () -> {
                        Bukkit.getPluginManager().callEvent(new PacketEntityInteractEvent(player, getEntityId(packetPlayInUseEntity), EnumWrappers.EntityUseAction.INTERACT_AT));
                    });
                }

                list.add(packet);
            }
        });
    }

    public static void uninject(Player player) {
        ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline().remove("PacketInjector");
    }

    private static int getEntityId(PacketPlayInUseEntity packetPlayInUseEntity) {
        int i = 0;
        try {
            Field field = packetPlayInUseEntity.getClass().getDeclaredField("a");
            field.setAccessible(true);
            i = (int) field.get(packetPlayInUseEntity);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return i;
    }
}
