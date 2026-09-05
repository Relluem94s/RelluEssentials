package de.relluem94.minecraft.server.spigot.essentials.services.cleanup;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Service responsible for cleaning up outdated or invalid protection entries.
 */
public class ProtectionCleanUpService {

  private final ServiceContext serviceContext;

  /**
   * Constructs a new {@link ProtectionCleanUpService} with the provided service context.
   *
   * @param serviceContext the context containing necessary services
   */
  public ProtectionCleanUpService(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  /**
   * Starts an asynchronous cleanup process for all protection entries.
   * The process iterates through locations in batches to avoid server lag,
   * checking if the block at the protected location matches the registered material.
   *
   * @param p the player performing the cleanup command to receive progress updates
   */
  public void cleanUpProtections(@NonNull Player p) {
    HashMap<Location, ProtectionEntry> protectionEntryList = new HashMap<>(
        serviceContext.getProtectionService().getAllProtectionEntries()
    );

    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_START,
            protectionEntryList.size()));

    List<Location> locations = new ArrayList<>(protectionEntryList.keySet());
    int[] index = {0};
    HashMap<Location, ProtectionEntry> removeMap = new HashMap<>();
    int total = locations.size();

    serviceContext.getSchedulerService().runTaskTimer(
        task -> {
          int batchSize = 5;
          int processed = 0;

          while (index[0] < locations.size() && processed < batchSize) {
            Location l = locations.get(index[0]);
            ProtectionEntry pe = protectionEntryList.get(l);

            if (l.getWorld() == null) {
              index[0]++;
              processed++;
              continue;
            }

            if (!l.getChunk().isLoaded()) {
              l.getChunk().load();
            }

            if (!l.getBlock().getType().equals(Material.getMaterial(pe.getMaterialName()))) {
              removeMap.put(l, pe);
              p.sendMessage(
                  serviceContext.getTranslationService()
                      .getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS,
                          pe.getId(), pe.getMaterialName(), l.getBlock().getType().name()));
              serviceContext.getProtectionService().deleteProtectionAndRemoveFromRegistry(pe);
            }

            index[0]++;
            processed++;
          }

          int percent = total == 0 ? 100 : (int) Math.round((index[0] / (double) total) * 100);
          p.sendMessage(
              serviceContext.getTranslationService().getWithPrefix(
                  MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_PERCENTAGE,
                  index[0], total, percent));

          if (index[0] >= locations.size()) {
            task.cancel();

            if (removeMap.isEmpty()) {
              p.sendMessage(
                  serviceContext.getTranslationService().getWithPrefix(
                      MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE));
            } else {
              p.sendMessage(serviceContext.getTranslationService().getWithPrefix(
                  MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP,
                  removeMap.size()));
              for (Location l : removeMap.keySet()) {
                serviceContext.getProtectionService().removeProtectionEntry(l);
              }
              p.sendMessage(
                  serviceContext.getTranslationService()
                      .getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_END,
                          serviceContext.getProtectionService().getAllProtectionEntries()
                              .size()));
              p.sendMessage(
                  serviceContext.getTranslationService()
                      .getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_PROTECTIONS_END,
                          removeMap.size()));
            }
          }
        },
        0L,
        300L
    );

    int removedCount = serviceContext.getProtectionService()
        .removeOutdatedProtectionsFromDatabaseAndRegistry();
    if (removedCount > 0) {
      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(
          MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_OUTDATED_REMOVED,
          removedCount));
    }
  }
}
