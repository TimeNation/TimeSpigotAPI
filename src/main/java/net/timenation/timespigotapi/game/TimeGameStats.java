package net.timenation.timespigotapi.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeGameStats {

    private String gameName;
    private String lastPatchLink;
    private int uniquePlayers;
    private int weeklyPlayers;
}