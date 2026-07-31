package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;

public class CleanUpManager implements Disable {

  @Override
  public void disable() {
    RelluEssentials.getInstance().locationTypeEntryList.clear();
    RelluEssentials.getInstance().groupEntryList.clear();
  }
}