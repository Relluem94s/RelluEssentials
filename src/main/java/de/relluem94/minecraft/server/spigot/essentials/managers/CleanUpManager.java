package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class CleanUpManager implements IDisable {

    @Override
    public void disable() {
        RelluEssentials.getInstance().locationTypeEntryList.clear();
        RelluEssentials.getInstance().blockHistoryList.clear();
        RelluEssentials.getInstance().groupEntryList.clear();
        RelluEssentials.getInstance().selections.clear();
    }
}