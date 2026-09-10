package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcEquipmentInventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginManagerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcEquipCommandTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private GroupService groupService;

  @Mock
  private TranslationService translationService;

  @Mock
  private NpcService npcService;

  @Mock
  private ServerService serverService;

  @Mock
  private PluginManagerService pluginManagerService;

  @Mock
  private Player player;

  private NpcEquipCommand npcEquipCommand;

  private static final String TRANSLATED_MESSAGE = "translated-message";
  private static final String VALID_UUID_STRING = UUID.randomUUID().toString();

  @BeforeEach
  void setUp() {
    npcEquipCommand = new NpcEquipCommand(serviceContext);

    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
  }

  @Test
  void executeSendsPermissionMissingWhenPlayerIsNotAuthorized() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    npcEquipCommand.execute(player, new String[]{"npc", "equip", VALID_UUID_STRING});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(npcService, never()).getNpcById(any());
  }

  @Test
  void executeSendsUsageMessageWhenArgsAreTooShort() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NPC_EQUIP_USAGE)).thenReturn(TRANSLATED_MESSAGE);

    npcEquipCommand.execute(player, new String[]{"npc", "equip"});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(serviceContext, never()).getNpcService();
  }

  @Test
  void executeSendsUsageMessageWhenArgsAreEmpty() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NPC_EQUIP_USAGE)).thenReturn(TRANSLATED_MESSAGE);

    npcEquipCommand.execute(player, new String[]{});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void executeSendsInvalidIdMessageWhenNpcIdIsNotValidUUID() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NPC_INVALID_ID)).thenReturn(TRANSLATED_MESSAGE);

    npcEquipCommand.execute(player, new String[]{"npc", "equip", "not-a-valid-uuid"});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(serviceContext, never()).getNpcService();
  }

  @Test
  void executeSendsNotFoundMessageWhenNpcDoesNotExist() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcById(any(UUID.class))).thenReturn(Optional.empty());
    when(translationService.getWithPrefix(MessageKey.COMMAND_NPC_NOT_FOUND)).thenReturn(TRANSLATED_MESSAGE);

    npcEquipCommand.execute(player, new String[]{"npc", "equip", VALID_UUID_STRING});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(serverService, never()).createInventory(any(), anyInt(), anyString());
  }

  @Test
  void executeOpensEquipmentInventoryWhenNpcExistsWithoutInventoryAndWithoutEntityUUID() {
    UUID npcId = UUID.fromString(VALID_UUID_STRING);
    Npc npc = new Npc(1, npcId, "TestNpc", 0, 0, 0, 0f, 0f, "world");
    Inventory inventoryMock = mock(Inventory.class);

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.createInventory(eq(null), eq(54), anyString())).thenReturn(inventoryMock);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    npcEquipCommand.execute(player, new String[]{"npc", "equip", VALID_UUID_STRING});

    verify(serverService).createInventory(null, 54, "NPC Equipment: " + npcId);
    verify(player).openInventory(inventoryMock);
    verify(pluginManagerService).registerEvents(any());
  }

  @Test
  void executeRegistersCloseListenerAfterOpeningInventory() {
    UUID npcId = UUID.fromString(VALID_UUID_STRING);
    Npc npc = new Npc(1, npcId, "TestNpc", 0, 0, 0, 0f, 0f, "world");
    Inventory inventoryMock = mock(Inventory.class);

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.createInventory(eq(null), eq(54), anyString())).thenReturn(inventoryMock);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    npcEquipCommand.execute(player, new String[]{"npc", "equip", VALID_UUID_STRING});

    verify(pluginManagerService).registerEvents(any());
  }

  @Test
  void matchesReturnsTrueWhenArgsContainNpcAndEquip() {
    boolean result = npcEquipCommand.matches(new String[]{"npc", "equip", VALID_UUID_STRING});

    assertTrue(result);
  }

  @Test
  void matchesReturnsTrueWhenNpcAndEquipAreMixedCase() {
    boolean result = npcEquipCommand.matches(new String[]{"NPC", "EQUIP", VALID_UUID_STRING});

    assertTrue(result);
  }

  @Test
  void matchesReturnsFalseWhenFirstArgIsNotNpc() {
    boolean result = npcEquipCommand.matches(new String[]{"other", "equip", VALID_UUID_STRING});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseWhenSecondArgIsNotEquip() {
    boolean result = npcEquipCommand.matches(new String[]{"npc", "other", VALID_UUID_STRING});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseWhenArgsLengthIsOne() {
    boolean result = npcEquipCommand.matches(new String[]{"npc"});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseWhenArgsAreEmpty() {
    boolean result = npcEquipCommand.matches(new String[]{});

    assertFalse(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"npc equip", "NPC EQUIP", "Npc Equip"})
  void matchesReturnsTrueForVariousCaseCombinations(String argsString) {
    String[] args = argsString.split(" ");

    boolean result = npcEquipCommand.matches(args);

    assertTrue(result);
  }

  @Test
  void executeLoadsInventoryFromJsonWhenNpcHasInventory() {
    UUID npcId = UUID.fromString(VALID_UUID_STRING);
    Npc npc = new Npc(1, npcId, "TestNpc", new org.json.JSONObject(), 0, 0, 0, 0f, 0f, "world");
    Inventory inventoryMock = mock(Inventory.class);

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.createInventory(eq(null), eq(54), anyString())).thenReturn(inventoryMock);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    npcEquipCommand.execute(player, new String[]{"npc", "equip", VALID_UUID_STRING});

    verify(player).openInventory(inventoryMock);
  }

  @Test
  void executeCallsLoadInventoryFromJsonWhenNpcHasInventory() {
    UUID npcId = UUID.fromString(VALID_UUID_STRING);
    Npc npc = new Npc(1, npcId, "TestNpc", new org.json.JSONObject(), 0, 0, 0, 0f, 0f, "world");
    Inventory inventoryMock = mock(Inventory.class);

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.createInventory(eq(null), eq(54), anyString())).thenReturn(inventoryMock);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (var mockedInventoryHelper = org.mockito.Mockito.mockStatic(InventoryHelper.class)) {
      npcEquipCommand.execute(player, new String[]{"npc", "equip", VALID_UUID_STRING});

      mockedInventoryHelper.verify(() -> InventoryHelper.loadInventoryFromJSON(eq(inventoryMock), eq(npc.getInventory())));
    }
  }

  @Test
  void executeCallsLoadEntityEquipmentIntoInventoryWhenNpcHasEntityUUID() {
    UUID npcId = UUID.fromString(VALID_UUID_STRING);
    UUID entityUUID = UUID.randomUUID();
    Npc npc = new Npc(1, npcId, "TestNpc", 0, 0, 0, 0f, 0f, "world");
    npc.setEntityUUID(entityUUID);
    Inventory inventoryMock = mock(Inventory.class);

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.createInventory(eq(null), eq(54), anyString())).thenReturn(inventoryMock);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (var mockedNpcEquipmentHelper = org.mockito.Mockito.mockStatic(NpcEquipmentInventoryHelper.class)) {
      npcEquipCommand.execute(player, new String[]{"npc", "equip", VALID_UUID_STRING});

      mockedNpcEquipmentHelper.verify(() -> NpcEquipmentInventoryHelper.loadEntityEquipmentIntoInventory(eq(entityUUID), eq(inventoryMock)));
    }
  }
}