package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.model.Npc;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class NpcChunkLoadListener implements Listener {
  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    List<Npc> npcsInChunk = findNpcsInChunk(event.getChunk());
    if (npcsInChunk.isEmpty()) return;

    Bukkit.getScheduler().runTaskLater(
        RelluEssentials.getInstance(),
        () -> npcsInChunk.forEach(npc -> RelluEssentials.getInstance().getNpcService().spawnNpc(npc)),
        5L
    );
  }

  @EventHandler
  public void onChunkUnload(ChunkUnloadEvent event) {
    findNpcsInChunk(event.getChunk()).forEach(npc ->
        RelluEssentials.getInstance().getNpcService().despawnNpc(npc.getId())
    );
  }

  private List<Npc> findNpcsInChunk(Chunk chunk) {
    return RelluEssentials.getInstance().getNpcService().getAllNpcs().stream()
        .filter(npc -> npc.getWorldName().equals(chunk.getWorld().getName()))
        .filter(npc -> toChunkCoordinate(npc.getX()) == chunk.getX()
            && toChunkCoordinate(npc.getZ()) == chunk.getZ())
        .toList();
  }

  private int toChunkCoordinate(double blockCoordinate) {
    return ((int) blockCoordinate) >> 4;
  }
}