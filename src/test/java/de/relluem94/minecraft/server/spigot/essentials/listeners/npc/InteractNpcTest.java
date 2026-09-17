package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcDialogueProgressService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InteractNpcTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private NpcService npcService;

  @Mock
  private NpcDialogueProgressService npcDialogueProgressService;

  @Mock
  private PlayerInteractEntityEvent event;

  @Mock
  private Mannequin mannequin;

  @Mock
  private Player player;

  private InteractNpc interactNpc;

  @BeforeEach
  void setUp() {
    interactNpc = new InteractNpc();
    interactNpc.injectContext(serviceContext);
  }

  @Test
  void onPlayerInteractEntityIgnoresEventWhenClickedEntityIsNotMannequin() {
    when(event.getRightClicked()).thenReturn(mock(org.bukkit.entity.Villager.class));

    interactNpc.onPlayerInteractEntity(event);

    verify(event, never()).getHand();
    verify(serviceContext, never()).getNpcService();
  }

  @Test
  void onPlayerInteractEntityIgnoresEventWhenHandIsNotMainHand() {
    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.OFF_HAND);

    interactNpc.onPlayerInteractEntity(event);

    verify(event, never()).getPlayer();
    verify(serviceContext, never()).getNpcService();
  }

  @Test
  void onPlayerInteractEntityIgnoresEventWhenPlayerIsOnCooldown() {
    UUID playerUuid = UUID.randomUUID();
    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);

    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcs()).thenReturn(List.of());

    interactNpc.onPlayerInteractEntity(event);
    interactNpc.onPlayerInteractEntity(event);

    verify(serviceContext, times(1)).getNpcService();
  }

  @Test
  void onPlayerInteractEntityIgnoresEventWhenNoNpcMatchesMannequin() {
    UUID playerUuid = UUID.randomUUID();

    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcs()).thenReturn(List.of());

    interactNpc.onPlayerInteractEntity(event);

    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void onPlayerInteractEntityIgnoresEventWhenMatchedNpcHasNoDialogueLines() {
    UUID playerUuid = UUID.randomUUID();
    UUID npcId = UUID.randomUUID();
    UUID mannequinUuid = UUID.randomUUID();

    Npc npc = new Npc(1, npcId, "TestNpc", 0, 0, 0, 0f, 0f, "world");
    npc.setEntityUUID(mannequinUuid);
    npc.setDialogueLines(List.of());

    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(mannequin.getUniqueId()).thenReturn(mannequinUuid);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcs()).thenReturn(List.of(npc));

    interactNpc.onPlayerInteractEntity(event);

    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void onPlayerInteractEntitySendsDialogueMessageToPlayerWhenNpcMatchesAndHasDialogue() {
    UUID playerUuid = UUID.randomUUID();
    UUID npcId = UUID.randomUUID();
    UUID mannequinUuid = UUID.randomUUID();

    NpcDialogueEntry dialogueEntry = new NpcDialogueEntry();
    dialogueEntry.setText("Hello traveler!");

    Npc npc = new Npc(1, npcId, "Gandalf", 0, 0, 0, 0f, 0f, "world");
    npc.setEntityUUID(mannequinUuid);
    npc.setDialogueLines(List.of(dialogueEntry));

    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(mannequin.getUniqueId()).thenReturn(mannequinUuid);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcs()).thenReturn(List.of(npc));
    when(serviceContext.getNpcDialogueProgressService()).thenReturn(npcDialogueProgressService);
    when(npcDialogueProgressService.getNextLineIndexAndAdvance(npcId, playerUuid, 1)).thenReturn(0);

    interactNpc.onPlayerInteractEntity(event);

    verify(player).sendMessage(contains("Gandalf"));
    verify(player).sendMessage(contains("Hello traveler!"));
  }

  @Test
  void onPlayerInteractEntityAllowsInteractionAfterCooldownExpires() throws InterruptedException {
    UUID playerUuid = UUID.randomUUID();
    UUID npcId = UUID.randomUUID();
    UUID mannequinUuid = UUID.randomUUID();

    NpcDialogueEntry dialogueEntry = new NpcDialogueEntry();
    dialogueEntry.setText("Hello again!");

    Npc npc = new Npc(1, npcId, "Merlin", 0, 0, 0, 0f, 0f, "world");
    npc.setEntityUUID(mannequinUuid);
    npc.setDialogueLines(List.of(dialogueEntry));

    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(mannequin.getUniqueId()).thenReturn(mannequinUuid);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcs()).thenReturn(List.of(npc));
    when(serviceContext.getNpcDialogueProgressService()).thenReturn(npcDialogueProgressService);
    when(npcDialogueProgressService.getNextLineIndexAndAdvance(npcId, playerUuid, 1)).thenReturn(0);

    interactNpc.onPlayerInteractEntity(event);

    Thread.sleep(800);

    interactNpc.onPlayerInteractEntity(event);

    verify(player, times(2)).sendMessage(anyString());
  }

  @Test
  void onPlayerInteractEntitySendsCorrectDialogueLineBasedOnProgressIndex() {
    UUID playerUuid = UUID.randomUUID();
    UUID npcId = UUID.randomUUID();
    UUID mannequinUuid = UUID.randomUUID();

    NpcDialogueEntry firstEntry = new NpcDialogueEntry();
    firstEntry.setText("First line");

    NpcDialogueEntry secondEntry = new NpcDialogueEntry();
    secondEntry.setText("Second line");

    Npc npc = new Npc(1, npcId, "Narrator", 0, 0, 0, 0f, 0f, "world");
    npc.setEntityUUID(mannequinUuid);
    npc.setDialogueLines(List.of(firstEntry, secondEntry));

    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(mannequin.getUniqueId()).thenReturn(mannequinUuid);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcs()).thenReturn(List.of(npc));
    when(serviceContext.getNpcDialogueProgressService()).thenReturn(npcDialogueProgressService);
    when(npcDialogueProgressService.getNextLineIndexAndAdvance(npcId, playerUuid, 2)).thenReturn(1);

    interactNpc.onPlayerInteractEntity(event);

    verify(player).sendMessage(contains("Second line"));
  }

  @Test
  void onPlayerInteractEntityAllowsDifferentPlayersToInteractSimultaneously() {
    UUID firstPlayerUuid = UUID.randomUUID();
    UUID secondPlayerUuid = UUID.randomUUID();
    UUID npcId = UUID.randomUUID();
    UUID mannequinUuid = UUID.randomUUID();

    Player secondPlayer = mock(Player.class);

    NpcDialogueEntry dialogueEntry = new NpcDialogueEntry();
    dialogueEntry.setText("Greetings!");

    Npc npc = new Npc(1, npcId, "Guard", 0, 0, 0, 0f, 0f, "world");
    npc.setEntityUUID(mannequinUuid);
    npc.setDialogueLines(List.of(dialogueEntry));

    PlayerInteractEntityEvent secondEvent = mock(PlayerInteractEntityEvent.class);

    when(event.getRightClicked()).thenReturn(mannequin);
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(firstPlayerUuid);

    when(secondEvent.getRightClicked()).thenReturn(mannequin);
    when(secondEvent.getHand()).thenReturn(EquipmentSlot.HAND);
    when(secondEvent.getPlayer()).thenReturn(secondPlayer);
    when(secondPlayer.getUniqueId()).thenReturn(secondPlayerUuid);

    when(mannequin.getUniqueId()).thenReturn(mannequinUuid);
    when(serviceContext.getNpcService()).thenReturn(npcService);
    when(npcService.getNpcs()).thenReturn(List.of(npc));
    when(serviceContext.getNpcDialogueProgressService()).thenReturn(npcDialogueProgressService);
    when(npcDialogueProgressService.getNextLineIndexAndAdvance(any(), eq(firstPlayerUuid), eq(1))).thenReturn(0);
    when(npcDialogueProgressService.getNextLineIndexAndAdvance(any(), eq(secondPlayerUuid), eq(1))).thenReturn(0);

    interactNpc.onPlayerInteractEntity(event);
    interactNpc.onPlayerInteractEntity(secondEvent);

    verify(player).sendMessage(anyString());
    verify(secondPlayer).sendMessage(anyString());
  }
}