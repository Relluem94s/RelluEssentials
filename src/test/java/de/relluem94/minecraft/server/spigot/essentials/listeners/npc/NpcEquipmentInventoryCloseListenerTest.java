package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcEquipmentInventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import java.util.UUID;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcEquipmentInventoryCloseListenerTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private NpcService npcService;

  @Mock
  private Player closingPlayer;

  @Mock
  private HumanEntity differentPlayer;

  @Mock
  private Npc targetNpc;

  @Mock
  private Inventory equipmentInventory;

  @Mock
  private Inventory differentInventory;

  @Mock
  private InventoryCloseEvent inventoryCloseEvent;

  private NpcEquipmentInventoryCloseListener listener;

  private MockedStatic<HandlerList> handlerListMockedStatic;
  private MockedStatic<NpcEquipmentInventoryHelper> npcEquipmentInventoryHelperMockedStatic;

  @BeforeEach
  void setUp() {
    lenient().when(serviceContext.getNpcService()).thenReturn(npcService);
    listener = new NpcEquipmentInventoryCloseListener(serviceContext, closingPlayer, targetNpc, equipmentInventory);
    handlerListMockedStatic = mockStatic(HandlerList.class);
    npcEquipmentInventoryHelperMockedStatic = mockStatic(NpcEquipmentInventoryHelper.class);
  }

  @AfterEach
  void tearDown() {
    handlerListMockedStatic.close();
    npcEquipmentInventoryHelperMockedStatic.close();
  }

  @Test
  void onInventoryCloseDoesNothingWhenPlayerDoesNotMatch() {
    when(inventoryCloseEvent.getPlayer()).thenReturn(differentPlayer);

    listener.onInventoryClose(inventoryCloseEvent);

    handlerListMockedStatic.verifyNoInteractions();
    verify(npcService, never()).saveNpcInventory(any(), any());
    npcEquipmentInventoryHelperMockedStatic.verifyNoInteractions();
  }

  @Test
  void onInventoryCloseDoesNothingWhenInventoryDoesNotMatch() {
    when(inventoryCloseEvent.getPlayer()).thenReturn(closingPlayer);
    when(inventoryCloseEvent.getInventory()).thenReturn(differentInventory);

    listener.onInventoryClose(inventoryCloseEvent);

    handlerListMockedStatic.verifyNoInteractions();
    verify(npcService, never()).saveNpcInventory(any(), any());
    npcEquipmentInventoryHelperMockedStatic.verifyNoInteractions();
  }

  @Test
  void onInventoryCloseUnregistersListenerWhenPlayerAndInventoryMatch() {
    when(inventoryCloseEvent.getPlayer()).thenReturn(closingPlayer);
    when(inventoryCloseEvent.getInventory()).thenReturn(equipmentInventory);
    when(targetNpc.getEntityUUID()).thenReturn(null);

    listener.onInventoryClose(inventoryCloseEvent);

    handlerListMockedStatic.verify(() -> HandlerList.unregisterAll(listener));
  }

  @Test
  void onInventoryCloseSavesNpcInventoryWhenPlayerAndInventoryMatch() {
    when(inventoryCloseEvent.getPlayer()).thenReturn(closingPlayer);
    when(inventoryCloseEvent.getInventory()).thenReturn(equipmentInventory);
    when(targetNpc.getEntityUUID()).thenReturn(null);

    listener.onInventoryClose(inventoryCloseEvent);

    verify(npcService).saveNpcInventory(targetNpc, equipmentInventory);
  }

  @Test
  void onInventoryCloseAppliesEquipmentToEntityWhenEntityUuidIsPresent() {
    UUID entityUUID = UUID.randomUUID();
    when(inventoryCloseEvent.getPlayer()).thenReturn(closingPlayer);
    when(inventoryCloseEvent.getInventory()).thenReturn(equipmentInventory);
    when(targetNpc.getEntityUUID()).thenReturn(entityUUID);

    listener.onInventoryClose(inventoryCloseEvent);

    npcEquipmentInventoryHelperMockedStatic.verify(() ->
        NpcEquipmentInventoryHelper.applyInventoryEquipmentToEntity(equipmentInventory, entityUUID));
  }

  @Test
  void onInventoryCloseSkipsApplyEquipmentWhenEntityUuidIsNull() {
    when(inventoryCloseEvent.getPlayer()).thenReturn(closingPlayer);
    when(inventoryCloseEvent.getInventory()).thenReturn(equipmentInventory);
    when(targetNpc.getEntityUUID()).thenReturn(null);

    listener.onInventoryClose(inventoryCloseEvent);

    npcEquipmentInventoryHelperMockedStatic.verifyNoInteractions();
  }

  @Test
  void onInventoryCloseExecutesFullSequenceWhenAllConditionsMet() {
    UUID entityUUID = UUID.randomUUID();
    when(inventoryCloseEvent.getPlayer()).thenReturn(closingPlayer);
    when(inventoryCloseEvent.getInventory()).thenReturn(equipmentInventory);
    when(targetNpc.getEntityUUID()).thenReturn(entityUUID);

    listener.onInventoryClose(inventoryCloseEvent);

    handlerListMockedStatic.verify(() -> HandlerList.unregisterAll(listener));
    verify(npcService).saveNpcInventory(targetNpc, equipmentInventory);
    npcEquipmentInventoryHelperMockedStatic.verify(() ->
        NpcEquipmentInventoryHelper.applyInventoryEquipmentToEntity(equipmentInventory, entityUUID));
  }
}