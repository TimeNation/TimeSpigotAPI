package net.timenation.timespigotapi.player;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TimePlayer {

    private String playerName;
    private UUID playerUuid;
    private String playerNickName;
    private String language;
    private int crystals;
    private int lootboxes;
    private boolean nickTool;
    private boolean isNicked;
}
