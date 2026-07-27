package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NPCRepository {

    private static final String NPC_FILE_NAME = "npcs.yml";
    private static final String NPC_SECTION_KEY = "npcs";

    private final File npcFile;
    private FileConfiguration npcConfig;

    public NPCRepository(RelluEssentials plugin) {
        this.npcFile = new File(plugin.getDataFolder(), NPC_FILE_NAME);
        this.npcConfig = YamlConfiguration.loadConfiguration(npcFile);
    }

    public void save(NPC npc) {
        String path = NPC_SECTION_KEY + "." + npc.getId().toString();
        npcConfig.set(path + ".profileName", npc.getProfileName());
        npcConfig.set(path + ".x", npc.getX());
        npcConfig.set(path + ".y", npc.getY());
        npcConfig.set(path + ".z", npc.getZ());
        npcConfig.set(path + ".world", npc.getWorldName());
        if (npc.getEntityUUID() != null) {
            npcConfig.set(path + ".entityUUID", npc.getEntityUUID().toString());
        }
        persistToDisk();
    }

    public void delete(UUID npcId) {
        npcConfig.set(NPC_SECTION_KEY + "." + npcId.toString(), null);
        persistToDisk();
    }

    public List<NPC> loadAll() {
        List<NPC> npcs = new ArrayList<>();
        if (!npcConfig.contains(NPC_SECTION_KEY)) {
            return npcs;
        }
        for (String key : Objects.requireNonNull(npcConfig.getConfigurationSection(NPC_SECTION_KEY)).getKeys(false)) {
            String path = NPC_SECTION_KEY + "." + key;
            UUID id = UUID.fromString(key);
            String profileName = npcConfig.getString(path + ".profileName");
            double x = npcConfig.getDouble(path + ".x");
            double y = npcConfig.getDouble(path + ".y");
            double z = npcConfig.getDouble(path + ".z");
            String world = npcConfig.getString(path + ".world");
            NPC npc = new NPC(id, profileName, x, y, z, world);
            String entityUUIDString = npcConfig.getString(path + ".entityUUID");
            if (entityUUIDString != null) {
                npc.setEntityUUID(UUID.fromString(entityUUIDString));
            }
            npcs.add(npc);
        }
        return npcs;
    }

    private void persistToDisk() {
        try {
            npcConfig.save(npcFile);
        } catch (IOException e) {
            RelluEssentials.getInstance().getLogger().severe("Failed to save NPC data: " + e.getMessage());
        }
    }
}