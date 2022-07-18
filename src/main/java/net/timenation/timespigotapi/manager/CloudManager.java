package net.timenation.timespigotapi.manager;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import eu.thesimplecloud.api.service.ICloudService;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class CloudManager {

    public int getPlayerCount(String group) {
        return CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName(group).getOnlinePlayerCount();
    }

    public int getPlayerCountFromServer(String server) {
        return CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(server).getOnlineCount();
    }

    public void sendPlayerToCloudService(String service, Player player) {
        ICloudService iCloudService = CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(service);
        ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getName());

        iCloudPlayer.connect(iCloudService);
    }

    public void startCloudServer(String cloudServerGroup) {
        CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName(cloudServerGroup).startNewService();
    }

    public void stopCloudServer(String cloudServer) {
        CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(cloudServer).shutdown();
    }

    public String getOnlineTimeinDays(ICloudPlayer iCloudPlayer) {
        long ms = iCloudPlayer.getOnlineTime();
        return String.format("%d", TimeUnit.MILLISECONDS.toDays(ms));
    }

    public String getOnlineTimeinHour(ICloudPlayer iCloudPlayer) {
        long ms = iCloudPlayer.getOnlineTime();
        return String.format("%d", TimeUnit.MILLISECONDS.toHours(ms));
    }

    public String getOnlineTimeinMinutes(ICloudPlayer iCloudPlayer) {
        long ms = iCloudPlayer.getOnlineTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        Date date = new Date(ms);
        return simpleDateFormat.format(date);
    }

    public String getOnlineTimeInDaysAndHours(ICloudPlayer iCloudPlayer) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD:kk");
        return simpleDateFormat.format(new Date(iCloudPlayer.getOnlineTime())).replace(":", "d ") + "h";
    }
}
