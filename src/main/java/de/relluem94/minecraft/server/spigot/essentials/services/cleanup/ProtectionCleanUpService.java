package de.relluem94.minecraft.server.spigot.essentials.services.cleanup;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ProtectionCleanUpService {

  private final TranslationService translationService;
  private final DatabaseHelper databaseHelper;
  private final ProtectionRegistry protectionRegistry;


  public ProtectionCleanUpService(TranslationService translationService, ProtectionRegistry protectionRegistry, DatabaseHelper databaseHelper) {
    this.translationService = translationService;
    this.databaseHelper = databaseHelper;
    this.protectionRegistry = protectionRegistry;
  }

  public void cleanUpProtections(@NonNull Player p) {
    HashMap<Location, ProtectionEntry> protectionEntryList = new HashMap<>(
        protectionRegistry.getProtectionEntryList()
    );

    p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_START,
        protectionEntryList.size()));

    List<Location> locations = new ArrayList<>(protectionEntryList.keySet());
    int[] index = {0};
    HashMap<Location, ProtectionEntry> removeMap = new HashMap<>();
    int total = locations.size();

    Bukkit.getScheduler().runTaskTimer(
        RelluEssentials.getInstance(),
        task -> {
          int batchSize = 5;
          int processed = 0;

          while (index[0] < locations.size() && processed < batchSize) {
            Location l = locations.get(index[0]);
            ProtectionEntry pe = protectionEntryList.get(l);

            if (!l.getChunk().isLoaded()) {
              l.getChunk().load();
            }

            if (!l.getBlock().getType().equals(Material.getMaterial(pe.getMaterialName()))) {
              removeMap.put(l, pe);
              p.sendMessage(
                  translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS,
                      pe.getId(), pe.getMaterialName(), l.getBlock().getType().name()));
              databaseHelper.deleteProtection(pe);
            }

            index[0]++;
            processed++;
          }

          int percent = (int) Math.round((index[0] / (double) total) * 100);
          p.sendMessage(
              translationService.getWithPrefix(
                  MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_PERCENTAGE,
                  index[0], total, percent));

          if (index[0] >= locations.size()) {
            task.cancel();

            if (removeMap.isEmpty()) {
              p.sendMessage(
                  translationService.getWithPrefix(
                      MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE));
            } else {
              p.sendMessage(translationService.getWithPrefix(
                  MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP,
                  removeMap.size()));
              for (Location l : removeMap.keySet()) {
                protectionRegistry.removeProtectionEntry(l);
              }
              p.sendMessage(
                  translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_END,
                      protectionRegistry.getProtectionEntryList()
                          .size()));
            }
          }
        },
        0L,
        300L
    );

    int deleted = databaseHelper.cleanupProtections();
    p.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_PROTECTIONS_END,
            deleted));
  }
}
