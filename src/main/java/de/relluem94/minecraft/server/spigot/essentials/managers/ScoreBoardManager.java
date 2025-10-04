package de.relluem94.minecraft.server.spigot.essentials.managers;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoardManager implements IEnable {

    public static final ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    public static final Scoreboard board = sm != null ? sm.getNewScoreboard() : null;

    @Override
    public void enable() {
        // TODO in the future
    }
    
}
