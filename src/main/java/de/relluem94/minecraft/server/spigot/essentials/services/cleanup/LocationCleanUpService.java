package de.relluem94.minecraft.server.spigot.essentials.services.cleanup;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.repositories.LocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Service responsible for cleaning up outdated location data from the repository.
 */
public class LocationCleanUpService {

  private final TranslationService translationService;
  private final LocationRepository locationRepository;

  /**
   * Constructs a new LocationCleanUpService.
   *
   * @param translationService the service used for translating messages
   * @param locationRepository the repository used to manage location data
   */
  public LocationCleanUpService(TranslationService translationService,
      LocationRepository locationRepository) {
    this.translationService = translationService;
    this.locationRepository = locationRepository;
  }

  /**
   * Removes all outdated locations from the repository and notifies the player of the result.
   *
   * @param p the player executing the cleanup process
   */
  public void cleanUpLocations(@NonNull Player p) {
    int deletedCount = locationRepository.removeOutdatedLocations();
    p.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END,
            deletedCount));
  }
}
