package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorldGroupRegistryTest {

  private WorldGroupRegistry registry;

  @BeforeEach
  void setUp() {
    registry = new WorldGroupRegistry();
  }

  @Test
  void loadWorldsForSetting_ShouldOverwriteExistingWorlds() {
    registry.loadWorldsForSetting(WorldSetting.NPC_BANKER, Set.of("world1"));
    registry.loadWorldsForSetting(WorldSetting.NPC_BANKER, Set.of("world2", "world3"));

    Set<String> worlds = registry.getWorldsWithActiveSetting(WorldSetting.NPC_BANKER);

    assertEquals(2, worlds.size());
    assertTrue(worlds.contains("world2"));
    assertTrue(worlds.contains("world3"));
    assertFalse(worlds.contains("world1"));
  }

  @Test
  void isSettingActiveForWorld_ShouldReturnTrueIfWorldIsPresent() {
    registry.addWorldToSetting(WorldSetting.ORE_RESPAWN, "mining_world");

    assertTrue(registry.isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, "mining_world"));
  }

  @Test
  void isSettingActiveForWorld_ShouldReturnFalseIfWorldIsNotPresent() {
    registry.addWorldToSetting(WorldSetting.ORE_RESPAWN, "mining_world");

    assertFalse(registry.isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, "other_world"));
    assertFalse(registry.isSettingActiveForWorld(WorldSetting.COLLECT_BAG, "mining_world"));
  }

  @Test
  void isSettingActiveForWorld_ShouldReturnFalseForEmptyRegistry() {
    assertFalse(registry.isSettingActiveForWorld(WorldSetting.NPC_BEEKEEPER, "any_world"));
  }

  @Test
  void getWorldsWithActiveSetting_ShouldReturnUnmodifiableSet() {
    registry.addWorldToSetting(WorldSetting.DEATH_LOSE_COINS, "death_world");
    Set<String> worlds = registry.getWorldsWithActiveSetting(WorldSetting.DEATH_LOSE_COINS);

    assertThrows(UnsupportedOperationException.class, () -> worlds.add("new_world"));
  }

  @Test
  void getWorldsWithActiveSetting_ShouldReturnEmptySetIfNoWorldsRegistered() {
    Set<String> worlds = registry.getWorldsWithActiveSetting(WorldSetting.SCOREBOARD_SHOW);
    assertTrue(worlds.isEmpty());
  }

  @Test
  void addWorldToSetting_ShouldAddWorldToExistingOrNewSetting() {
    registry.addWorldToSetting(WorldSetting.USE_CLOUDSAILOR, "cloud_world");
    registry.addWorldToSetting(WorldSetting.USE_CLOUDSAILOR, "sky_world");

    Set<String> worlds = registry.getWorldsWithActiveSetting(WorldSetting.USE_CLOUDSAILOR);

    assertEquals(2, worlds.size());
    assertTrue(worlds.containsAll(Set.of("cloud_world", "sky_world")));
  }

  @Test
  void removeWorldFromSetting_ShouldRemoveOnlySpecifiedWorld() {
    registry.addWorldToSetting(WorldSetting.PROTECTION_NOTIFY_SELF, "world1");
    registry.addWorldToSetting(WorldSetting.PROTECTION_NOTIFY_SELF, "world2");

    registry.removeWorldFromSetting(WorldSetting.PROTECTION_NOTIFY_SELF, "world1");

    Set<String> worlds = registry.getWorldsWithActiveSetting(WorldSetting.PROTECTION_NOTIFY_SELF);
    assertEquals(1, worlds.size());
    assertTrue(worlds.contains("world2"));
    assertFalse(worlds.contains("world1"));
  }

  @Test
  void removeWorldFromAllSettings_ShouldRemoveWorldFromEverySetting() {
    registry.addWorldToSetting(WorldSetting.NPC_ENCHANTER, "target_world");
    registry.addWorldToSetting(WorldSetting.NPC_BAGSALESMAN, "target_world");
    registry.addWorldToSetting(WorldSetting.NPC_ENCHANTER, "other_world");

    registry.removeWorldFromAllSettings("target_world");

    assertFalse(registry.isSettingActiveForWorld(WorldSetting.NPC_ENCHANTER, "target_world"));
    assertFalse(registry.isSettingActiveForWorld(WorldSetting.NPC_BAGSALESMAN, "target_world"));
    assertTrue(registry.isSettingActiveForWorld(WorldSetting.NPC_ENCHANTER, "other_world"));
  }

  @Test
  void removeWorldFromAllSettings_ShouldNotAffectOtherWorlds() {
    registry.addWorldToSetting(WorldSetting.ENTITIES_DROP_COINS, "world1");

    registry.removeWorldFromAllSettings("non_existent_world");

    assertTrue(registry.isSettingActiveForWorld(WorldSetting.ENTITIES_DROP_COINS, "world1"));
  }
}