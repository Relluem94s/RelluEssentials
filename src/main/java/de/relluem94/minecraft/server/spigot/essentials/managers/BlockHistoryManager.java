package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHistoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class BlockHistoryManager implements IEnable {

    @Override
    public void enable() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskTimer(RelluEssentials.getInstance(), () -> {
            if (!RelluEssentials.getInstance().blockHistoryList.isEmpty()) {
                BlockHistoryEntry bh = RelluEssentials.getInstance().blockHistoryList.get(0);
                BlockHistoryHelper.setBlock(bh);
                if (bh.getDeleted() == null) {
                    RelluEssentials.getInstance().getDatabaseHelper().insertBlockHistory(bh);
                } else {
                    RelluEssentials.getInstance().getDatabaseHelper().deleteBlockHistory(bh);
                }
                RelluEssentials.getInstance().blockHistoryList.remove(0);
            }
        }, 0L, 2L);
    }
    
}
