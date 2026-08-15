package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

@ListenerName("NpcChunkLoadListener")
public class NpcChunkLoadListener implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    List<Npc> npcsInChunk = findNpcsInChunk(event.getChunk());
    if (npcsInChunk.isEmpty()) {
      return;
    }

    serviceContext.getSchedulerService().runTaskLater(
        () -> npcsInChunk.forEach(
            npc -> serviceContext.getNpcService().spawnNpc(npc)),
        5L
    );
  }

  @EventHandler
  public void onChunkUnload(ChunkUnloadEvent event) {
    findNpcsInChunk(event.getChunk()).forEach(npc ->
        serviceContext.getNpcService().despawnNpc(npc.getId())
    );
  }

  private List<Npc> findNpcsInChunk(Chunk chunk) {
    return serviceContext.getNpcService().getAllNpcs().stream()
        .filter(npc -> npc.getWorldName().equals(chunk.getWorld().getName()))
        .filter(npc -> toChunkCoordinate(npc.getX()) == chunk.getX()
            && toChunkCoordinate(npc.getZ()) == chunk.getZ())
        .toList();
  }

  private int toChunkCoordinate(double blockCoordinate) {
    return ((int) blockCoordinate) >> 4;
  }
}