package de.relluem94.minecraft.server.spigot.essentials.npcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mannequin;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcSpawnerTest {

  private static final UUID NPC_UUID = UUID.randomUUID();
  private static final UUID MANNEQUIN_UUID = UUID.randomUUID();
  private static final String WORLD_NAME = "world";
  private static final String NPC_PROFILE_NAME = "TestNpc";
  private static final double NPC_X = 10.0;
  private static final double NPC_Y = 64.0;
  private static final double NPC_Z = 20.0;
  private static final float NPC_YAW = 90.0f;
  private static final float NPC_PITCH = 0.0f;

  @Mock
  private Server server;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private NpcMannequinAttributeApplier npcMannequinAttributeApplier;

  @Mock
  private World world;

  @Mock
  private Mannequin mannequin;

  @Mock
  private PersistentDataContainer persistentDataContainer;

  @Mock
  private PlayerProfile playerProfile;

  @Mock
  private Npc npc;

  @Mock
  private NamespacedKey npcIdKey;

  private NpcSpawner npcSpawner;

  @BeforeEach
  void setUp() {
    npcSpawner = new NpcSpawner(server, npcIdKey, npcMannequinAttributeApplier);
  }

  @Test
  void spawnMannequin_whenWorldDoesNotExist_returnsEmptyOptional() {
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(server.getWorld(WORLD_NAME)).thenReturn(null);

    Optional<UUID> result = npcSpawner.spawnMannequin(npc);

    assertTrue(result.isEmpty());
  }

  @Test
  void spawnMannequin_whenMannequinAlreadyExists_returnsExistingMannequinUuid() {
    when(npc.getId()).thenReturn(NPC_UUID);
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(mannequin.getType()).thenReturn(EntityType.MANNEQUIN);
    when(mannequin.getUniqueId()).thenReturn(MANNEQUIN_UUID);
    when(mannequin.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(server.getWorld(WORLD_NAME)).thenReturn(world);
    when(world.getEntities()).thenReturn(List.of(mannequin));
    when(persistentDataContainer.get(any(NamespacedKey.class), eq(PersistentDataType.STRING)))
        .thenReturn(NPC_UUID.toString());
    when(server.getEntity(MANNEQUIN_UUID)).thenReturn(mannequin);

    Optional<UUID> result = npcSpawner.spawnMannequin(npc);

    assertTrue(result.isPresent());
    assertEquals(MANNEQUIN_UUID, result.get());
  }

  @Test
  void spawnMannequin_whenMannequinAlreadyExists_appliesAttributesToExistingMannequin() {
    when(npc.getId()).thenReturn(NPC_UUID);
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(mannequin.getType()).thenReturn(EntityType.MANNEQUIN);
    when(mannequin.getUniqueId()).thenReturn(MANNEQUIN_UUID);
    when(mannequin.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(server.getWorld(WORLD_NAME)).thenReturn(world);
    when(world.getEntities()).thenReturn(List.of(mannequin));
    when(persistentDataContainer.get(any(NamespacedKey.class), eq(PersistentDataType.STRING)))
        .thenReturn(NPC_UUID.toString());
    when(server.getEntity(MANNEQUIN_UUID)).thenReturn(mannequin);

    npcSpawner.spawnMannequin(npc);

    verify(npcMannequinAttributeApplier).applyAttributes(mannequin);
  }

  @Test
  void spawnMannequin_whenNoExistingMannequin_spawnsNewMannequinAndReturnsUuid() {
    when(npc.getId()).thenReturn(NPC_UUID);
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(npc.getX()).thenReturn(NPC_X);
    when(npc.getY()).thenReturn(NPC_Y);
    when(npc.getZ()).thenReturn(NPC_Z);
    when(npc.getYaw()).thenReturn(NPC_YAW);
    when(npc.getPitch()).thenReturn(NPC_PITCH);
    when(npc.getProfileName()).thenReturn(NPC_PROFILE_NAME);
    when(mannequin.getUniqueId()).thenReturn(MANNEQUIN_UUID);
    when(mannequin.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(server.getWorld(WORLD_NAME)).thenReturn(world);
    when(world.getEntities()).thenReturn(Collections.emptyList());
    when(world.spawnEntity(any(Location.class), eq(EntityType.MANNEQUIN))).thenReturn(mannequin);
    when(server.createPlayerProfile(NPC_PROFILE_NAME)).thenReturn(playerProfile);

    Optional<UUID> result = npcSpawner.spawnMannequin(npc);

    assertTrue(result.isPresent());
    assertEquals(MANNEQUIN_UUID, result.get());
  }

  @Test
  void spawnMannequin_whenNoExistingMannequin_tagsNewMannequinWithNpcId() {
    when(npc.getId()).thenReturn(NPC_UUID);
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(npc.getX()).thenReturn(NPC_X);
    when(npc.getY()).thenReturn(NPC_Y);
    when(npc.getZ()).thenReturn(NPC_Z);
    when(npc.getYaw()).thenReturn(NPC_YAW);
    when(npc.getPitch()).thenReturn(NPC_PITCH);
    when(npc.getProfileName()).thenReturn(NPC_PROFILE_NAME);
    when(mannequin.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(mannequin.getUniqueId()).thenReturn(MANNEQUIN_UUID);
    when(server.getWorld(WORLD_NAME)).thenReturn(world);
    when(world.getEntities()).thenReturn(Collections.emptyList());
    when(world.spawnEntity(any(Location.class), eq(EntityType.MANNEQUIN))).thenReturn(mannequin);
    when(server.createPlayerProfile(NPC_PROFILE_NAME)).thenReturn(playerProfile);

    npcSpawner.spawnMannequin(npc);

    verify(persistentDataContainer).set(any(NamespacedKey.class), eq(PersistentDataType.STRING),
        eq(NPC_UUID.toString()));
  }

  @Test
  void spawnMannequin_whenNoExistingMannequin_setsPlayerProfileOnMannequin() {
    when(npc.getId()).thenReturn(NPC_UUID);
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(npc.getX()).thenReturn(NPC_X);
    when(npc.getY()).thenReturn(NPC_Y);
    when(npc.getZ()).thenReturn(NPC_Z);
    when(npc.getYaw()).thenReturn(NPC_YAW);
    when(npc.getPitch()).thenReturn(NPC_PITCH);
    when(npc.getProfileName()).thenReturn(NPC_PROFILE_NAME);
    when(mannequin.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(mannequin.getUniqueId()).thenReturn(MANNEQUIN_UUID);
    when(server.getWorld(WORLD_NAME)).thenReturn(world);
    when(world.getEntities()).thenReturn(Collections.emptyList());
    when(world.spawnEntity(any(Location.class), eq(EntityType.MANNEQUIN))).thenReturn(mannequin);
    when(server.createPlayerProfile(NPC_PROFILE_NAME)).thenReturn(playerProfile);

    npcSpawner.spawnMannequin(npc);

    verify(mannequin).setPlayerProfile(playerProfile);
  }

  @Test
  void spawnMannequin_whenNoExistingMannequin_appliesAttributesToNewMannequin() {
    when(npc.getId()).thenReturn(NPC_UUID);
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(npc.getX()).thenReturn(NPC_X);
    when(npc.getY()).thenReturn(NPC_Y);
    when(npc.getZ()).thenReturn(NPC_Z);
    when(npc.getYaw()).thenReturn(NPC_YAW);
    when(npc.getPitch()).thenReturn(NPC_PITCH);
    when(npc.getProfileName()).thenReturn(NPC_PROFILE_NAME);
    when(mannequin.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(mannequin.getUniqueId()).thenReturn(MANNEQUIN_UUID);
    when(server.getWorld(WORLD_NAME)).thenReturn(world);
    when(world.getEntities()).thenReturn(Collections.emptyList());
    when(world.spawnEntity(any(Location.class), eq(EntityType.MANNEQUIN))).thenReturn(mannequin);
    when(server.createPlayerProfile(NPC_PROFILE_NAME)).thenReturn(playerProfile);

    npcSpawner.spawnMannequin(npc);

    verify(npcMannequinAttributeApplier).applyAttributes(mannequin);
  }

  @Test
  void spawnMannequin_whenSpawnedEntityIsNotMannequin_returnsEmptyOptional() {
    Entity nonMannequinEntity = mock(Entity.class);
    when(npc.getId()).thenReturn(NPC_UUID);
    when(npc.getWorldName()).thenReturn(WORLD_NAME);
    when(npc.getX()).thenReturn(NPC_X);
    when(npc.getY()).thenReturn(NPC_Y);
    when(npc.getZ()).thenReturn(NPC_Z);
    when(npc.getYaw()).thenReturn(NPC_YAW);
    when(npc.getPitch()).thenReturn(NPC_PITCH);
    when(server.getWorld(WORLD_NAME)).thenReturn(world);
    when(world.getEntities()).thenReturn(Collections.emptyList());
    when(world.spawnEntity(any(Location.class), eq(EntityType.MANNEQUIN)))
        .thenReturn(nonMannequinEntity);

    Optional<UUID> result = npcSpawner.spawnMannequin(npc);

    assertTrue(result.isEmpty());
  }

  @Test
  void despawnMannequin_whenEntityExists_removesEntity() {
    when(server.getEntity(MANNEQUIN_UUID)).thenReturn(mannequin);

    npcSpawner.despawnMannequin(MANNEQUIN_UUID);

    verify(mannequin).remove();
  }

  @Test
  void despawnMannequin_whenEntityDoesNotExist_doesNotThrow() {
    when(server.getEntity(MANNEQUIN_UUID)).thenReturn(null);

    npcSpawner.despawnMannequin(MANNEQUIN_UUID);

    verify(server).getEntity(MANNEQUIN_UUID);
  }
}