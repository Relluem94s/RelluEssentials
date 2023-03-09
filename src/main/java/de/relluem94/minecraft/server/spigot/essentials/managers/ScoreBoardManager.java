package de.relluem94.minecraft.server.spigot.essentials.managers;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class ScoreBoardManager implements IEnable {

    @Override
    public void enable() {
        if (RelluEssentials.sm == null) {
            RelluEssentials.sm = Bukkit.getServer().getScoreboardManager();
        }

        RelluEssentials.board = RelluEssentials.sm.getNewScoreboard();
    }
    
}
