package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MobRegistryTest {

  @Mock
  private CustomMobDefinition firstDefinition;

  @Mock
  private CustomMobDefinition secondDefinition;

  @Mock
  private CustomMob firstMob;

  @Mock
  private CustomMob secondMob;

  @Mock
  private LivingEntity firstLivingEntity;

  @Mock
  private LivingEntity secondLivingEntity;

  @Mock
  private RelluEssentialsNamespacedKey firstKey;

  @Mock
  private RelluEssentialsNamespacedKey secondKey;

  private MobRegistry mobRegistry;

  @BeforeEach
  void setUp() {
    mobRegistry = new MobRegistry();
  }

  @Test
  void registerDefinitionStoresDefinitionLookupableByKey() {
    when(firstDefinition.key()).thenReturn(firstKey);

    mobRegistry.registerDefinition(firstDefinition);

    Optional<CustomMobDefinition> result = mobRegistry.findDefinitionByKey(firstKey);
    assertTrue(result.isPresent());
    assertEquals(firstDefinition, result.get());
  }

  @Test
  void registerDefinitionOverwritesExistingDefinitionWithSameKey() {
    when(firstDefinition.key()).thenReturn(firstKey);
    when(secondDefinition.key()).thenReturn(firstKey);

    mobRegistry.registerDefinition(firstDefinition);
    mobRegistry.registerDefinition(secondDefinition);

    Optional<CustomMobDefinition> result = mobRegistry.findDefinitionByKey(firstKey);
    assertTrue(result.isPresent());
    assertEquals(secondDefinition, result.get());
  }

  @Test
  void unregisterDefinitionRemovesDefinitionFromRegistry() {
    when(firstDefinition.key()).thenReturn(firstKey);

    mobRegistry.registerDefinition(firstDefinition);
    mobRegistry.unregisterDefinition(firstKey);

    Optional<CustomMobDefinition> result = mobRegistry.findDefinitionByKey(firstKey);
    assertFalse(result.isPresent());
  }

  @Test
  void unregisterDefinitionOnNonExistentKeyDoesNotThrow() {
    assertDoesNotThrow(() -> mobRegistry.unregisterDefinition(firstKey));
  }

  @Test
  void findDefinitionByKeyReturnsEmptyWhenNotRegistered() {
    Optional<CustomMobDefinition> result = mobRegistry.findDefinitionByKey(firstKey);
    assertFalse(result.isPresent());
  }

  @Test
  void findAllDefinitionsReturnsAllRegisteredDefinitions() {
    when(firstDefinition.key()).thenReturn(firstKey);
    when(secondDefinition.key()).thenReturn(secondKey);

    mobRegistry.registerDefinition(firstDefinition);
    mobRegistry.registerDefinition(secondDefinition);

    List<CustomMobDefinition> result = mobRegistry.findAllDefinitions();
    assertEquals(2, result.size());
    assertTrue(result.contains(firstDefinition));
    assertTrue(result.contains(secondDefinition));
  }

  @Test
  void findAllDefinitionsReturnsEmptyListWhenNoneRegistered() {
    List<CustomMobDefinition> result = mobRegistry.findAllDefinitions();
    assertTrue(result.isEmpty());
  }

  @Test
  void findAllDefinitionsReturnsUnmodifiableList() {
    List<CustomMobDefinition> result = mobRegistry.findAllDefinitions();
    assertThrows(UnsupportedOperationException.class, () -> result.add(firstDefinition));
  }

  @Test
  void registerSpawnedMobStoresMobInRegistry() {
    mobRegistry.registerSpawnedMob(firstMob);

    List<CustomMob> result = mobRegistry.findAllSpawned();
    assertEquals(1, result.size());
    assertTrue(result.contains(firstMob));
  }

  @Test
  void unregisterSpawnedMobRemovesMobByEntityUuid() {
    UUID entityUuid = UUID.randomUUID();
    when(firstMob.getLivingEntity()).thenReturn(firstLivingEntity);
    when(firstLivingEntity.getUniqueId()).thenReturn(entityUuid);

    mobRegistry.registerSpawnedMob(firstMob);
    mobRegistry.unregisterSpawnedMob(entityUuid);

    List<CustomMob> result = mobRegistry.findAllSpawned();
    assertTrue(result.isEmpty());
  }

  @Test
  void unregisterSpawnedMobOnlyRemovesMobWithMatchingUuid() {
    UUID firstUuid = UUID.randomUUID();
    UUID secondUuid = UUID.randomUUID();

    when(firstMob.getLivingEntity()).thenReturn(firstLivingEntity);
    when(firstLivingEntity.getUniqueId()).thenReturn(firstUuid);
    when(secondMob.getLivingEntity()).thenReturn(secondLivingEntity);
    when(secondLivingEntity.getUniqueId()).thenReturn(secondUuid);

    mobRegistry.registerSpawnedMob(firstMob);
    mobRegistry.registerSpawnedMob(secondMob);
    mobRegistry.unregisterSpawnedMob(firstUuid);

    List<CustomMob> result = mobRegistry.findAllSpawned();
    assertEquals(1, result.size());
    assertTrue(result.contains(secondMob));
  }

  @Test
  void unregisterSpawnedMobWithNonExistentUuidDoesNotThrow() {
    assertDoesNotThrow(() -> mobRegistry.unregisterSpawnedMob(UUID.randomUUID()));
  }

  @Test
  void findAllSpawnedReturnsAllSpawnedMobs() {
    mobRegistry.registerSpawnedMob(firstMob);
    mobRegistry.registerSpawnedMob(secondMob);

    List<CustomMob> result = mobRegistry.findAllSpawned();
    assertEquals(2, result.size());
    assertTrue(result.contains(firstMob));
    assertTrue(result.contains(secondMob));
  }

  @Test
  void findAllSpawnedReturnsEmptyListWhenNoneRegistered() {
    List<CustomMob> result = mobRegistry.findAllSpawned();
    assertTrue(result.isEmpty());
  }

  @Test
  void findAllSpawnedReturnsUnmodifiableList() {
    List<CustomMob> result = mobRegistry.findAllSpawned();
    assertThrows(UnsupportedOperationException.class, () -> result.add(firstMob));
  }

  @Test
  void findAllSpawnedAliveReturnsOnlyAliveMobs() {
    when(firstMob.isAlive()).thenReturn(true);
    when(secondMob.isAlive()).thenReturn(false);

    mobRegistry.registerSpawnedMob(firstMob);
    mobRegistry.registerSpawnedMob(secondMob);

    List<CustomMob> result = mobRegistry.findAllSpawnedAlive();
    assertEquals(1, result.size());
    assertTrue(result.contains(firstMob));
  }

  @Test
  void findAllSpawnedAliveReturnsEmptyListWhenNoMobsAlive() {
    when(firstMob.isAlive()).thenReturn(false);
    when(secondMob.isAlive()).thenReturn(false);

    mobRegistry.registerSpawnedMob(firstMob);
    mobRegistry.registerSpawnedMob(secondMob);

    List<CustomMob> result = mobRegistry.findAllSpawnedAlive();
    assertTrue(result.isEmpty());
  }

  @Test
  void findAllSpawnedAliveReturnsEmptyListWhenNoMobsSpawned() {
    List<CustomMob> result = mobRegistry.findAllSpawnedAlive();
    assertTrue(result.isEmpty());
  }

  @Test
  void findAllSpawnedByDefinitionKeyReturnsOnlyMobsMatchingKey() {
    when(firstMob.getDefinitionKey()).thenReturn(firstKey);
    when(secondMob.getDefinitionKey()).thenReturn(secondKey);

    mobRegistry.registerSpawnedMob(firstMob);
    mobRegistry.registerSpawnedMob(secondMob);

    List<CustomMob> result = mobRegistry.findAllSpawnedByDefinitionKey(firstKey);
    assertEquals(1, result.size());
    assertTrue(result.contains(firstMob));
  }

  @Test
  void findAllSpawnedByDefinitionKeyReturnsEmptyListWhenNoMobsMatchKey() {
    when(firstMob.getDefinitionKey()).thenReturn(secondKey);

    mobRegistry.registerSpawnedMob(firstMob);

    List<CustomMob> result = mobRegistry.findAllSpawnedByDefinitionKey(firstKey);
    assertTrue(result.isEmpty());
  }

  @Test
  void findAllSpawnedByDefinitionKeyReturnsEmptyListWhenNoMobsSpawned() {
    List<CustomMob> result = mobRegistry.findAllSpawnedByDefinitionKey(firstKey);
    assertTrue(result.isEmpty());
  }

  @Test
  void findAllSpawnedByDefinitionKeyReturnsAllMobsMatchingKey() {
    when(firstMob.getDefinitionKey()).thenReturn(firstKey);
    when(secondMob.getDefinitionKey()).thenReturn(firstKey);

    mobRegistry.registerSpawnedMob(firstMob);
    mobRegistry.registerSpawnedMob(secondMob);

    List<CustomMob> result = mobRegistry.findAllSpawnedByDefinitionKey(firstKey);
    assertEquals(2, result.size());
    assertTrue(result.contains(firstMob));
    assertTrue(result.contains(secondMob));
  }
}