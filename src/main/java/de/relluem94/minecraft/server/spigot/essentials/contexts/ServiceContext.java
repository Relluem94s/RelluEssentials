package de.relluem94.minecraft.server.spigot.essentials.contexts;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WarpRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.BackService;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.BlockDropService;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.ChatService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds references to some active services and managers used across the plugin.
 * Constructed from a {@link RelluEssentials} plugin instance.
 */
@Setter
@Getter
public class ServiceContext {

  private TranslationService translationService;
  private GroupService groupService;
  private PlayerService playerService;
  private CommandManager commandManager; // TODO SERVICE
  private BuyBackService buyBackService;
  private NpcService npcService;
  private SchedulerService schedulerService;
  private DatabaseHelper databaseHelper;
  private ProtectionService protectionService;
  private UndoHistoryService undoHistoryService;
  private SelectionService selectionService;
  private BagService bagService;
  private MessageService messageService;
  private ChatService chatService;
  private BankService bankService;
  private BackService backService;
  private TeleportService teleportService;
  private ProtectionActionService protectionActionService;
  private BlockDropService blockDropService;
  private WarpRepository warpRepository; // TODO SERVICE
  private TraderNpcRegistry traderNpcRegistry; // TODO SERVICE

  private BankerNpc bankerNpc;

  /**
   * Creates a new ServiceContext.
   *
   */
  public ServiceContext() {}
}