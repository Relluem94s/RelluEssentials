package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class MessageService {

  private final TranslationService translationService;

  public MessageService(TranslationService translationService){
    this.translationService = translationService;
  }

  /**
   *
   * @param l Location
   * @return String with Location
   */
  public @NotNull String locationToString(@NotNull Location l) {
    return locationToString(l, false);
  }


  /**
   *
   * @param l     Location
   * @param round boolean should round the number
   * @return String with Location
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

  public static @NotNull String firstCharToUpper(@NotNull String s) {
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }

}
