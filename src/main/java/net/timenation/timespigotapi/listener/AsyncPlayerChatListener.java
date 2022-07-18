package net.timenation.timespigotapi.listener;

import net.timenation.timespigotapi.TimeSpigotAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("timenation.chat.color")) {
            event.setMessage(event.getMessage().replace("&", "§"));
        }

        event.setFormat(TimeSpigotAPI.getInstance().getColorAPI().process("%1$s <SOLID:4f4242>► §7%2$s"));
    }
}
