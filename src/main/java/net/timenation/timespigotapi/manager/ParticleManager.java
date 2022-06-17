package net.timenation.timespigotapi.manager;

import net.timenation.timespigotapi.TimeSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleManager {
    private double time;

    public void spawnCustomSizeRotateParticles(Location location, Particle particle, double circlesize) {
        time += Math.PI / 36;
        double x = circlesize * Math.cos(time);
        double y = 0.2;
        double z = circlesize * Math.sin(time);
        location.add(x, y, z);
        Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(particle, location, 2));
        location.subtract(x, y, z);
    }

    public void spawnCustomSizeRotateParticlesSin(Location location, Particle particle, double circlesize) {
        time += Math.PI / 36;
        double x = circlesize * Math.cos(time);
        double y = 0.2;
        double z = circlesize * Math.sin(time);
        location.subtract(x, y, z);
        Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(particle, location, 2));
        location.add(x, y, z);
    }

    public void spawnLocationRotateParticle(Location location, Particle particle) {
        time += Math.PI / 36;
        double x = 0.7 * Math.cos(time);
        double y = 0;
        double z = 0.7 * Math.sin(time);

        {
            location.add(x, y, z);
            Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(particle, location, 2));
            location.subtract(x, y, z);
        }

        {
            location.subtract(x, y, z);
            Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(particle, location, 2));
            location.add(x, y, z);
        }
    }

    public void spawnNormalSizeRotateParticles(Location location, Particle particle) {
        time += Math.PI / 18;
        double x = 1.3 * Math.cos(time);
        double y = 0.2;
        double z = 1.3 * Math.sin(time);

        location.add(x, y, z);
        Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(particle, location, 2));
        location.subtract(x, y, z);
    }

    public void spawnNormalSizeRotateParticles(Location location, Particle particle, Particle.DustOptions dustOptions) {
        time += Math.PI / 18;
        double x = 1.3 * Math.cos(time);
        double y = 0.2;
        double z = 1.3 * Math.sin(time);

        location.add(x, y, z);
        Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(particle, location, 2, dustOptions));
        location.subtract(x, y, z);
    }

    public void spawnCircleParticlesAtPlayer(Player player, Particle particle, double circlesize, double high) {
        Location location = player.getLocation().clone();
        time += Math.PI / 8;
        double x = circlesize * Math.sin(time);
        double y = high;
        double z = circlesize * Math.cos(time);
        location.add(x, y, z);
        Bukkit.getOnlinePlayers().forEach(players -> players.spawnParticle(particle, location, 2));
        location.subtract(x, y, z);
    }

    public void spawnCircleParticles(Location location, Particle particle) {
        location.setX(location.getX() - 0.2);
        location.setZ(location.getZ() - 1.6);
        for (int i = 0; i < 360; i += 360 / 20) {
            double angle = (i * Math.PI / 180);
            double x = 0.5 * Math.cos(angle);
            double z = 0.5 * Math.sin(angle);
            Location particleLoc = location.add(x, 0, z);
            Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(particle, location, 2));
        }
    }

    public void startBlockParticle(Location loc, Color color, int size) {
        new BukkitRunnable() {
            double time = 0;
            int state = 0;
            Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
            Vector vector = location.getDirection().normalize();

            @Override
            public void run() {
                time += 0.1;

                double x = vector.getX() * time;
                double y = vector.getY() * time;
                double z = vector.getZ() * time;

                location.add(x, y, z);
                Bukkit.getOnlinePlayers().forEach(player -> player.spawnParticle(Particle.REDSTONE, location, 2, new Particle.DustOptions(color, 1F)));
                location.subtract(x, y, z);

                if (time > 3) {
                    time = 0;
                    location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                    switch (state) {
                        case 0 -> {
                            state++;
                            location.subtract(0, 0, 0.1);
                            vector = location.getDirection();
                        }
                        case 1 -> {
                            state++;
                            location.add(0.1, 0, size);
                            location.setYaw(90);
                            vector = location.getDirection();
                        }
                        case 2 -> {
                            state++;
                            location.add(0, 0, size + 0.1).subtract(size, 0, 0);
                            vector = location.getDirection().setZ(-1);
                        }
                        case 3 -> {
                            location.subtract(size + 0.1, 0, 0);
                            location.setYaw(-90);
                            vector = location.getDirection();
                            state = 0;
                        }
                    }
                }
            }
        }.runTaskTimer(TimeSpigotAPI.getInstance(), 0, 1);
    }
}