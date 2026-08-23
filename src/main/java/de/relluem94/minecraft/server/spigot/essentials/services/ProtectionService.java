package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.ProtectionRepository;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Service responsible for managing block protections, coordinating between the
 * {@link ProtectionRegistry} for in-memory access and {@link ProtectionRepository} for persistence.
 */
public class ProtectionService {

  private final ProtectionRegistry protectionRegistry;
  private final ProtectionRepository protectionRepository;
  private final ServiceContext serviceContext;

  /**
   * Constructs a new ProtectionService.
   *
   * @param protectionLocksEntryList List of existing protection locks.
   * @param protectionEntryMap Map of locations to their respective protection entries.
   * @param protectionRepository The repository used for database operations.
   * @param serviceContext The global service context.
   */
  public ProtectionService(List<ProtectionLockEntry> protectionLocksEntryList,
      Map<Location, ProtectionEntry> protectionEntryMap, ProtectionRepository protectionRepository,
      ServiceContext serviceContext) {
    this.protectionRegistry = new ProtectionRegistry(protectionLocksEntryList, protectionEntryMap);
    this.protectionRepository = protectionRepository;
    this.serviceContext = serviceContext;
  }

  /**
   * Removes protection from a specific block if it exists, effectively
   * allowing explosions to occur.
   *
   * @param block The block to check for protection.
   * @return {@code true} if protection was found and removed, {@code false} otherwise.
   */
  public boolean removeExplodedBlockProtectionOrCancelExplosion(Block block) {
    ProtectionEntry protection = protectionRegistry.getProtectionEntry(block.getLocation());
    if (protection != null) {
      protectionRepository.remove(protection);
      protectionRegistry.removeProtectionEntry(block.getLocation());
      return true;
    }
    return false;
  }

  /**
   * Retrieves the protection entry associated with a specific location from the registry.
   *
   * @param location The location to check.
   * @return The {@link ProtectionEntry} if found, or {@code null} otherwise.
   */
  public ProtectionEntry getProtectionEntry(Location location) {
    return protectionRegistry.getProtectionEntry(location);
  }

  /**
   * Removes a protection entry from the in-memory registry.
   *
   * @param location The location of the protection to remove.
   */
  public void removeProtectionEntry(Location location) {
    protectionRegistry.removeProtectionEntry(location);
  }

  /**
   * Adds or updates a protection entry in the in-memory registry.
   *
   * @param location The location of the protection.
   * @param protectionEntry The protection entry to store.
   */
  public void putProtectionEntry(Location location, ProtectionEntry protectionEntry) {
    protectionRegistry.putProtectionEntry(location, protectionEntry);
  }

  /**
   * Retrieves all protection entries owned by a specific player.
   *
   * @param playerId The unique ID of the player.
   * @return A list of {@link ProtectionEntry} objects owned by the player.
   */
  public List<ProtectionEntry> getProtectionEntriesOwnedBy(long playerId) {
    return protectionRegistry.getProtectionEntriesOwnedBy(playerId);
  }

  /**
   * Retrieves all protection entries currently held in the registry.
   *
   * @return A map of locations to their respective {@link ProtectionEntry}.
   */
  public Map<Location, ProtectionEntry> getAllProtectionEntries() {
    return protectionRegistry.getProtectionEntryList();
  }

  /**
   * Checks if a specific material is eligible for protection.
   *
   * @param material The material to check.
   * @return {@code true} if the material can be protected, {@code false} otherwise.
   */
  public boolean isProtectableMaterial(Material material) {
    return protectionRegistry.isProtectableMaterial(material);
  }

  /**
   * Persists a new protection entry to the database and adds it to the registry.
   *
   * @param location The location of the protection.
   * @param protectionEntry The protection entry to save.
   */
  public void saveProtectionAndAddToRegistry(Location location, ProtectionEntry protectionEntry) {
    protectionRepository.save(protectionEntry);
    ProtectionEntry savedProtectionEntry = protectionRepository.findByLocation(location);
    if (savedProtectionEntry != null) {
      protectionRegistry.putProtectionEntry(location, savedProtectionEntry);
    }
  }

  /**
   * Updates the protection flags in the database and refreshes the registry.
   *
   * @param protectionEntry The protection entry containing updated flags.
   */
  public void updateProtectionFlags(ProtectionEntry protectionEntry) {
    protectionRepository.updateFlags(protectionEntry);
    protectionRegistry.putProtectionEntry(protectionEntry.getLocationEntry().getLocation(),
        protectionEntry);
  }

  /**
   * Updates the protection rights in the database and refreshes the registry.
   *
   * @param protectionEntry The protection entry containing updated rights.
   */
  public void updateProtectionRights(ProtectionEntry protectionEntry) {
    protectionRepository.updateRights(protectionEntry);
    protectionRegistry.putProtectionEntry(protectionEntry.getLocationEntry().getLocation(),
        protectionEntry);
  }

  /**
   * Searches for a protection entry in the database by its location.
   *
   * @param location The location to search for.
   * @return The {@link ProtectionEntry} if found, or {@code null} otherwise.
   */
  @SuppressWarnings("unused")
  public ProtectionEntry findProtectionByLocation(Location location) {
    return protectionRepository.findByLocation(location);
  }

  /**
   * Removes a protection entry from both the database and the in-memory registry.
   *
   * @param protection The protection entry to delete.
   */
  public void deleteProtectionAndRemoveFromRegistry(ProtectionEntry protection) {
    protectionRepository.remove(protection);
    protectionRegistry.removeProtectionEntry(protection.getLocationEntry().getLocation());
  }

  /**
   * Removes outdated protections from the database and cleans up the registry.
   *
   * @return The number of protections that were removed.
   */
  public int removeOutdatedProtectionsFromDatabaseAndRegistry() {
    List<Long> deletedIds = protectionRepository.removeOutdatedProtections();
    protectionRegistry.removeProtectionEntriesByIds(deletedIds);
    return deletedIds.size();
  }

  /**
   * Determines if a player owns a specific protection entry.
   *
   * @param protectionEntry The protection entry to check.
   * @param player The player to check ownership for.
   * @return {@code true} if the player is the owner or if the protection
   *     is null, {@code false} otherwise.
   */
  public boolean playerOwnsProtection(ProtectionEntry protectionEntry, Player player) {
    PlayerEntry playerEntry = serviceContext.getPlayerService().getPlayerEntry(player);
    if (protectionEntry != null) {
      return protectionEntry.getLocationEntry().getPlayerId() == playerEntry.getId();
    }
    return true;
  }

  /**
   * Removes protection from a block if the material is protectable and protection exists.
   *
   * @param block The block to check and potentially remove protection from.
   */
  public void removeBlockProtectionIfExists(Block block) {
    if (!isProtectableMaterial(block.getType())) {
      return;
    }
    ProtectionEntry protection = getProtectionEntry(block.getLocation());
    if (protection != null) {
      deleteProtectionAndRemoveFromRegistry(protection);
    }
  }

}