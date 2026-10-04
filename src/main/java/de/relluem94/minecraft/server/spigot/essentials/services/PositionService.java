package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.PositionRegistry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Service responsible for managing and visualizing player position metadata.
 */
@RequiredArgsConstructor
public class PositionService {

  private final PositionRegistry positionRegistry;
  private final TranslationService translationService;

  /**
   * Checks if the specified player has any positions stored in the registry.
   *
   * @param player the player to check
   * @return true if positions exist, false otherwise
   */
  public boolean hasPositions(Player player) {
    return positionRegistry.contains(player);
  }

  /**
   * Ensures that position entries exist for the specified player.
   * If no positions exist, a new empty store is created.
   *
   * @param player the player for whom to ensure positions exist
   */
  public void ensurePositionsExist(Player player) {
    if (!positionRegistry.contains(player)) {
      positionRegistry.put(player, new DoubleStore<>(null, null));
    }
  }

  /**
   * Sets the first position for the specified player.
   *
   * @param player   the player
   * @param location the location to set as the first position
   */
  public void setFirstPosition(Player player, Location location) {
    DoubleStore<Location, Location> store = positionRegistry.contains(player)
        ? positionRegistry.getAll().get(player)
        : new DoubleStore<>(null, null);
    positionRegistry.put(player, new DoubleStore<>(location, store.getSecondValue()));
  }

  /**
   * Sets the second position for the specified player.
   *
   * @param player   the player
   * @param location the location to set as the second position
   */
  public void setSecondPosition(Player player, Location location) {
    DoubleStore<Location, Location> store = positionRegistry.contains(player)
        ? positionRegistry.getAll().get(player)
        : new DoubleStore<>(null, null);
    positionRegistry.put(player, new DoubleStore<>(store.getValue(), location));
  }

  /**
   * Removes the first position for the specified player.
   *
   * @param player the player
   */
  public void removeFirstPosition(Player player) {
    ensurePositionsExist(player);
    positionRegistry.getAll().get(player).setValue(null);
  }

  /**
   * Removes the second position for the specified player.
   *
   * @param player the player
   */
  public void removeSecondPosition(Player player) {
    ensurePositionsExist(player);
    positionRegistry.getAll().get(player).setSecondValue(null);
  }

  /**
   * Shifts both stored positions for the player by a given direction and amount.
   *
   * @param player    the player
   * @param direction the direction of the shift
   * @param amount    the distance to shift
   */
  public void shiftPositions(Player player, Vector direction, int amount) {
    ensurePositionsExist(player);
    DoubleStore<Location, Location> positions = positionRegistry.getAll().get(player);
    Location first = positions.getValue();
    Location second = positions.getSecondValue();

    if (first != null) {
      Vector offset = direction.clone().multiply(amount);
      first.setX(Math.round(first.getX() + offset.getX()));
      first.setY(Math.round(first.getY() + offset.getY()));
      first.setZ(Math.round(first.getZ() + offset.getZ()));
    }

    if (second != null) {
      Vector offset = direction.clone().multiply(amount);
      second.setX(Math.round(second.getX() + offset.getX()));
      second.setY(Math.round(second.getY() + offset.getY()));
      second.setZ(Math.round(second.getZ() + offset.getZ()));
    }
  }

  /**
   * Expands or decreases the distance between the two positions relative to the player.
   * The position further away from the player is the one being moved.
   *
   * @param player    the player
   * @param direction the direction to expand or decrease towards
   * @param amount    the distance to move
   * @param expand    true to expand the distance, false to decrease it
   */
  public void expandOrDecreasePositions(Player player, Vector direction, int amount,
      boolean expand) {
    DoubleStore<Location, Location> positions = positionRegistry.getAll().get(player);
    Location first = positions.getValue();
    Location second = positions.getSecondValue();

    Vector playerVec = player.getLocation().toVector();
    double projFirst = direction.dot(first.toVector().subtract(playerVec));
    double projSecond = direction.dot(second.toVector().subtract(playerVec));
    Location farther = projFirst >= projSecond ? first : second;

    int multiplier = expand ? 1 : -1;
    Vector offset = direction.clone().multiply(amount * multiplier);
    farther.setX(Math.round(farther.getX() + offset.getX()));
    farther.setY(Math.round(farther.getY() + offset.getY()));
    farther.setZ(Math.round(farther.getZ() + offset.getZ()));
  }

