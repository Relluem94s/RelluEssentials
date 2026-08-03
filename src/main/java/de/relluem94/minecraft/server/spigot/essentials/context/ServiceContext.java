package de.relluem94.minecraft.server.spigot.essentials.context;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

/**
 * Holds references to some active services and managers used across the plugin.
 * Constructed from a {@link RelluEssentials} plugin instance.
 */
@Setter
@Getter
public class ServiceContext {

  private GroupService groupService;
  private PlayerService playerService;
  private CommandManager commandManager;
  private BuyBackService buyBackService;
  private NpcService npcService;
  private GroupRegistry groupRegistry;
  private SchedulerService schedulerService;

  /**
   * Creates a new ServiceContext from the given plugin instance.
   *
   * @param plugin the plugin instance to retrieve services from
   */
  public ServiceContext(@NonNull RelluEssentials plugin) {
    this.groupService = plugin.getGroupService();
    this.playerService = plugin.getPlayerService();
    this.commandManager = plugin.getCommandManager();
    this.buyBackService = plugin.getBuyBackService();
    this.npcService = plugin.getNpcService();
    this.groupRegistry = plugin.getGroupRegistry();
    this.schedulerService = plugin.getSchedulerService();
  }
}