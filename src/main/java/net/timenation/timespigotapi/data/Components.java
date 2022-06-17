package net.timenation.timespigotapi.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Components {

    public TextComponent ofChildren(final ComponentLike... children) {
        if (children.length == 0) {
            return Component.empty();
        }

        return Component.text().append(children).build();
    }

    public Component parse(final String input, final int onlinePlayers, final int maxPlayers) {
        return MiniMessage.miniMessage().deserialize(
                replacePlayerCount(input, Integer.toString(onlinePlayers), Integer.toString(maxPlayers))
        );
    }

    public Component parse(final String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

    private String replacePlayerCount(final String input, final String online, final String max) {
        return input.replace("{onlinePlayers}", online).replace("{maxPlayers}", max);
    }
}