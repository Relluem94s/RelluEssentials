package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.DropEntry;
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
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BackLocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagTypeRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BuyBackRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.CropRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.DropRuleRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.LocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.NpcRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PlayerRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.UndoHistoryRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WarpRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.BackService;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.BlockDropService;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.ChatService;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcDialogueProgressService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PositionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TraderNpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.minecraft.server.spigot.essentials.services.WarpService;
import de.relluem94.minecraft.server.spigot.essentials.services.cleanup.LocationCleanUpService;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.plugin.Plugin;

public class ServiceManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentials = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentials.getServiceContext();
    DatabaseHelper databaseHelper = serviceContext.getDatabaseHelper();

    /* Services */
    serviceContext.setClipboardService(new ClipboardService());
    DropRuleRepository dropRuleRepository = new DropRuleRepository();
    for (DropEntry de : databaseHelper.getDrops()) {
      dropRuleRepository.register(de.getMaterial(), new DoubleStore<>(de.getMin(), de.getMax()));
    }

    CropRepository cropRepository = new CropRepository();
    for (CropEntry ce : databaseHelper.getCrops()) {
      cropRepository.register(ce.getSeed(), ce.getPlant());
    }

    BlockDropService blockDropService = new BlockDropService(dropRuleRepository, cropRepository);
    serviceContext.setBlockDropService(blockDropService);

    ProtectionService protectionService = new ProtectionService(
        databaseHelper.getProtectionLocks(),
        databaseHelper.getProtections(),
        databaseHelper);
    serviceContext.setProtectionService(protectionService);

    TraderNpcRegistry traderNpcRegistry = new TraderNpcRegistry(
        serviceContext.getTranslationService());
    traderNpcRegistry.init(databaseHelper.getTraderNPCs());
    BankerNpc bankerNpc = new BankerNpc(serviceContext);
    traderNpcRegistry.addNPC(new BagSalesmanNpc(serviceContext));
    traderNpcRegistry.addNPC(bankerNpc);
    traderNpcRegistry.addNPC(new BeekeeperNpc(serviceContext));
    traderNpcRegistry.addNPC(new EnchanterNpc(serviceContext));
    TraderNpcService traderNpcService = new TraderNpcService(traderNpcRegistry, bankerNpc);
    serviceContext.setTraderNpcService(traderNpcService);

    WarpRepository warpRepository = new WarpRepository(databaseHelper.getWarps());
    serviceContext.setWarpService(new WarpService(warpRepository, databaseHelper, serviceContext.getLocationTypeService()));

    SchedulerService schedulerService = new SchedulerService(relluEssentials);
    serviceContext.setSchedulerService(schedulerService);
    UndoHistoryRepository undoHistoryRepository = new UndoHistoryRepository();
    UndoHistoryService undoHistoryService = new UndoHistoryService(undoHistoryRepository);
    serviceContext.setUndoHistoryService(undoHistoryService);
    SelectionService selectionService = new SelectionService(serviceContext);
    serviceContext.setSelectionService(selectionService);
    GroupRepository groupRepository = new GroupRepository(databaseHelper.getGroups());
    GroupRegistry groupRegistry = new GroupRegistry(groupRepository);
    GroupService groupService = new GroupService(groupRegistry, groupRepository);
    serviceContext.setGroupService(groupService);
    PlayerRegistry playerRegistry = new PlayerRegistry();
    PlayerRepository playerRepository = new PlayerRepository(serviceContext.getDatabaseHelper());
    PlayerService playerService = new PlayerService(serviceContext, playerRegistry,
        playerRepository);
    playerService.initialize();
    serviceContext.setPlayerService(playerService);
    groupService.setPlayerRegistry(playerRegistry);

    BagRepository bagRepository = new BagRepository(databaseHelper.getBags());
    BagTypeRepository bagTypeRepository = new BagTypeRepository(databaseHelper.getBagTypes());
    BagTypeRegistry bagTypeRegistry = new BagTypeRegistry(bagTypeRepository);
    BagRegistry bagRegistry = new BagRegistry(bagRepository);
    BagService bagService = new BagService(serviceContext, bagRegistry, bagTypeRegistry);
    serviceContext.setBagService(bagService);

    BuyBackRepository buyBackRepository = new BuyBackRepository();
    BuyBackService buyBackService = new BuyBackService(buyBackRepository);
    serviceContext.setBuyBackService(buyBackService);
    MessageService messageService = new MessageService(serviceContext.getTranslationService());
    serviceContext.setMessageService(messageService);
    ReplyRegistry replyRegistry = new ReplyRegistry();
    ChatService chatService = new ChatService(serviceContext, replyRegistry);
    serviceContext.setChatService(chatService);
    BankService bankService = new BankService(databaseHelper, playerRegistry,
        new BankTierRegistry(databaseHelper.getBankTiers()), serviceContext.getTranslationService(),
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

    NpcRepository npcRepository = new NpcRepository(relluEssentials.getPersistenceContext().getNpcDao());
    NpcSpawner npcSpawner = new NpcSpawner();
    NpcValidator npcValidator = new NpcValidator();
    NpcService npcService = new NpcService(npcRepository, npcSpawner, npcValidator);
    serviceContext.setNpcService(npcService);

    NpcDialogueRegistry npcDialogueRegistry = new NpcDialogueRegistry();
    NpcDialogueProgressService npcDialogueProgressService = new NpcDialogueProgressService(npcDialogueRegistry);
    serviceContext.setNpcDialogueProgressService(npcDialogueProgressService);

    PositionRegistry positionRegistry = new PositionRegistry();
    PositionService positionService = new PositionService(positionRegistry,
        serviceContext.getTranslationService());
    serviceContext.getSchedulerService()
        .runTaskTimer(() -> positionService.tickHighlights(), 0L, 20L);
    serviceContext.setPositionService(positionService);

    LocationRepository locationRepository = new LocationRepository(relluEssentials.getPersistenceContext().getLocationDao());

    LocationCleanUpService locationCleanUpService = new LocationCleanUpService(
        serviceContext.getTranslationService(),
        locationRepository
        );
    serviceContext.setLocationCleanUpService(locationCleanUpService);
  }

  public void preEnable(RelluEssentials relluEssentials) {

    String lang = relluEssentials.getConfig().getString("language", "en_US");

    TranslationService translationService = new TranslationService(relluEssentials);
    translationService.loadLanguages();
    translationService.setDefaultLanguage(lang);
    relluEssentials.getServiceContext().setTranslationService(translationService);
  }
}
