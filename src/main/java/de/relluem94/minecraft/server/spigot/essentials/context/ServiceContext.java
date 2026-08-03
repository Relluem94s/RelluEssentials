package de.relluem94.minecraft.server.spigot.essentials.context;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceContext {

  private final TranslationService translationService;
  private GroupService groupService;
  private PlayerService playerService;
  private CommandManager commandManager;
  private BuyBackService buyBackService;
  private NpcService npcService;
  private GroupRegistry groupRegistry;
  private DatabaseHelper databaseHelper;
  private ProtectionRegistry protectionRegistry;

  public ServiceContext(RelluEssentials plugin) {
    this.groupService = plugin.getGroupService();
    this.playerService = plugin.getPlayerService();
    this.commandManager = plugin.getCommandManager();
    this.buyBackService = plugin.getBuyBackService();
    this.npcService = plugin.getNpcService();
    this.groupRegistry = plugin.getGroupRegistry();
    this.translationService = plugin.getTranslationService();
    this.databaseHelper = plugin.getDatabaseHelper();
    this.protectionRegistry = plugin.getProtectionRegistry();
  }
}