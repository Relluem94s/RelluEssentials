package de.relluem94.minecraft.server.spigot.essentials.contexts;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.BackService;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
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

  private final TranslationService translationService;
  private GroupService groupService;
  private PlayerService playerService;
  private CommandManager commandManager;
  private BuyBackService buyBackService;
  private NpcService npcService;
  private GroupRegistry groupRegistry;
  private SchedulerService schedulerService;
  private DatabaseHelper databaseHelper;
  private ProtectionRegistry protectionRegistry;
  private UndoHistoryService undoHistoryService;
  private SelectionService selectionService;
  private BagService bagService;
  private MessageService messageService;
  private BankService bankService;
  private BackService backService;
  private TeleportService teleportService;
  private ProtectionActionService protectionActionService;

  private BankerNpc bankerNpc;

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
    this.translationService = plugin.getTranslationService();
    this.databaseHelper = plugin.getDatabaseHelper();
    this.protectionRegistry = plugin.getProtectionRegistry();
    this.undoHistoryService = plugin.getUndoHistoryService();
  }
}