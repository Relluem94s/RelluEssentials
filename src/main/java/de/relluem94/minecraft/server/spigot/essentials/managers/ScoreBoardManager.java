package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;

public class ScoreBoardManager implements IEnable {

    public static final ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    private static final Map<UUID, Scoreboard> playerBoards = new HashMap<>();

    @Override
    public void enable() {
        Bukkit.getOnlinePlayers().forEach(ScoreBoardManager::applyToPlayer);

        Bukkit.getScheduler().runTaskTimer(
                RelluEssentials.getInstance(),
                ScoreBoardManager::updateAll,
                20L,
                20L
        );
    }

    public static void applyToPlayer(Player player) {
        if (sm == null) return;

        Scoreboard board = sm.getNewScoreboard();
        playerBoards.put(player.getUniqueId(), board);

        Objective objective = board.registerNewObjective(
                "relluEssentials", Criteria.DUMMY,
                ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Info"
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
        updatePlayer(player);
    }

    public static void updatePlayer(@NonNull Player player) {
        Scoreboard board = playerBoards.get(player.getUniqueId());
        if (board == null) return;

        Objective objective = board.getObjective(PLUGIN_NAME_RELLU + PLUGIN_NAME_ESSENTIALS);
        if (objective == null) return;

        PlayerEntry pe = RelluEssentials.getInstance()
                .getPlayerAPI()
                .getPlayerEntry(player.getUniqueId());
        if (pe == null) return;

        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        objective.getScore(pe.getGroup().getPrefix() + player.getName()).setScore(6);
        objective.getScore(PLUGIN_FORMS_SCOREBOARD_BORDER).setScore(5);
        objective.getScore("§r").setScore(4);
        objective.getScore(languageHelper.get(MessageKey.PLUGIN_SCOREBOARD_RANK) + ": " + pe.getGroup().getPrefix() + pe.getGroup().getName()).setScore(3);
        objective.getScore(
                languageHelper.get(MessageKey.PLUGIN_SCOREBOARD_PURSE) + ": " + PLUGIN_NAME_MONEY + " " + ChatColor.GOLD + StringHelper.formatDouble(pe.getPurse())
        ).setScore(2);
        objective.getScore(
                languageHelper.get(MessageKey.PLUGIN_SCOREBOARD_WORLD) + ": " + ChatColor.GRAY + " " + Objects.requireNonNull(player.getLocation().getWorld()).getName()
        ).setScore(1);
    }

    public static void updateAll() {
        Bukkit.getOnlinePlayers().forEach(ScoreBoardManager::updatePlayer);
    }

    public static void removePlayer(UUID uuid) {
        playerBoards.remove(uuid);
    }

}