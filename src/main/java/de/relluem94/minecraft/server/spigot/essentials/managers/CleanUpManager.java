package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class CleanUpManager implements IDisable {

    @Override
    public void disable() {
        RelluEssentials.locationTypeEntryList.clear();
        RelluEssentials.blockHistoryList.clear();
        RelluEssentials.groupEntryList.clear();
        RelluEssentials.selections.clear();
    }
}