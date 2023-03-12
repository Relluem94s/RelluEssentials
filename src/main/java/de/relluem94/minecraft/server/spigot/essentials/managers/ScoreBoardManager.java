package de.relluem94.minecraft.server.spigot.essentials.managers;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoardManager implements IEnable {

    public static ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    public static Scoreboard board;

    @Override
    public void enable() {
        if (sm == null) {
            sm = Bukkit.getServer().getScoreboardManager();
        }

        board = sm.getNewScoreboard();
    }
    
}
