package de.relluem94.minecraft.server.spigot.essentials.services.cleanup;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class LocationCleanUpService {

  private final TranslationService translationService;
  private final DatabaseHelper databaseHelper;

  public LocationCleanUpService(TranslationService translationService, DatabaseHelper databaseHelper) {
    this.translationService = translationService;
    this.databaseHelper = databaseHelper;
  }

  public void cleanUpLocations(@NonNull Player p) {
    int deleted = databaseHelper.cleanupLocations();
    p.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END,
            deleted));
  }
}
