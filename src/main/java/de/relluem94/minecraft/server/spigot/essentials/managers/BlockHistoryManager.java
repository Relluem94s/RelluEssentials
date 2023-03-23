package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class BlockHistoryManager implements IEnable {

    @Override
    public void enable() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskTimer(RelluEssentials.getInstance(), () -> {
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
