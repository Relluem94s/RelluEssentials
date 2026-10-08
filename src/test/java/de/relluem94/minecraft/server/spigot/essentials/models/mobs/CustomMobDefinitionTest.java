package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomMobDefinitionTest {

  @Mock
  private RelluEssentialsNamespacedKey mockedKey;

  @Mock
  private PotionEffect mockedPotionEffect;

  @Mock
  private CustomMobEquipment mockedEquipment;

  private Collection<PotionEffect> potionEffects;

  @BeforeEach
  void setUp() {
    potionEffects = List.of(mockedPotionEffect);
  }

  @Test
  void keyReturnsCorrectKey() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertEquals(mockedKey, definition.key());
  }

  @Test
  void entityTypeReturnsCorrectEntityType() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertEquals(EntityType.ZOMBIE, definition.entityType());
  }

  @Test
  void customNameReturnsCorrectName() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertEquals("TestMob", definition.customName());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void isCustomNameVisibleReturnsCorrectValue(boolean isVisible) {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", isVisible, potionEffects, mockedEquipment,
            20.0, true, false);

    assertEquals(isVisible, definition.isCustomNameVisible());
  }

  @Test
  void potionEffectsReturnsCorrectCollection() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertEquals(potionEffects, definition.potionEffects());
  }

  @Test
  void equipmentReturnsCorrectEquipment() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertEquals(mockedEquipment, definition.equipment());
  }

  @Test
  void healthReturnsCorrectHealth() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertEquals(20.0, definition.health());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void canPickUpItemsReturnsCorrectValue(boolean canPickUp) {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            canPickUp, false);

    assertEquals(canPickUp, definition.canPickUpItems());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void invisibleReturnsCorrectValue(boolean invisible) {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, invisible);

    assertEquals(invisible, definition.invisible());
  }

  @Test
  void potionEffectsReturnsEmptyCollectionWhenNoEffectsProvided() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, List.of(), mockedEquipment, 20.0, true,
            false);

    assertTrue(definition
        .potionEffects()
        .isEmpty());
  }

  @Test
  void healthReturnsZeroWhenHealthIsSetToZero() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 0.0,
            true, false);

    assertEquals(0.0, definition.health());
  }

  @Test
  void customNameReturnsEmptyStringWhenNameIsEmpty() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "", true, potionEffects, mockedEquipment, 20.0, true,
            false);

    assertEquals("", definition.customName());
  }

  @Test
  void isCustomNameVisibleReturnsFalseWhenSetToFalse() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", false, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertFalse(definition.isCustomNameVisible());
  }

  @Test
  void canPickUpItemsReturnsFalseWhenSetToFalse() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            false, false);

    assertFalse(definition.canPickUpItems());
  }

  @Test
  void invisibleReturnsFalseWhenSetToFalse() {
    CustomMobDefinition definition =
        new CustomMobDefinition(mockedKey, EntityType.ZOMBIE, "TestMob", true, potionEffects, mockedEquipment, 20.0,
            true, false);

    assertFalse(definition.invisible());
  }
}