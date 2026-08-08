package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ProtectionRegistry;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ProtectionService {

  private final ProtectionRegistry protectionRegistry;
  private final DatabaseHelper databaseHelper;

  public ProtectionService(List<ProtectionLockEntry> protectionLocksEntryList,
      Map<Location, ProtectionEntry> protectionEntryMap, DatabaseHelper databaseHelper) {
    this.protectionRegistry = new ProtectionRegistry(protectionLocksEntryList, protectionEntryMap);
    this.databaseHelper = databaseHelper;
  }

  public void deleteProtectionAndRemoveFromRegistry(ProtectionEntry protection) {
    databaseHelper.deleteProtection(protection);
    protectionRegistry.removeProtectionEntry(protection.getLocationEntry().getLocation());
  }

  public boolean removeExplodedBlockProtectionOrCancelExplosion(Block block) {
    ProtectionEntry protection = protectionRegistry.getProtectionEntry(block.getLocation());
    if (protection != null) {
      databaseHelper.deleteProtection(protection);
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
}