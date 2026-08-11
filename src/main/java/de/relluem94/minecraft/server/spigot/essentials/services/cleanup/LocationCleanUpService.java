package de.relluem94.minecraft.server.spigot.essentials.services.cleanup;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.repositories.LocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class LocationCleanUpService {

  private final TranslationService translationService;
  private final LocationRepository locationRepository;

  public LocationCleanUpService(TranslationService translationService, LocationRepository locationRepository) {
    this.translationService = translationService;
    this.locationRepository = locationRepository;
  }

  public void cleanUpLocations(@NonNull Player p) {
    int deletedCount = locationRepository.removeOutdatedLocations();
    p.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END,
            deletedCount));
  }
}
