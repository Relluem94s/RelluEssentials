package de.relluem94.minecraft.server.spigot.essentials.contexts;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.services.BackService;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.BlockDropService;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.ChatService;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.CommandService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationTypeService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcDialogueProgressService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginInformationService;
import de.relluem94.minecraft.server.spigot.essentials.services.PositionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TraderNpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.minecraft.server.spigot.essentials.services.WarpService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.cleanup.LocationCleanUpService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds references to some active services and managers used across the plugin.
 * Constructed from a {@link RelluEssentials} plugin instance.
 */
@Setter
@Getter
@NoArgsConstructor
public class ServiceContext {
  /* TEMP DatabaseHelper*/
  private DatabaseHelper databaseHelper;

  /* Services */
  private TranslationService translationService;
  private GroupService groupService;
  private PlayerService playerService;
  private CommandService commandService;
  private BuyBackService buyBackService;
  private NpcService npcService;
  private SchedulerService schedulerService;
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
  private WarpService warpService;
  private NpcDialogueProgressService npcDialogueProgressService;
  private TraderNpcService traderNpcService;
  private PositionService positionService;
  private SettingService settingService;
  private WorldGroupService worldGroupService;
  private ClipboardService clipboardService;
  private PluginInformationService pluginInformationService;
  private LocationTypeService locationTypeService;
  private LocationCleanUpService locationCleanUpService;
}