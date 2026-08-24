package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import de.relluem94.rellulib.stores.DoubleStore;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionRegistryTest {

  private PositionRegistry positionRegistry;
  private Player mockPlayer;
  private DoubleStore<Location, Location> mockStore;

  @BeforeEach
  void setUp() {
    positionRegistry = new PositionRegistry();
    mockPlayer = mock(Player.class);
    mockStore = mock(DoubleStore.class);
  }

  @Test
  void putShouldStoreLocationStoreForPlayer() {
    positionRegistry.put(mockPlayer, mockStore);

    assertTrue(positionRegistry.contains(mockPlayer));
    assertEquals(mockStore, positionRegistry.getAll().get(mockPlayer));
  }

  @Test
  void removeShouldRemoveLocationStoreForPlayer() {
    positionRegistry.put(mockPlayer, mockStore);
    positionRegistry.remove(mockPlayer);

    assertFalse(positionRegistry.contains(mockPlayer));
    assertFalse(positionRegistry.getAll().containsKey(mockPlayer));
  }

  @Test
  void containsShouldReturnCorrectBoolean() {
    assertFalse(positionRegistry.contains(mockPlayer));

    positionRegistry.put(mockPlayer, mockStore);

    assertTrue(positionRegistry.contains(mockPlayer));
  }

  @Test
  void getAllShouldReturnAllStoredEntries() {
    Player secondMockPlayer = mock(Player.class);
    DoubleStore<Location, Location> secondMockStore = mock(DoubleStore.class);

    positionRegistry.put(mockPlayer, mockStore);
    positionRegistry.put(secondMockPlayer, secondMockStore);

    Map<Player, DoubleStore<Location, Location>> allEntries = positionRegistry.getAll();

    assertEquals(2, allEntries.size());
    assertEquals(mockStore, allEntries.get(mockPlayer));
    assertEquals(secondMockStore, allEntries.get(secondMockPlayer));
  }
}