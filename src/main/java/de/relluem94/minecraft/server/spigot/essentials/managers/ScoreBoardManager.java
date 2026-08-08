package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SCOREBOARD_BORDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_ESSENTIALS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_RELLU;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jspecify.annotations.NonNull;

public class ScoreBoardManager implements Enable {

  public static final ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
  private static final Map<UUID, Scoreboard> playerBoards = new HashMap<>();
  private static final Set<UUID> hiddenBoards = new HashSet<>(); // NEU
  private static TranslationService translationService;

  public static void applyToPlayer(Player player) {
    if (sm == null) {
      return;
    }

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
    if (hiddenBoards.contains(player.getUniqueId())) {
      return;
    }

    Scoreboard board = playerBoards.get(player.getUniqueId());
    if (board == null) {
      return;
    }

    Objective objective = board.getObjective(PLUGIN_NAME_RELLU + PLUGIN_NAME_ESSENTIALS);
    if (objective == null) {
      return;
    }

    PlayerEntry pe = RelluEssentials.getInstance().getServiceContext()
        .getPlayerService()
        .getPlayerEntry(player.getUniqueId());
    if (pe == null) {
      return;
    }

    for (String entry : board.getEntries()) {
      board.resetScores(entry);
    }

    objective.getScore(pe.getGroup().getPrefix() + player.getName()).setScore(6);
    objective.getScore(PLUGIN_FORMS_SCOREBOARD_BORDER).setScore(5);
    objective.getScore("§r").setScore(4);
    objective.getScore(
        translationService.get(MessageKey.PLUGIN_SCOREBOARD_RANK) + ": " + pe.getGroup().getPrefix()
            + pe.getGroup().getName()).setScore(3);
    objective.getScore(
        translationService.get(MessageKey.PLUGIN_SCOREBOARD_PURSE) + ": " + PLUGIN_NAME_MONEY + " "
            + ChatColor.GOLD + StringHelper.formatDouble(pe.getPurse())
    ).setScore(2);
    objective.getScore(
        translationService.get(MessageKey.PLUGIN_SCOREBOARD_WORLD) + ": " + ChatColor.GRAY + " "
            + Objects.requireNonNull(player.getLocation().getWorld()).getName()
    ).setScore(1);
  }

  public static void updateAll() {
    Bukkit.getOnlinePlayers().forEach(ScoreBoardManager::updatePlayer);
  }

  public static void removePlayer(UUID uuid) {
    hiddenBoards.remove(uuid);
    playerBoards.remove(uuid);
  }

  @Override
  public void enable(Plugin plugin) {

    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    translationService = relluEssentialsPlugin.getServiceContext().getTranslationService();

    Bukkit.getOnlinePlayers().forEach(ScoreBoardManager::applyToPlayer);

    relluEssentialsPlugin.getServiceContext().getSchedulerService().runTaskTimer(ScoreBoardManager::updateAll,
        20L,
        20L
    );
  }
}