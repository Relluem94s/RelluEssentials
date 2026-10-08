package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomMobTest {

  @Mock
  private World world;

  @Mock
  private Location location;

  @Mock
  private LivingEntity livingEntity;

  @Mock
  private EntityEquipment entityEquipment;

  @Mock
  private ItemStack mainHand;

  @Mock
  private ItemStack offHand;

  @Mock
  private ItemStack helmet;

  @Mock
  private ItemStack chest;

  @Mock
  private ItemStack leggings;

  @Mock
  private ItemStack boots;

  @Mock
  private PotionEffect potionEffect;

  @Mock
  private PotionEffect secondPotionEffect;

  @BeforeEach
  void setUp() {
    lenient().when(location.getWorld()).thenReturn(world);
    lenient().when(world.spawnEntity(location, EntityType.ZOMBIE)).thenReturn(livingEntity);
    lenient().when(livingEntity.getHealth()).thenReturn(20.0);
  }

  @Test
  void spawnSetsCustomNameFromConstructor() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "TestName", true);

    customMob.spawn();

    verify(livingEntity).setCustomName("TestName");
  }

  @Test
  void spawnUsesEntityTypeNameWhenCustomNameIsNull() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, null, true);

    customMob.spawn();

    verify(livingEntity).setCustomName(EntityType.ZOMBIE.name());
  }

  @Test
  void spawnSetsCustomNameVisibleTrue() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn();

    verify(livingEntity).setCustomNameVisible(true);
  }

  @Test
  void spawnSetsCustomNameVisibleFalse() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", false);

    customMob.spawn();

    verify(livingEntity).setCustomNameVisible(false);
  }

  @Test
  void spawnSetsHealthToEntityHealthWhenHealthIsZero() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn();

    verify(livingEntity).setHealth(20.0);
  }

  @Test
  void spawnSetsConfiguredHealthEvenWhenExceedingEntityDefaultHealth() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.setHealth(100.0);

    customMob.spawn();

    verify(livingEntity).setHealth(100.0);
  }

  @Test
  void spawnSetsConfiguredHealthWhenLowerThanEntityHealth() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.setHealth(10.0);

    customMob.spawn();

    verify(livingEntity).setHealth(10.0);
  }

  @Test
  void spawnAppliesPotionEffects() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.addPotionEffect(potionEffect);

    customMob.spawn();

    verify(livingEntity).addPotionEffects(argThat(effects -> effects.contains(potionEffect) && effects.size() == 1));
  }

  @Test
  void spawnAppliesCollectionOfPotionEffects() {
    Collection<PotionEffect> effects = List.of(potionEffect, secondPotionEffect);
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.addPotionEffect(effects);

    customMob.spawn();

    verify(livingEntity).addPotionEffects(argThat(actual -> actual.containsAll(effects) && actual.size() == effects.size()));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void spawnSetsInvisibility(boolean invisible) {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.setInvisible(invisible);

    customMob.spawn();

    verify(livingEntity).setInvisible(invisible);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void spawnSetsCanPickupItems(boolean canPickup) {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.setCanPickupItems(canPickup);

    customMob.spawn();

    verify(livingEntity).setCanPickupItems(canPickup);
  }

  @Test
  void spawnDoesNothingWhenWorldIsNull() {
    when(location.getWorld()).thenReturn(null);
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn();

    verify(world, never()).spawnEntity(location, EntityType.ZOMBIE);
  }

  @Test
  void spawnWithEquipmentSetsAllItems() {
    when(livingEntity.getEquipment()).thenReturn(entityEquipment);
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn(mainHand, offHand, helmet, chest, leggings, boots);

    verify(entityEquipment).setItemInMainHand(mainHand);
    verify(entityEquipment).setItemInOffHand(offHand);
    verify(entityEquipment).setHelmet(helmet);
    verify(entityEquipment).setChestplate(chest);
    verify(entityEquipment).setLeggings(leggings);
    verify(entityEquipment).setBoots(boots);
  }

  @Test
  void spawnWithEquipmentSkipsEquipmentWhenEquipmentSlotIsNull() {
    when(livingEntity.getEquipment()).thenReturn(null);
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn(mainHand, offHand, helmet, chest, leggings, boots);

    verify(entityEquipment, never()).setItemInMainHand(mainHand);
    verify(entityEquipment, never()).setItemInOffHand(offHand);
    verify(entityEquipment, never()).setHelmet(helmet);
    verify(entityEquipment, never()).setChestplate(chest);
    verify(entityEquipment, never()).setLeggings(leggings);
    verify(entityEquipment, never()).setBoots(boots);
  }

  @Test
  void spawnWithEquipmentStillSpawnsEntityWhenEquipmentIsNull() {
    when(livingEntity.getEquipment()).thenReturn(null);
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn(mainHand, offHand, helmet, chest, leggings, boots);

    verify(world).spawnEntity(location, EntityType.ZOMBIE);
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void constructorThrowsNullPointerExceptionWhenEntityTypeIsNull() {
    assertThrows(NullPointerException.class, () -> new CustomMob(location, null, "Name", true));
  }

  @Test
  void spawnDefaultCanPickupItemsIsTrue() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn();

    verify(livingEntity).setCanPickupItems(true);
  }

  @Test
  void spawnDefaultIsNotInvisible() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn();

    verify(livingEntity).setInvisible(false);
  }

  @Test
  void spawnWithEquipmentDoesNothingWhenWorldIsNull() {
    when(location.getWorld()).thenReturn(null);
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);

    customMob.spawn(mainHand, offHand, helmet, chest, leggings, boots);

    verify(livingEntity, never()).setCustomName(any());
  }

  @Test
  void spawnSetsEntityTypeCorrectly() {
    when(world.spawnEntity(location, EntityType.SKELETON)).thenReturn(livingEntity);
    CustomMob customMob = new CustomMob(location, EntityType.SKELETON, "Name", true);

    customMob.spawn();

    verify(world).spawnEntity(location, EntityType.SKELETON);
  }

  @Test
  void addPotionEffectWithEmptyCollectionAppliesNoPotionEffects() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.addPotionEffect(List.of());

    customMob.spawn();

    verify(livingEntity).addPotionEffects(argThat(Collection::isEmpty));
  }

  @Test
  void healthRemainsAtEntityHealthWhenSetToExactEntityHealth() {
    CustomMob customMob = new CustomMob(location, EntityType.ZOMBIE, "Name", true);
    customMob.setHealth(20.0);

    customMob.spawn();

    verify(livingEntity).setHealth(20.0);
  }
}