package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;

public class ItemRegistryService {

  private final RelluEssentials plugin;

  public ItemRegistryService(RelluEssentials plugin) {
    this.plugin = plugin;
  }

  public ItemHelper findByNamespace(String namespace) {
    return ItemRegistry.find(RegistryKey.of(plugin, namespace)).orElseThrow();
  }
}