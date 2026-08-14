package de.relluem94.minecraft.server.spigot.essentials.interfaces.helpers;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;

public interface IPatchHelper {

  void applyPatch(int currentVersion);
  PluginInformationEntry loadPluginInformation();
}
