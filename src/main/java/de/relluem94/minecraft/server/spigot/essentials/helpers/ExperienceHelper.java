package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import org.bukkit.entity.Player;

/**
 * Utility class providing helper methods for calculating and manipulating player
 * experience points and levels.
 */
public class ExperienceHelper {

  private ExperienceHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Calculates the total experience points required to reach the given level from zero.
   *
   * @param level the target level for which the total experience is calculated
   * @return the total experience points required to reach the specified level
   */
  public static int getTotalExperience(int level) {
    int xp = 0;

    if (level >= 0 && level <= 15) {
      xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
    } else if (level > 15 && level <= 30) {
      xp = (int) Math.round(2.5 * Math.pow(level, 2) - 40.5 * level + 360);
    } else if (level > 30) {
      xp = (int) Math.round(4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
    }
    return xp;
  }

  /**
   * Calculates the total accumulated experience points of the given player,
   * including both their current level progress and the experience within the current level.
   *
   * @param player the player whose total experience is calculated
   * @return the total experience points the player currently has
   */
  public static int getTotalExperience(Player player) {
    return Math.round(player.getExp() * player.getExpToLevel()) + getTotalExperience(
        player.getLevel());
  }

  /**
   * Sets the total experience points of the given player to the specified amount,
   * automatically deriving and applying the corresponding level and remaining experience progress.
   *
   * @param player the player whose total experience is set
   * @param amount the total experience points to assign to the player
   */
  public static void setTotalExperience(Player player, int amount) {
    int level;
    int xp;
    float a = 1;
    float b = 0;
    float c = -amount;

    if (amount > getTotalExperience(0) && amount <= getTotalExperience(15)) {
      b = 6;
    } else if (amount > getTotalExperience(15) && amount <= getTotalExperience(30)) {
      a = 2.5f;
      b = -40.5f;
      c += 360;
    } else if (amount > getTotalExperience(30)) {
      a = 4.5f;
      b = -162.5f;
      c += 2220;
    }

    level = (int) Math.floor((-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a));
    xp = amount - getTotalExperience(level);
    player.setLevel(level);
    player.setExp(0);
    player.giveExp(xp);
  }
}