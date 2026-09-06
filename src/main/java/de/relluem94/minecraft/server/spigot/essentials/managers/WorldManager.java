package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.plugin.Plugin;

/**
 * Manages the lifecycle of Minecraft worlds defined in the world group configuration. Handles
 * creation, loading, and unloading of worlds during plugin enable and disable phases.
 */
public class WorldManager implements Enable, Disable {

  private final Random random = new Random();
  private ServiceContext serviceContext;

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    if (relluEssentialsPlugin.isUnitTest()) {
      return;
    }
    serviceContext = relluEssentialsPlugin.getServiceContext();

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = serviceContext.getWorldGroupService()
        .getWorldsMap();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        PLUGIN_COLOR_COMMAND + "Worlds Size: " + worldsMap.size());

    for (WorldGroupEntry wge : worldsMap.keySet()) {
      if (wge == null) {
        continue;
      }
      for (WorldEntry we : worldsMap.get(wge)) {
        if (we != null && !WorldHelper.worldExists(we.getName())) {
          createWorld(we);
          continue;
        }

        if (we == null) {
          continue;
        }

        if (!WorldHelper.worldExists(we.getName())) {
          createWorld(we);
        } else if (plugin.getServer().getWorld(we.getName()) == null) {
          WorldHelper.loadWorld(we.getName());
          setStandardGameRules(we.getName());
        }
      }
    }
  }

  @Override
  public void disable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    if (relluEssentialsPlugin.isUnitTest()) {
      return;
    }

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = serviceContext.getWorldGroupService()
        .getWorldsMap();
    for (WorldGroupEntry wge : worldsMap.keySet()) {
      if (wge == null) {
        continue;
      }

      for (WorldEntry we : worldsMap.get(wge)) {
        try {
          if (we == null) {
            return;
          }

          WorldHelper.unloadWorld(we.getName(), true);
        } catch (WorldNotLoadedException e) {
          Logger.getLogger(WorldManager.class.getName()).log(Level.WARNING, e.getMessage());
        }
      }
    }
  }

  private void createWorld(WorldEntry we) {
    WorldType type = WorldType.NORMAL;
    World.Environment worldEnvironment = getEnvironment(we.getName());

    if (we.getName().equals("lobby")) {
      WorldHelper.createWorld(we.getName(), type, worldEnvironment, false, 6203818585396731238L);
      setStandardGameRules(we.getName());
      setLobbySpawnLocation(we.getName());
    } else {
      WorldHelper.createWorld(we.getName(), type, worldEnvironment, false);
    }
  }

  private World.Environment getEnvironment(String name) {
    if (name.endsWith("_nether")) {
      return World.Environment.NETHER;
    }

    if (name.endsWith("_the_end")) {
      return World.Environment.THE_END;
    }

    if (name.endsWith("_custom")) {
      return World.Environment.CUSTOM;
    }

    return World.Environment.NORMAL;
  }

  private void setLobbySpawnLocation(String name) {
    World world = serviceContext.getPluginMetadataService().getPlugin().getServer().getWorld(name);
    int random = this.random.nextInt(9 + 1 - 1) + 1;
    if (world == null) {
      return;
    }

    switch (random) {
      case 1:
        world.setSpawnLocation(140, 143, 188);
        break;
      case 2:
        world.setSpawnLocation(-226, 115, -5777);
        break;
      case 3:
        world.setSpawnLocation(718, 136, -4215);
        break;
      case 4:
        world.setSpawnLocation(497, 68, -2800);
        break;
      default:
        world.setSpawnLocation(141, 143, 188);
        break;
    }
  }

  private void setStandardGameRules(String name) {
    World lobbyWorld = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getWorld(name);

    if (lobbyWorld == null) {
      return;
    }

    lobbyWorld.setGameRule(GameRule.FIRE_DAMAGE, false);
    lobbyWorld.setGameRule(GameRule.SPAWN_MOBS, false);
    lobbyWorld.setGameRule(GameRule.MOB_GRIEFING, false);
    lobbyWorld.setGameRule(GameRule.ADVANCE_WEATHER, false);
  }
}