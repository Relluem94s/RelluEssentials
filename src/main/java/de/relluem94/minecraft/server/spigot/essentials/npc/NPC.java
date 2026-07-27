package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NPC {
    private final UUID id;
    private String profileName;
    private double x;
    private double y;
    private double z;
    private String worldName;
    private UUID entityUUID;
    private ItemHelper itemHelper;

    public NPC(UUID id, String profileName, double x, double y, double z, String worldName) {
        this.id = id;
        this.profileName = profileName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = worldName;
    }
}