package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobEquipment;
import de.relluem94.minecraft.server.spigot.essentials.registries.MobRegistry;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MobServiceTest {

  @Mock
  private MobRegistry mobRegistry;

  @Mock
  private LivingEntity livingEntity;

  @Mock
  private World world;

  @Mock
  private Location location;

  @Mock
  private EntityEquipment entityEquipment;

  @Mock
  private PersistentDataContainer persistentDataContainer;

  @Mock
  private CustomMobDefinition definition;

  @Mock
  private CustomMobEquipment equipment;

  @Mock
  private RelluEssentialsNamespacedKey definitionKey;

  private MobService mobService;

  @BeforeEach
  void setUp() {
    mobService = new MobService(mobRegistry);
  }

  @Test
  void registerDefinitionDelegatesToRegistry() {
    mobService.registerDefinition(definition);

    verify(mobRegistry).registerDefinition(definition);
  }

  @Test
  void findDefinitionReturnsDefinitionWhenFound() {
    when(mobRegistry.findDefinitionByKey(definitionKey)).thenReturn(Optional.of(definition));

    Optional<CustomMobDefinition> result = mobService.findDefinition(definitionKey);

    assertTrue(result.isPresent());
    assertEquals(definition, result.get());
  }

  @Test
  void findDefinitionReturnsEmptyWhenNotFound() {
    when(mobRegistry.findDefinitionByKey(definitionKey)).thenReturn(Optional.empty());

    Optional<CustomMobDefinition> result = mobService.findDefinition(definitionKey);

    assertTrue(result.isEmpty());
  }

  @Test
  void spawnMobByKeyReturnsEmptyWhenDefinitionNotFound() {
    when(mobRegistry.findDefinitionByKey(definitionKey)).thenReturn(Optional.empty());

    Optional<CustomMob> result = mobService.spawnMob(definitionKey, location);

    assertTrue(result.isEmpty());
  }

  @Test
  void spawnMobByKeyReturnsEmptyWhenWorldIsNull() {
    when(mobRegistry.findDefinitionByKey(definitionKey)).thenReturn(Optional.of(definition));
    when(location.getWorld()).thenReturn(null);

    Optional<CustomMob> result = mobService.spawnMob(definitionKey, location);

    assertTrue(result.isEmpty());
  }

  @Test
  void spawnMobByKeySpawnsMobSuccessfully() {
    setupFullSpawnMocks(10.0);
    when(mobRegistry.findDefinitionByKey(definitionKey)).thenReturn(Optional.of(definition));

    Optional<CustomMob> result = mobService.spawnMob(definitionKey, location);

    assertTrue(result.isPresent());
    assertEquals(livingEntity, result
        .get()
        .getLivingEntity());
    assertEquals(EntityType.ZOMBIE, result
        .get()
        .getEntityType());
    assertEquals(definitionKey, result
        .get()
        .getDefinitionKey());
  }

  @Test
  void spawnMobByDefinitionReturnsEmptyWhenWorldIsNull() {
    when(location.getWorld()).thenReturn(null);

    Optional<CustomMob> result = mobService.spawnMob(definition, location);

    assertTrue(result.isEmpty());
  }

  @Test
  void spawnMobByDefinitionSpawnsMobSuccessfully() {
    setupFullSpawnMocks(10.0);

    Optional<CustomMob> result = mobService.spawnMob(definition, location);

    assertTrue(result.isPresent());
    assertEquals(livingEntity, result
        .get()
        .getLivingEntity());
    assertEquals(EntityType.ZOMBIE, result
        .get()
        .getEntityType());
    assertEquals(definitionKey, result
        .get()
        .getDefinitionKey());
  }

  @Test
  void spawnMobUsesEntityHealthWhenDefinitionHealthIsZero() {
    setupFullSpawnMocks(0.0);
    when(livingEntity.getHealth()).thenReturn(20.0);

    mobService.spawnMob(definition, location);

    verify(livingEntity).setHealth(20.0);
  }

  @Test
  void spawnMobUsesDefinitionHealthWhenNonZero() {
    setupFullSpawnMocks(15.0);

    mobService.spawnMob(definition, location);

    verify(livingEntity).setHealth(15.0);
  }

  @Test
  void spawnMobAppliesAllEntityProperties() {
    Collection<PotionEffect> potionEffects = List.of();
    setupFullSpawnMocks(10.0);
    when(definition.potionEffects()).thenReturn(potionEffects);

    mobService.spawnMob(definition, location);

    verify(livingEntity).setCustomName("TestMob");
    verify(livingEntity).setCustomNameVisible(true);
    verify(livingEntity).setHealth(10.0);
    verify(livingEntity).addPotionEffects(potionEffects);
    verify(livingEntity).setInvisible(false);
    verify(livingEntity).setCanPickupItems(false);
  }

  @Test
  void spawnMobAppliesEquipmentWhenEquipmentIsNotNull() {
    ItemStack mainHand = mock(ItemStack.class);
    ItemStack offHand = mock(ItemStack.class);
    ItemStack helmet = mock(ItemStack.class);
    ItemStack chestplate = mock(ItemStack.class);
    ItemStack leggings = mock(ItemStack.class);
    ItemStack boots = mock(ItemStack.class);

    setupFullSpawnMocks(10.0);
    when(equipment.mainHand()).thenReturn(mainHand);
    when(equipment.offHand()).thenReturn(offHand);
    when(equipment.helmet()).thenReturn(helmet);
    when(equipment.chestplate()).thenReturn(chestplate);
    when(equipment.leggings()).thenReturn(leggings);
    when(equipment.boots()).thenReturn(boots);

    mobService.spawnMob(definition, location);

    verify(entityEquipment).setItemInMainHand(mainHand);
    verify(entityEquipment).setItemInOffHand(offHand);
    verify(entityEquipment).setHelmet(helmet);
    verify(entityEquipment).setChestplate(chestplate);
    verify(entityEquipment).setLeggings(leggings);
    verify(entityEquipment).setBoots(boots);
  }

  @Test
  void spawnMobSkipsEquipmentWhenEntityEquipmentIsNull() {
    setupFullSpawnMocksWithNullEquipment();

    assertDoesNotThrow(() -> mobService.spawnMob(definition, location));
    verify(entityEquipment, never()).setItemInMainHand(any());
  }

  @Test
  void spawnMobAppliesNbtTagWithDefinitionKey() {
    setupFullSpawnMocks(10.0);
    when(definitionKey.getNamespace()).thenReturn("relluessentials");
    when(definitionKey.toString()).thenReturn("relluessentials:zombie_boss");

    mobService.spawnMob(definition, location);

    NamespacedKey namespacedKey = NamespacedKey.fromString("relluessentials:mob_definition_key");
    assertNotNull(namespacedKey);
    verify(persistentDataContainer).set(eq(namespacedKey),
        eq(PersistentDataType.STRING), eq("relluessentials:zombie_boss"));
  }

  @Test
  void spawnMobRegistersSpawnedMobInRegistry() {
    setupFullSpawnMocks(10.0);

    mobService.spawnMob(definition, location);

    verify(mobRegistry).registerSpawnedMob(any(CustomMob.class));
  }

  @Test
  void countSpawnedMobsReturnsTotalCount() {
    CustomMob mob1 = mock(CustomMob.class);
    CustomMob mob2 = mock(CustomMob.class);
    when(mobRegistry.findAllSpawned()).thenReturn(List.of(mob1, mob2));

    int count = mobService.countSpawnedMobs();

    assertEquals(2, count);
  }

  @Test
  void countAliveMobsReturnsAliveCount() {
    CustomMob mob1 = mock(CustomMob.class);
    when(mobRegistry.findAllSpawnedAlive()).thenReturn(List.of(mob1));

    int count = mobService.countAliveMobs();

    assertEquals(1, count);
  }

  @Test
  void despawnAllRemovesAllAliveMobsAndUnregistersAll() {
    LivingEntity aliveLivingEntity = mock(LivingEntity.class);
    CustomMob aliveMob = mock(CustomMob.class);
    UUID aliveUuid = UUID.randomUUID();

    LivingEntity deadLivingEntity = mock(LivingEntity.class);
    CustomMob deadMob = mock(CustomMob.class);
    UUID deadUuid = UUID.randomUUID();

    when(aliveMob.getLivingEntity()).thenReturn(aliveLivingEntity);
    when(deadMob.getLivingEntity()).thenReturn(deadLivingEntity);
    when(aliveLivingEntity.getUniqueId()).thenReturn(aliveUuid);
    when(deadLivingEntity.getUniqueId()).thenReturn(deadUuid);

    when(mobRegistry.findAllSpawnedAlive()).thenReturn(List.of(aliveMob));
    when(mobRegistry.findAllSpawned()).thenReturn(List.of(aliveMob, deadMob));

    mobService.despawnAll();

    verify(aliveLivingEntity).remove();
    verify(deadLivingEntity, never()).remove();
    verify(mobRegistry).unregisterSpawnedMob(aliveUuid);
    verify(mobRegistry).unregisterSpawnedMob(deadUuid);
  }

  @Test
  void despawnAllDoesNothingWhenNoMobsSpawned() {
    when(mobRegistry.findAllSpawnedAlive()).thenReturn(List.of());
    when(mobRegistry.findAllSpawned()).thenReturn(List.of());

    assertDoesNotThrow(() -> mobService.despawnAll());

    verify(mobRegistry, never()).unregisterSpawnedMob(any());
  }

  @Test
  void unregisterDefinitionDelegatesToRegistry() {
    mobService.unregisterDefinition(definitionKey);

    verify(mobRegistry).unregisterDefinition(definitionKey);
  }

  @Test
  void findAllDefinitionsReturnsList() {
    when(mobRegistry.findAllDefinitions()).thenReturn(List.of(definition));

    List<CustomMobDefinition> result = mobService.findAllDefinitions();

    assertEquals(1, result.size());
    assertEquals(definition, result.getFirst());
  }

  @Test
  void unregisterSpawnedMobDelegatesToRegistry() {
    UUID uuid = UUID.randomUUID();

    mobService.unregisterSpawnedMob(uuid);

    verify(mobRegistry).unregisterSpawnedMob(uuid);
  }

  @Test
  void findAllSpawnedReturnsList() {
    CustomMob mob = mock(CustomMob.class);
    when(mobRegistry.findAllSpawned()).thenReturn(List.of(mob));

    List<CustomMob> result = mobService.findAllSpawned();

    assertEquals(1, result.size());
    assertEquals(mob, result.getFirst());
  }

  @Test
  void findAllSpawnedAliveReturnsList() {
    CustomMob mob = mock(CustomMob.class);
    when(mobRegistry.findAllSpawnedAlive()).thenReturn(List.of(mob));

    List<CustomMob> result = mobService.findAllSpawnedAlive();

    assertEquals(1, result.size());
    assertEquals(mob, result.getFirst());
  }

  @Test
  void findAllSpawnedByDefinitionKeyReturnsList() {
    CustomMob mob = mock(CustomMob.class);
    when(mobRegistry.findAllSpawnedByDefinitionKey(definitionKey)).thenReturn(List.of(mob));

    List<CustomMob> result = mobService.findAllSpawnedByDefinitionKey(definitionKey);

    assertEquals(1, result.size());
    assertEquals(mob, result.getFirst());
  }

  @Test
  void findAllSpawnedByDefinitionKeyReturnsEmptyWhenNoneMatch() {
    when(mobRegistry.findAllSpawnedByDefinitionKey(definitionKey)).thenReturn(List.of());

    List<CustomMob> result = mobService.findAllSpawnedByDefinitionKey(definitionKey);

    assertTrue(result.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 5, 10})
  void countSpawnedMobsReturnsCorrectCount(int count) {
    List<CustomMob> mobs = createMockMobList(count);
    when(mobRegistry.findAllSpawned()).thenReturn(mobs);

    assertEquals(count, mobService.countSpawnedMobs());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 3, 7})
  void countAliveMobsReturnsCorrectCount(int count) {
    List<CustomMob> mobs = createMockMobList(count);
    when(mobRegistry.findAllSpawnedAlive()).thenReturn(mobs);

    assertEquals(count, mobService.countAliveMobs());
  }

  @Test
  void spawnMobSkipsNbtTagWhenNamespacedKeyIsInvalid() {
    setupFullSpawnMocks(10.0);
    when(definitionKey.getNamespace()).thenReturn("INVALID NAMESPACE!!!");

    mobService.spawnMob(definition, location);

    verify(persistentDataContainer, never()).set(any(), any(), any());
  }

  private List<CustomMob> createMockMobList(int size) {
    return java.util.stream.IntStream
        .range(0, size)
        .mapToObj(_ -> mock(CustomMob.class))
        .toList();
  }

  private void setupFullSpawnMocks(double health) {
    when(location.getWorld()).thenReturn(world);
    when(world.spawnEntity(location, EntityType.ZOMBIE)).thenReturn(livingEntity);
    when(definition.entityType()).thenReturn(EntityType.ZOMBIE);
    when(definition.customName()).thenReturn("TestMob");
    when(definition.isCustomNameVisible()).thenReturn(true);
    when(definition.health()).thenReturn(health);
    when(definition.potionEffects()).thenReturn(List.of());
    when(definition.invisible()).thenReturn(false);
    when(definition.canPickUpItems()).thenReturn(false);
    when(definition.equipment()).thenReturn(equipment);
    when(definition.key()).thenReturn(definitionKey);
    when(livingEntity.getEquipment()).thenReturn(entityEquipment);
    lenient().when(livingEntity.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(definitionKey.getNamespace()).thenReturn("relluessentials");
    lenient().when(definitionKey.toString()).thenReturn("relluessentials:zombie_boss");
  }

  private void setupFullSpawnMocksWithNullEquipment() {
    when(location.getWorld()).thenReturn(world);
    when(world.spawnEntity(location, EntityType.ZOMBIE)).thenReturn(livingEntity);
    when(definition.entityType()).thenReturn(EntityType.ZOMBIE);
    when(definition.customName()).thenReturn("TestMob");
    when(definition.isCustomNameVisible()).thenReturn(true);
    when(definition.health()).thenReturn(10.0);
    when(definition.potionEffects()).thenReturn(List.of());
    when(definition.invisible()).thenReturn(false);
    when(definition.canPickUpItems()).thenReturn(false);
    when(definition.equipment()).thenReturn(equipment);
    when(definition.key()).thenReturn(definitionKey);
    when(livingEntity.getEquipment()).thenReturn(null);
    when(livingEntity.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(definitionKey.getNamespace()).thenReturn("relluessentials");
    when(definitionKey.toString()).thenReturn("relluessentials:zombie_boss");
  }
}