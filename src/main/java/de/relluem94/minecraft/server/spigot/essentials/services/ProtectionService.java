package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.ProtectionRepository;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ProtectionService {

  private final ProtectionRegistry protectionRegistry;
  private final ProtectionRepository protectionRepository;

  public ProtectionService(List<ProtectionLockEntry> protectionLocksEntryList,
      Map<Location, ProtectionEntry> protectionEntryMap, ProtectionRepository protectionRepository) {
    this.protectionRegistry = new ProtectionRegistry(protectionLocksEntryList, protectionEntryMap);
    this.protectionRepository = protectionRepository;
  }

  public boolean removeExplodedBlockProtectionOrCancelExplosion(Block block) {
    ProtectionEntry protection = protectionRegistry.getProtectionEntry(block.getLocation());
    if (protection != null) {
      protectionRepository.remove(protection);
      protectionRegistry.removeProtectionEntry(block.getLocation());
      return true;
    }
    return false;
  }

  public ProtectionEntry getProtectionEntry(Location location) {
    return protectionRegistry.getProtectionEntry(location);
  }

  public void removeProtectionEntry(Location location) {
    protectionRegistry.removeProtectionEntry(location);
  }

  public void putProtectionEntry(Location location, ProtectionEntry protectionEntry) {
    protectionRegistry.putProtectionEntry(location, protectionEntry);
  }

  public List<ProtectionEntry> getProtectionEntriesOwnedBy(long playerId) {
    return protectionRegistry.getProtectionEntriesOwnedBy(playerId);
  }

  public Map<Location, ProtectionEntry> getAllProtectionEntries() {
    return protectionRegistry.getProtectionEntryList();
  }

  public boolean isProtectableMaterial(Material material) {
    return protectionRegistry.isProtectableMaterial(material);
  }

  public void saveProtectionAndAddToRegistry(Location location, ProtectionEntry protectionEntry) {
    protectionRepository.save(protectionEntry);
    ProtectionEntry savedProtectionEntry = protectionRepository.findByLocation(location);
    if (savedProtectionEntry != null) {
      protectionRegistry.putProtectionEntry(location, savedProtectionEntry);
    }
  }

  public void updateProtectionFlags(ProtectionEntry protectionEntry) {
    protectionRepository.updateFlags(protectionEntry);
    protectionRegistry.putProtectionEntry(protectionEntry.getLocationEntry().getLocation(), protectionEntry);
  }

  public void updateProtectionRights(ProtectionEntry protectionEntry) {
    protectionRepository.updateRights(protectionEntry);
    protectionRegistry.putProtectionEntry(protectionEntry.getLocationEntry().getLocation(), protectionEntry);
  }

  public ProtectionEntry findProtectionByLocation(Location location) {
    return protectionRepository.findByLocation(location);
  }

  public void deleteProtectionAndRemoveFromRegistry(ProtectionEntry protection) {
    protectionRepository.remove(protection);
    protectionRegistry.removeProtectionEntry(protection.getLocationEntry().getLocation());
  }

  public int removeOutdatedProtectionsFromDatabaseAndRegistry() {
    List<Long> deletedIds = protectionRepository.removeOutdatedProtections();
    protectionRegistry.removeProtectionEntriesByIds(deletedIds);
    return deletedIds.size();
  }
}