package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_POSITION_HIGHLIGHTING_DIFFERENT_WORLDS;

public class PositionHighlightManager implements IEnable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void enable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> toRemove = new ArrayList<>();
                for (Map.Entry<Player, DoubleStore<Location, Location>> entry : RelluEssentials.getInstance().postion.entrySet()) {
                    Player p = entry.getKey();
                    if (!p.isOnline()) {
                        toRemove.add(p);
                        continue;
                    }
                    DoubleStore<Location, Location> ds = entry.getValue();
                    Location firstLocation = ds.getValue();
                    Location secondLocation = ds.getSecondValue();
                    if (firstLocation == null && secondLocation == null) {
                        toRemove.add(p);
                        continue;
                    }

                    World w = firstLocation != null ? firstLocation.getWorld() : secondLocation.getWorld();
                    if (firstLocation != null && secondLocation != null) {
                        if (!Objects.equals(firstLocation.getWorld(), secondLocation.getWorld())) {
                            p.sendMessage(PLUGIN_POSITION_HIGHLIGHTING_DIFFERENT_WORLDS);
                            continue;
                        }

                        double minX = Math.min(firstLocation.getX(), secondLocation.getX());
                        double minY = Math.min(firstLocation.getY(), secondLocation.getY());
                        double minZ = Math.min(firstLocation.getZ(), secondLocation.getZ());
                        double maxX = Math.max(firstLocation.getX(), secondLocation.getX());
                        double maxY = Math.max(firstLocation.getY(), secondLocation.getY());
                        double maxZ = Math.max(firstLocation.getZ(), secondLocation.getZ());
                        drawBoxEdges(p, w, minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
                    } else if (firstLocation != null) {
                        drawBoxEdges(p, w, firstLocation.getX(), firstLocation.getY(), firstLocation.getZ(), firstLocation.getX() + 1, firstLocation.getY() + 1, firstLocation.getZ() + 1);
                    } else if (secondLocation != null) {
                        drawBoxEdges(p, w, secondLocation.getX(), secondLocation.getY(), secondLocation.getZ(), secondLocation.getX() + 1, secondLocation.getY() + 1, secondLocation.getZ() + 1);
                    }
                }
                for (Player p : toRemove) {
                    RelluEssentials.getInstance().postion.remove(p);
                }
            }
        }.runTaskTimer(RelluEssentials.getInstance(), 0L, 20L);
    }

    private void drawBoxEdges(Player p, World w, double x1, double y1, double z1, double x2, double y2, double z2) {
        double step = 1.0;
        for (double x = x1; x <= x2; x += step) {
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x, y1, z1), 1);
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x, y1, z2), 1);
        }
        for (double z = z1; z <= z2; z += step) {
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x1, y1, z), 1);
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x2, y1, z), 1);
        }
        for (double x = x1; x <= x2; x += step) {
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x, y2, z1), 1);
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x, y2, z2), 1);
        }
        for (double z = z1; z <= z2; z += step) {
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x1, y2, z), 1);
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x2, y2, z), 1);
        }
        for (double y = y1; y <= y2; y += step) {
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x1, y, z1), 1);
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x1, y, z2), 1);
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x2, y, z1), 1);
            p.spawnParticle(Particle.COMPOSTER, new Location(w, x2, y, z2), 1);
        }
    }
}
