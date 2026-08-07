package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import de.relluem94.minecraft.server.spigot.essentials.registries.RelluEssentialsRegistry;

public interface RelluEssentialsIntegration {

  String getPluginName();

  String getPluginVersion();

  void onRelluEssentialsInit(RelluEssentialsRegistry api);

  void onRelluEssentialsShutdown();
}