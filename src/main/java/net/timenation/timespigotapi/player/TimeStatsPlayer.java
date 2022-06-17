package net.timenation.timespigotapi.player;

@lombok.Getter
@lombok.Setter
public class TimeStatsPlayer {

    private String playerName;
    private String playerUuid;
    private String game;
    private int wins;
    private int looses;
    private int games;
    private int kills;
    private int deaths;
}
