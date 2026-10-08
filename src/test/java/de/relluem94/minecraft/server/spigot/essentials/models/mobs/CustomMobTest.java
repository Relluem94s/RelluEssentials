package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import java.util.UUID;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomMobTest {

  private final EntityType entityType = EntityType.ZOMBIE;
  @Mock
  private LivingEntity livingEntity;
  @Mock
  private RelluEssentialsNamespacedKey definitionKey;

  @Test
  void constructorMapsAllFieldsCorrectly() {
    UUID entityUuid = UUID.randomUUID();
    when(livingEntity.getUniqueId()).thenReturn(entityUuid);

    CustomMob customMob = new CustomMob(livingEntity, entityType, definitionKey);

    assertEquals(entityUuid, customMob.getEntityUuid());
    assertEquals(entityType, customMob.getEntityType());
    assertEquals(livingEntity, customMob.getLivingEntity());
    assertEquals(definitionKey, customMob.getDefinitionKey());
  }

  @Test
  void constructorExtractsUuidFromLivingEntity() {
    UUID entityUuid = UUID.randomUUID();
    when(livingEntity.getUniqueId()).thenReturn(entityUuid);

    CustomMob customMob = new CustomMob(livingEntity, entityType, definitionKey);

    assertEquals(entityUuid, customMob.getEntityUuid());
  }

  @Test
  void isAliveReturnsTrueWhenEntityIsValidAndNotDead() {
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.isValid()).thenReturn(true);
    when(livingEntity.isDead()).thenReturn(false);

    CustomMob customMob = new CustomMob(livingEntity, entityType, definitionKey);

    assertTrue(customMob.isAlive());
  }

  @Test
  void isAliveReturnsFalseWhenEntityIsNotValid() {
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.isValid()).thenReturn(false);

    CustomMob customMob = new CustomMob(livingEntity, entityType, definitionKey);

    assertFalse(customMob.isAlive());
  }

  @Test
  void isAliveReturnsFalseWhenEntityIsDead() {
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.isValid()).thenReturn(true);
    when(livingEntity.isDead()).thenReturn(true);

    CustomMob customMob = new CustomMob(livingEntity, entityType, definitionKey);

    assertFalse(customMob.isAlive());
  }

  @Test
  void isAliveReturnsFalseWhenEntityIsNotValidAndIsDead() {
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.isValid()).thenReturn(false);

    CustomMob customMob = new CustomMob(livingEntity, entityType, definitionKey);

    assertFalse(customMob.isAlive());
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void constructorThrowsNullPointerExceptionWhenLivingEntityIsNull() {
    assertThrows(NullPointerException.class, () -> new CustomMob(null, entityType, definitionKey));
  }
}