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
 *
 * @author Relluem94
 */
public class TabCompleterHelper {

  private TabCompleterHelper() {
    throw new IllegalStateException(
        Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static @NotNull List<String> getOnlinePlayers() {
    List<String> playerList = new ArrayList<>();
    for (Player p : Bukkit.getOnlinePlayers()) {
      playerList.add(p.getName());
    }

    return playerList;
  }

  public static @NotNull List<String> getProtectionFlags() {
    List<String> protectionFlagList = new ArrayList<>();
    for (ProtectionFlags protectionFlag : ProtectionFlags.values()) {
      protectionFlagList.add(protectionFlag.toString());
    }

    return protectionFlagList;
  }

  public static @NotNull List<String> getCommands(CommandsEnum @NotNull [] commandsEnums) {
    List<String> commands = new ArrayList<>();
    for (CommandsEnum command : commandsEnums) {
      commands.add(command.getName());
    }

    return commands;
  }

  public static @NotNull List<String> getWorlds() {
    List<String> worldNames = new ArrayList<>();

    for (World world : Bukkit.getWorlds()) {
      worldNames.add(world.getName());
    }

    return worldNames;
  }

  public static @NotNull List<String> getGroups(List<GroupEntry> groupEntryList) {
    List<String> groups = new ArrayList<>();

    for (GroupEntry ge : groupEntryList) {
      groups.add(ge.getName());
    }

    return groups;
  }

  public static @NotNull List<String> getWorldTypes() {
    List<String> worldTypes = new ArrayList<>();

    for (WorldType worldType : WorldType.values()) {
      worldTypes.add(worldType.getName());
    }

    return worldTypes;
  }

  public static @NotNull List<String> getWorldEnvironmentTypes() {
    List<String> worldTypes = new ArrayList<>();

    for (World.Environment worldEnvironmentType : World.Environment.values()) {
      worldTypes.add(worldEnvironmentType.name());
    }

    return worldTypes;
  }

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

  public static @NotNull List<String> getWeatherTypes() {
    List<String> weatherTypes = new ArrayList<>();

    for (WeatherType weatherType : WeatherType.values()) {
      weatherTypes.add(weatherType.name());
    }

    return weatherTypes;
  }
}
