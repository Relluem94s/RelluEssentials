package de.relluem94.minecraft.server.spigot.essentials.helpers.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

@Getter
public class Selection {

    int minX;
    int maxX;
    int minY;
    int maxY;
    int minZ;
    int maxZ;
    World world;

    @Setter
    private Location originalPivot;
    @Setter
    private Vector pivotPlayerOffset;

    private final Location pos1;
    private final Location pos2;

    public Selection(@NotNull Location pos1, @NotNull Location pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;

        minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        world = pos1.getWorld();
    }
}
