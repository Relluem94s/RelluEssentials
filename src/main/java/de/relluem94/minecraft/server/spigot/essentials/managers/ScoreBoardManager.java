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

import java.util.*;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;

public class ScoreBoardManager implements IEnable {

    public static final ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    private static final Map<UUID, Scoreboard> playerBoards = new HashMap<>();
    private static final Set<UUID> hiddenBoards = new HashSet<>(); // NEU

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

        String currentWorld = player.getWorld().getName();
        if (!RelluEssentials.getInstance().scoreboardShow.contains(currentWorld)) {
            hiddenBoards.add(player.getUniqueId());
            player.setScoreboard(sm.getMainScoreboard());
            return;
        }

        Scoreboard board = sm.getNewScoreboard();
        playerBoards.put(player.getUniqueId(), board);

        Objective objective = board.registerNewObjective(
                PLUGIN_NAME_RELLU + PLUGIN_NAME_ESSENTIALS, Criteria.DUMMY,
                ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Info"
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
        updatePlayer(player);
    }

    public static void setScoreboardVisible(Player player, boolean visible) {
        if (visible) {
            hiddenBoards.remove(player.getUniqueId());
            applyToPlayer(player);
        } else {
            hiddenBoards.add(player.getUniqueId());
            if (sm != null) {
                player.setScoreboard(sm.getMainScoreboard());
            }
        }
    }

    public static void updatePlayer(@NonNull Player player) {
        if (hiddenBoards.contains(player.getUniqueId())) return;

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
        hiddenBoards.remove(uuid);
        playerBoards.remove(uuid);
    }
}