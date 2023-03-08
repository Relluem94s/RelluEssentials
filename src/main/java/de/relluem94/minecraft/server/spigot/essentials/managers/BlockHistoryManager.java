package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class BlockHistoryManager implements Manager {

    @Override
    public void manage() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(RelluEssentials.getInstance(), () -> {
            if (!RelluEssentials.blockHistoryList.isEmpty()) {
                BlockHistoryEntry bh = RelluEssentials.blockHistoryList.get(0);
                BlockHelper.setBlock(bh);
                if (bh.getDeleted() == null) {
                    RelluEssentials.dBH.insertBlockHistory(bh);
                } else {
                    RelluEssentials.dBH.deleteBlockHistory(bh);
                }
                RelluEssentials.blockHistoryList.remove(0);
            }
        }, 0L, 2L);
    }
    
}
