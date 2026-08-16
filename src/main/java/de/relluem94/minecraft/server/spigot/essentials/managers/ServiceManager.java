package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.PersistenceContext;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcSpawner;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcValidator;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BagSalesmanNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BeekeeperNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.EnchanterNpc;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.NpcDialogueRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PositionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ReplyRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingPlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BackLocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagTypeRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BankRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BuyBackRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.CropRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.DropRuleRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.LocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.NpcRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PlayerRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.ProtectionRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingPlayerRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.TraderNpcRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.UndoHistoryRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.BackService;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.BlockDropService;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.ChatService;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.DeathChestService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcDialogueProgressService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginManagerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PositionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingPlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TraderNpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.minecraft.server.spigot.essentials.services.WarpService;
import de.relluem94.minecraft.server.spigot.essentials.services.cleanup.LocationCleanUpService;
import de.relluem94.minecraft.server.spigot.essentials.services.cleanup.ProtectionCleanUpService;
import org.bukkit.plugin.Plugin;

public class ServiceManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentials = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentials.getServiceContext();
    PersistenceContext persistenceContext = relluEssentials.getPersistenceContext();

    /* Services */
    serviceContext.setPluginManagerService(new PluginManagerService(plugin));
    serviceContext.setClipboardService(new ClipboardService());
    DropRuleRepository dropRuleRepository = new DropRuleRepository(persistenceContext.getDropDao());
    CropRepository cropRepository = new CropRepository(persistenceContext.getCropDao());
    BlockDropService blockDropService = new BlockDropService(dropRuleRepository, cropRepository);
    serviceContext.setBlockDropService(blockDropService);

    ProtectionRepository protectionRepository = new ProtectionRepository(
        persistenceContext.getProtectionDao(), persistenceContext.getLocationDao());

    ProtectionService protectionService = new ProtectionService(
        protectionRepository.loadAllLocks(),
        protectionRepository.loadAll(),
        protectionRepository,
        serviceContext);
    serviceContext.setProtectionService(protectionService);

    TraderNpcRepository traderNpcRepository = new TraderNpcRepository(
        persistenceContext.getTraderNpcDao());
    TraderNpcRegistry traderNpcRegistry = new TraderNpcRegistry(
        serviceContext.getTranslationService());
    BankerNpc bankerNpc = new BankerNpc(serviceContext);
    traderNpcRegistry.addNPC(new BagSalesmanNpc(serviceContext));
    traderNpcRegistry.addNPC(bankerNpc);
    traderNpcRegistry.addNPC(new BeekeeperNpc(serviceContext));
    traderNpcRegistry.addNPC(new EnchanterNpc(serviceContext));
    TraderNpcService traderNpcService = new TraderNpcService(traderNpcRegistry, traderNpcRepository,
        bankerNpc);
    traderNpcService.loadAndInitialiseNpcs();
    serviceContext.setTraderNpcService(traderNpcService);

    serviceContext.setWarpService(new WarpService(serviceContext));

    SchedulerService schedulerService = new SchedulerService(relluEssentials);
    serviceContext.setSchedulerService(schedulerService);
    UndoHistoryRepository undoHistoryRepository = new UndoHistoryRepository();
    UndoHistoryService undoHistoryService = new UndoHistoryService(undoHistoryRepository);
    serviceContext.setUndoHistoryService(undoHistoryService);
    SelectionService selectionService = new SelectionService(serviceContext);
    serviceContext.setSelectionService(selectionService);
    GroupRepository groupRepository = new GroupRepository(persistenceContext.getGroupDao());
    GroupRegistry groupRegistry = new GroupRegistry(groupRepository);
    GroupService groupService = new GroupService(groupRegistry, groupRepository);
    serviceContext.setGroupService(groupService);

    LocationRepository locationRepository = new LocationRepository(
        persistenceContext.getLocationDao());

    serviceContext.setLocationService(
        new LocationService(locationRepository, serviceContext.getLocationTypeService()));

    PlayerRegistry playerRegistry = new PlayerRegistry();
    PlayerRepository playerRepository = new PlayerRepository(persistenceContext.getPlayerDao());
    PlayerService playerService = new PlayerService(serviceContext, playerRegistry,
        playerRepository);
    playerService.initialize();
    serviceContext.setPlayerService(playerService);
    groupService.setPlayerRegistry(playerRegistry);

    BagRepository bagRepository = new BagRepository(persistenceContext.getBagDao());
    BagTypeRepository bagTypeRepository = new BagTypeRepository(persistenceContext.getBagDao());

    BagTypeRegistry bagTypeRegistry = new BagTypeRegistry();
    bagTypeRegistry.registerAll(bagTypeRepository.findAll());

    BagRegistry bagRegistry = new BagRegistry();
    bagRegistry.registerAll(bagRepository.findAll());

    BagService bagService = new BagService(
        serviceContext,
        bagRegistry,
        bagRepository,
        bagTypeRegistry,
        bagTypeRepository
    );
    serviceContext.setBagService(bagService);
    BuyBackRepository buyBackRepository = new BuyBackRepository();
    BuyBackService buyBackService = new BuyBackService(buyBackRepository);
    serviceContext.setBuyBackService(buyBackService);
    MessageService messageService = new MessageService(serviceContext.getTranslationService());
    serviceContext.setMessageService(messageService);
    ReplyRegistry replyRegistry = new ReplyRegistry();
    ChatService chatService = new ChatService(serviceContext, replyRegistry);
    serviceContext.setChatService(chatService);

    BankTierRegistry bankTierRegistry = new BankTierRegistry(
        persistenceContext.getBankDao().findAllBankTiers());
    BankRepository bankRepository = new BankRepository(persistenceContext.getBankDao());
    BankService bankService = new BankService(serviceContext, bankTierRegistry, bankRepository,
        relluEssentials);
    serviceContext.setBankService(bankService);

    BackLocationRepository backLocationRepository = new BackLocationRepository();
    BackService backService = new BackService(backLocationRepository);
    serviceContext.setBackService(backService);
    TeleportService teleportService = new TeleportService(serviceContext.getTranslationService(),
        backService);
    serviceContext.setTeleportService(teleportService);

    ProtectionActionService protectionActionService = new ProtectionActionService(serviceContext);
    serviceContext.setProtectionActionService(protectionActionService);

    NpcRepository npcRepository = new NpcRepository(
        relluEssentials.getPersistenceContext().getNpcDao());
    NpcSpawner npcSpawner = new NpcSpawner(serviceContext);
    NpcValidator npcValidator = new NpcValidator();
    NpcService npcService = new NpcService(npcRepository, npcSpawner, npcValidator);
    serviceContext.setNpcService(npcService);

    NpcDialogueRegistry npcDialogueRegistry = new NpcDialogueRegistry();
    NpcDialogueProgressService npcDialogueProgressService = new NpcDialogueProgressService(
        npcDialogueRegistry);
    serviceContext.setNpcDialogueProgressService(npcDialogueProgressService);

    PositionRegistry positionRegistry = new PositionRegistry();
    PositionService positionService = new PositionService(positionRegistry,
        serviceContext.getTranslationService());
    serviceContext.getSchedulerService()
        .runTaskTimer(positionService::tickHighlights, 0L, 20L);
    serviceContext.setPositionService(positionService);

    LocationCleanUpService locationCleanUpService = new LocationCleanUpService(
        serviceContext.getTranslationService(),
        locationRepository
    );
    serviceContext.setLocationCleanUpService(locationCleanUpService);

    ProtectionCleanUpService protectionCleanUpService = new ProtectionCleanUpService(
        serviceContext);
    serviceContext.setProtectionCleanUpService(protectionCleanUpService);
    serviceContext.setDeathChestService(new DeathChestService(serviceContext));

    SettingPlayerRegistry settingPlayerRegistry = new SettingPlayerRegistry();
    SettingPlayerRepository settingPlayerRepository = new SettingPlayerRepository(
        persistenceContext.getSettingPlayerDao());
    serviceContext.setSettingPlayerService(
        new SettingPlayerService(settingPlayerRegistry, settingPlayerRepository, serviceContext));
  }

  public void preEnable(RelluEssentials relluEssentials) {

    String lang = relluEssentials.getConfig().getString("language", "en_US");

    TranslationService translationService = new TranslationService(relluEssentials);
    translationService.loadLanguages();
    translationService.setDefaultLanguage(lang);
    relluEssentials.getServiceContext().setTranslationService(translationService);
  }
}
