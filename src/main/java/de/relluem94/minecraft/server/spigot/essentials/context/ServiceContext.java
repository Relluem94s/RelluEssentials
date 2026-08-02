package de.relluem94.minecraft.server.spigot.essentials.context;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import lombok.Getter;

@Getter
public class ServiceContext {

  private final GroupService groupService;
  private final PlayerService playerService;
  private final CommandManager commandManager;
  private final BuyBackService buyBackService;
  private final NpcService npcService;
  private final GroupRegistry groupRegistry;

  public ServiceContext(RelluEssentials plugin) {
    this.groupService = plugin.getGroupService();
    this.playerService = plugin.getPlayerService();
    this.commandManager = plugin.getCommandManager();
    this.buyBackService = plugin.getBuyBackService();
    this.npcService = plugin.getNpcService();
    this.groupRegistry = plugin.getGroupRegistry();
  }
}