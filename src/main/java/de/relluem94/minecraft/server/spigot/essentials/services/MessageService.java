package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Service responsible for handling message formatting and translations.
 */
public class MessageService {

  private final TranslationService translationService;

  /**
   * Creates a new instance of MessageService.
   *
   * @param translationService the service used for fetching translated strings
   */
  public MessageService(TranslationService translationService){
    this.translationService = translationService;
  }

  /**
   * Converts a location to a human-readable string without rounding coordinates.
   *
   * @param l the location to convert
   * @return a string representation of the location
   */
  public @NotNull String locationToString(@NotNull Location l) {
    return locationToString(l, false);
  }

  /**
   * Converts a location to a human-readable string.
   *
   * @param l     the location to convert
   * @param round whether the coordinates should be rounded
   * @return a string representation of the location
   */
  public @NotNull String locationToString(@NotNull Location l, boolean round) {
    if (round) {
      return locationToString(l);
    }

    World world = l.getWorld();
    if (world == null) {
      return translationService.get(MessageKey.COMMAND_WHERE_STRING, l.getX(), l.getY(), l.getZ(),
          "null");
    }
    return translationService.get(MessageKey.COMMAND_WHERE_STRING, l.getX(), l.getY(), l.getZ(),
        world.getName());
  }
}
