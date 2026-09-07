package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class providing helper methods for tab completion in commands. Supplies lists of valid
 * values for various command arguments such as players, worlds, materials, weather types, and
 * protection flags.
 *
 * @author Relluem94
 */
public class TabCompleterHelper {

  private TabCompleterHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Returns a list of names of all currently online players.
   *
   * @return a {@link List} of online player names
   * @deprecated Use the Bukkit API directly to retrieve online players
   */
  @Deprecated
  public static @NotNull List<String> getOnlinePlayers() {
    List<String> playerList = new ArrayList<>();
    for (Player p : Bukkit.getOnlinePlayers()) {
      playerList.add(p.getName());
    }

    return playerList;
  }

  /**
   * Returns a list of all available protection flag names.
   *
   * @return a {@link List} of protection flag names derived from {@link ProtectionFlags}
   */
  public static @NotNull List<String> getProtectionFlags() {
    List<String> protectionFlagList = new ArrayList<>();
    for (ProtectionFlags protectionFlag : ProtectionFlags.values()) {
      protectionFlagList.add(protectionFlag.toString());
    }

    return protectionFlagList;
  }

  /**
   * Returns a list of command names derived from the given array of {@link CommandsEnum} values.
   *
   * @param commandsEnums the array of {@link CommandsEnum} entries to extract names from
   * @return a {@link List} of command names
   */
  public static @NotNull List<String> getCommands(CommandsEnum @NotNull [] commandsEnums) {
    List<String> commands = new ArrayList<>();
    for (CommandsEnum command : commandsEnums) {
      commands.add(command.getName());
    }

    return commands;
  }

  /**
   * Returns a list of names of all worlds currently loaded on the server.
   *
   * @return a {@link List} of world names
   * @deprecated Use the Bukkit API directly to retrieve loaded worlds
   */
  @Deprecated
  public static @NotNull List<String> getWorlds() {
    List<String> worldNames = new ArrayList<>();

    for (World world : Bukkit.getWorlds()) {
      worldNames.add(world.getName());
    }

    return worldNames;
  }

  /**
   * Returns a list of group names derived from the given list of {@link GroupEntry} objects.
   *
   * @param groupEntryList the list of {@link GroupEntry} objects to extract names from
   * @return a {@link List} of group names
   */
  public static @NotNull List<String> getGroups(List<GroupEntry> groupEntryList) {
    List<String> groups = new ArrayList<>();

    for (GroupEntry ge : groupEntryList) {
      groups.add(ge.getName());
    }

    return groups;
  }

  /**
   * Returns a list of all available world type names.
   *
   * @return a {@link List} of world type names derived from {@link WorldType}
   */
  public static @NotNull List<String> getWorldTypes() {
    List<String> worldTypes = new ArrayList<>();

    for (WorldType worldType : WorldType.values()) {
      worldTypes.add(worldType.getName());
    }

    return worldTypes;
  }

  /**
   * Returns a list of all available world environment type names.
   *
   * @return a {@link List} of environment type names derived from
   *     {@link World.Environment}
   */
  public static @NotNull List<String> getWorldEnvironmentTypes() {
    List<String> worldTypes = new ArrayList<>();

    for (World.Environment worldEnvironmentType : World.Environment.values()) {
      worldTypes.add(worldEnvironmentType.name());
    }

    return worldTypes;
  }

  /**
   * Returns a list of non-legacy, solid block material names, optionally filtered by a search
   * string.
   *
   * @param filter a case-insensitive substring to filter material names by, or {@code null} to
   *               return all matching materials
   * @return a {@link List} of material names that are solid blocks and match the given filter
   */
  public static @NotNull List<String> getMaterials(@Nullable String filter) {
    List<String> materials = new ArrayList<>();

    for (Material material : Material.values()) {
      if (material.name().startsWith("LEGACY")) {
        continue;
      }

      String materialName = material.name();

      if (material.isBlock() && material.isSolid()) {
        if (filter == null || materialName.toLowerCase().contains(filter.toLowerCase())) {
          materials.add(materialName);
        }
      }
    }

    return materials;
  }

  /**
   * Returns a list of all available weather type names.
   *
   * @return a {@link List} of weather type names derived from {@link WeatherType}
   */
  public static @NotNull List<String> getWeatherTypes() {
    List<String> weatherTypes = new ArrayList<>();

    for (WeatherType weatherType : WeatherType.values()) {
      weatherTypes.add(weatherType.name());
    }

    return weatherTypes;
  }
}