  /**
   * Removes all position data for the specified player.
   *
   * @param player the player
   */
  public void clearPositions(Player player) {
    positionRegistry.remove(player);
  }

  /**
   * Retrieves the stored positions for the specified player.
   *
   * @param player the player
   * @return the DoubleStore containing the two locations
   */
  public DoubleStore<Location, Location> getPositions(Player player) {
    return positionRegistry.getAll().get(player);
  }

  /**
   * Iterates through all registered positions and handles visual highlighting.
   * Also removes players from the registry if they are offline or have no valid positions.
   */
  public void tickHighlights() {
    List<Player> playersToRemove = new ArrayList<>();

    for (Map.Entry<Player, DoubleStore<Location, Location>> entry :
        positionRegistry.getAll().entrySet()) {
      Player player = entry.getKey();

      if (!player.isOnline()) {
        playersToRemove.add(player);
        continue;
      }

      DoubleStore<Location, Location> locationStore = entry.getValue();
      Location firstLocation = locationStore.getValue();
      Location secondLocation = locationStore.getSecondValue();

      if (firstLocation == null && secondLocation == null) {
        playersToRemove.add(player);
        continue;
      }

      highlightPositionsForPlayer(player, firstLocation, secondLocation);
    }

    playersToRemove.forEach(positionRegistry::remove);
  }

  private void highlightPositionsForPlayer(Player player,
      Location firstLocation, Location secondLocation) {
    if (firstLocation != null && secondLocation != null) {
      if (!Objects.equals(firstLocation.getWorld(), secondLocation.getWorld())) {
        player.sendMessage(translationService.getWithPrefix(
            MessageKey.COMMAND_POSITION_HIGHLIGHTING_DIFFERENT_WORLDS));
        return;
      }
      drawBoundingBox(player, firstLocation, secondLocation);
    } else if (firstLocation != null) {
      drawSingleBlockBox(player, firstLocation);
    } else {
      drawSingleBlockBox(player, secondLocation);
    }
  }

  private void drawBoundingBox(Player player, Location firstLocation, Location secondLocation) {
    World world = firstLocation.getWorld();
    double minX = Math.min(firstLocation.getX(), secondLocation.getX());
    double minY = Math.min(firstLocation.getY(), secondLocation.getY());
    double minZ = Math.min(firstLocation.getZ(), secondLocation.getZ());
    double maxX = Math.max(firstLocation.getX(), secondLocation.getX());
    double maxY = Math.max(firstLocation.getY(), secondLocation.getY());
    double maxZ = Math.max(firstLocation.getZ(), secondLocation.getZ());
    drawBoxEdges(player, world, minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
  }

  private void drawSingleBlockBox(Player player, Location location) {
    World world = location.getWorld();
    drawBoxEdges(player, world,
        location.getX(), location.getY(), location.getZ(),
        location.getX() + 1, location.getY() + 1, location.getZ() + 1);
  }

  private void drawBoxEdges(Player player, World world, double x1, double y1, double z1,
      double x2, double y2, double z2) {
    double step = 1.0;
    for (double x = x1; x <= x2; x += step) {
      spawnParticle(player, world, x, y1, z1);
      spawnParticle(player, world, x, y1, z2);
      spawnParticle(player, world, x, y2, z1);
      spawnParticle(player, world, x, y2, z2);
    }
    for (double z = z1; z <= z2; z += step) {
      spawnParticle(player, world, x1, y1, z);
      spawnParticle(player, world, x2, y1, z);
      spawnParticle(player, world, x1, y2, z);
      spawnParticle(player, world, x2, y2, z);
    }
    for (double y = y1; y <= y2; y += step) {
      spawnParticle(player, world, x1, y, z1);
      spawnParticle(player, world, x1, y, z2);
      spawnParticle(player, world, x2, y, z1);
      spawnParticle(player, world, x2, y, z2);
    }
  }

  private void spawnParticle(Player player, World world, double x, double y, double z) {
    player.spawnParticle(Particle.COMPOSTER, new Location(world, x, y, z), 1);
  }
}