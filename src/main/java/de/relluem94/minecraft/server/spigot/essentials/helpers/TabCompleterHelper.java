package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.wrappers.CommandWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

  public static @NotNull List<String> getBags(Player p) {
    PlayerEntry pe = RelluEssentials.getInstance().getServiceContext().getPlayerService().getPlayerEntry(p);

    return RelluEssentials.getInstance().getServiceContext().getBagService().getBags(pe.getId())
        .stream()
        .map(bag -> bag.getBagType().getName().toLowerCase())
        .collect(Collectors.toList());
  }

  public static @NotNull List<String> getWorlds() {
    List<String> worldNames = new ArrayList<>();

    for (World world : Bukkit.getWorlds()) {
      worldNames.add(world.getName());
    }

    return worldNames;
  }

  public static @NotNull List<String> getHomes(Player p) {
    PlayerEntry pe = RelluEssentials.getInstance().getServiceContext().getPlayerService().getPlayerEntry(p);
    List<String> homes = new ArrayList<>();

    for (LocationEntry le : pe.getHomes()) {
      homes.add(le.getLocationName());
    }

    for (LocationEntry le : pe.getDeaths()) {
      homes.add(le.getLocationName());
    }

    return homes;
  }

  public static @NotNull List<String> getGroups(List<GroupEntry> groupEntryList) {
    List<String> groups = new ArrayList<>();

    for (GroupEntry ge : groupEntryList) {
      groups.add(ge.getName());
    }

    return groups;
  }

  public static List<String> getPluginCommands(List<CommandWrapper> commandList) {
    return commandList.stream()
        .map(CommandWrapper::getCommandName)
        .collect(Collectors.toList());
  }

  public static @NotNull List<String> getWarps(World world) {
    List<String> warps = new ArrayList<>();

    for (LocationEntry le : RelluEssentials.getInstance().getServiceContext().getWarpRepository().findByWorld(world)) {
      warps.add(le.getLocationName());
    }

    return warps;
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
