package de.relluem94.minecraft.server.spigot.essentials.services.migration;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Service responsible for migrating legacy configuration files to the new data format.
 */
@ApiStatus.Internal
@RequiredArgsConstructor
public class ConfigMigrationService {

  private final File dataFolder;
  private final ServiceContext serviceContext;

  /**
   * Checks if a legacy configuration file exists for the given name.
   *
   * @param name The name of the configuration file without the extension.
   * @return {@code true} if the file exists, {@code false} otherwise.
   */
  public boolean legacyConfigExists(String name) {
    return resolveLegacyConfigFile(name).exists();
  }

  /**
   * Retrieves the list of players from the specified legacy configuration file.
   *
   * @param name The name of the configuration file.
   * @return A list of {@link PlayerEntry} objects found in the configuration.
   */
  public List<PlayerEntry> getPlayers(String name) {
    YamlConfiguration config = loadConfig(name);
    List<PlayerEntry> list = new ArrayList<>();
    ConfigurationSection cs = config.getConfigurationSection("player");

    for (String uuid : Objects.requireNonNull(cs).getKeys(false)) {
      ConfigurationSection player = cs.getConfigurationSection(uuid);

      String groupName = Objects.requireNonNull(Objects.requireNonNull(player)
          .getString("group")).toLowerCase();
      boolean fly = player.getBoolean("fly");
      boolean afk = player.getBoolean("afk");
      String customname = player.getString("customname");

      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          "Found Player: " + uuid + " customname:" + customname + " afk:" + afk
              + " fly:" + fly + " group:" + groupName);

      PlayerEntry p = new PlayerEntry();
      p.setGroup(serviceContext.getGroupService().resolveGroupWithFallback(groupName));
      p.setAfk(afk);
      p.setFlying(fly);
      p.setCreatedBy(1);
      p.setCustomName(customname);
      p.setUuid(uuid);

      list.add(p);
    }
    return list;
  }

  /**
   * Retrieves the list of homes for a specific player from the specified legacy configuration file.
   *
   * @param name The name of the configuration file.
   * @param p    The player whose homes are being retrieved.
   * @return A list of {@link LocationEntry} objects representing the player's homes.
   */
  public List<LocationEntry> getHomes(String name, @NotNull PlayerEntry p) {
    YamlConfiguration config = loadConfig(name);
    List<LocationEntry> list = new ArrayList<>();
    ConfigurationSection homes = config.getConfigurationSection("player." + p.getUuid() + ".home");

    if (homes == null) {
      return list;
    }

    for (String homeName : homes.getKeys(false)) {
      ConfigurationSection homeSection = homes.getConfigurationSection(homeName);
      if (homeSection == null) {
        continue;
      }

      String worldName = homeSection.getString("world");
      World world = worldName != null ? Bukkit.getServer().getWorld(worldName) : null;

      if (world == null) {
        continue;
      }

      float x = (float) homeSection.getDouble("x");
      float y = (float) homeSection.getDouble("y");
      float z = (float) homeSection.getDouble("z");
      float yaw = (float) homeSection.getDouble("yaw");
      float pitch = (float) homeSection.getDouble("pitch");

      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          "Found Home: " + homeName + " x:" + x + " y:" + y + " z:" + z
              + " yaw:" + yaw + " pitch:" + pitch + " world:" + world);

      LocationEntry locationEntry = new LocationEntry();
      locationEntry.setLocation(new Location(world, x, y, z, yaw, pitch));
      locationEntry.setLocationName(homeName);
      locationEntry.setPlayerId(p.getId());

      LocationTypeEntry typeEntry = new LocationTypeEntry();
      int typeId = homeName.equals("death") ? 2 : 1;
      typeEntry.setId(typeId);
      locationEntry.setLocationType(typeEntry);

      list.add(locationEntry);
    }
    return list;
  }

  private File resolveLegacyConfigFile(String name) {
    return new File(dataFolder, name + ".yml");
  }

  private YamlConfiguration loadConfig(String name) {
    return YamlConfiguration.loadConfiguration(resolveLegacyConfigFile(name));
  }
}