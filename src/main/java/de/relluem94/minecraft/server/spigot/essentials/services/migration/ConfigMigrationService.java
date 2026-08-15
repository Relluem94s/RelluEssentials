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

@ApiStatus.Internal
@RequiredArgsConstructor
public class ConfigMigrationService {

  private final File dataFolder;
  private final ServiceContext serviceContext;

  public boolean legacyConfigExists(String name) {
    return resolveLegacyConfigFile(name).exists();
  }

  public List<PlayerEntry> getPlayers(String name) {
    YamlConfiguration config = loadConfig(name);
    List<PlayerEntry> list = new ArrayList<>();
    ConfigurationSection cs = config.getConfigurationSection("player");

    for (String uuid : Objects.requireNonNull(cs).getKeys(false)) {
      ConfigurationSection player = cs.getConfigurationSection(uuid);

      String groupName = Objects.requireNonNull(Objects.requireNonNull(player).getString("group")).toLowerCase();
      boolean fly = player.getBoolean("fly");
      boolean afk = player.getBoolean("afk");
      String customname = player.getString("customname");

      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          "Found Player: " + uuid + " customname:" + customname + " afk:" + afk + " fly:" + fly + " group:" + groupName);

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

  public List<LocationEntry> getHomes(String name, @NotNull PlayerEntry p) {
    YamlConfiguration config = loadConfig(name);
    List<LocationEntry> list = new ArrayList<>();
    ConfigurationSection homes = config.getConfigurationSection("player." + p.getUuid() + ".home");

    for (String home : Objects.requireNonNull(homes).getKeys(false)) {
      ConfigurationSection h = homes.getConfigurationSection(home);
      if (h == null) {
        continue;
      }

      float x = (float) h.getDouble("x");
      float y = (float) h.getDouble("y");
      float z = (float) h.getDouble("z");
      float yaw = (float) h.getDouble("yaw");
      float pitch = (float) h.getDouble("pitch");
      int type = home.equals("death") ? 2 : 1;
      String worldName = h.getString("world");
      World world = Bukkit.getServer().getWorld(Objects.requireNonNull(worldName));

      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          "Found Home: " + home + " x:" + x + " y:" + y + " z:" + z + " yaw:" + yaw + " pitch:" + pitch + " world:" + world);

      LocationEntry l = new LocationEntry();
      l.setLocation(new Location(world, x, y, z, yaw, pitch));
      l.setLocationName(home);
      l.setPlayerId(p.getId());
      LocationTypeEntry lt = new LocationTypeEntry();
      lt.setId(type);
      l.setLocationType(lt);

      list.add(l);
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