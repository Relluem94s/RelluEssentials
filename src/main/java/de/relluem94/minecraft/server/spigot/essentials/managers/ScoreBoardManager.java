package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SCOREBOARD_BORDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_ESSENTIALS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_RELLU;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jspecify.annotations.NonNull;

/**
 * Manages per-player scoreboards, including creation, updates, visibility toggling,
 * and cleanup. Scoreboards are conditionally shown based on world group settings
 * and can be hidden or restored per player at runtime.
 */
public class ScoreBoardManager implements Enable {

  private static final Map<UUID, Scoreboard> playerBoards = new HashMap<>();
  private static final Set<UUID> hiddenBoards = new HashSet<>(); // NEU
  private static ScoreboardManager sm;
  private static TranslationService translationService;
  private ServiceContext serviceContext;

  /**
   * Creates and assigns a new scoreboard to the given player if the scoreboard setting
   * is active for the player's current world. If the setting is inactive, the player
   * is assigned the main scoreboard and marked as hidden.
   *
   * @param player            the player to apply the scoreboard to
   * @param worldGroupService the service used to check world-specific settings
   */
  public static void applyToPlayer(Player player, WorldGroupService worldGroupService) {
    if (sm == null) {
      return;
    }

    String currentWorld = player.getWorld().getName();
    boolean settingActiveForWorld = worldGroupService.isSettingActiveForWorld(
        WorldSetting.SCOREBOARD_SHOW, currentWorld);
    if (!settingActiveForWorld) {
      hiddenBoards.add(player.getUniqueId());
      player.setScoreboard(sm.getMainScoreboard());
      return;
    }

    Scoreboard board = sm.getNewScoreboard();
    playerBoards.put(player.getUniqueId(), board);

    Objective objective = board.registerNewObjective(PLUGIN_NAME_RELLU + PLUGIN_NAME_ESSENTIALS,
        Criteria.DUMMY, ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Info");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    player.setScoreboard(board);
    updatePlayer(player);
  }

  /**
   * Shows or hides the scoreboard for the given player. When made visible, a new
   * scoreboard is applied via {@link #applyToPlayer(Player, WorldGroupService)}.
   * When hidden, the player is assigned the main scoreboard.
   *
   * @param player            the player whose scoreboard visibility is being changed
   * @param visible           {@code true} to show the scoreboard, {@code false} to hide it
   * @param worldGroupService the service used to check world-specific settings when showing
   */
  public static void setScoreboardVisible(Player player, boolean visible,
      WorldGroupService worldGroupService) {
    if (visible) {
      hiddenBoards.remove(player.getUniqueId());
      applyToPlayer(player, worldGroupService);
    } else {
      hiddenBoards.add(player.getUniqueId());
      if (sm != null) {
        player.setScoreboard(sm.getMainScoreboard());
      }
    }
  }

  /**
   * Refreshes the scoreboard content for the given player by rewriting all score entries
   * with current data including rank, purse balance, and current world name.
   * Does nothing if the player's scoreboard is hidden or not yet initialized.
   *
   * @param player the player whose scoreboard should be updated
   */
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

    PlayerEntry pe = RelluEssentials.getInstance().getServiceContext().getPlayerService()
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
            + ChatColor.GOLD + StringHelper.formatDouble(pe.getPurse())).setScore(2);
    objective.getScore(
        translationService.get(MessageKey.PLUGIN_SCOREBOARD_WORLD) + ": " + ChatColor.GRAY + " "
            + Objects.requireNonNull(player.getLocation().getWorld()).getName()).setScore(1);
  }

  /**
   * Removes all scoreboard state associated with the given player UUID,
   * including their board instance and hidden status.
   *
   * @param uuid the UUID of the player to remove
   */
  public static void removePlayer(UUID uuid) {
    hiddenBoards.remove(uuid);
    playerBoards.remove(uuid);
  }

  /**
   * Refreshes the scoreboards of all currently online players.
   */
  public void updateAll() {
    serviceContext.getPluginMetadataService().getPlugin().getServer().getOnlinePlayers()
        .forEach(ScoreBoardManager::updatePlayer);
  }

  /**
   * Initializes the scoreboard manager by resolving required services, applying scoreboards
   * to all currently online players, and scheduling a repeating update task.
   *
   * @param plugin the plugin instance used to access services and the server's scoreboard manager
   */
  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    this.serviceContext = serviceContext;
    translationService = serviceContext.getTranslationService();

    sm = plugin.getServer().getScoreboardManager();

    serviceContext.getPluginMetadataService().getPlugin().getServer().getOnlinePlayers().forEach(
        (player) -> ScoreBoardManager.applyToPlayer(
            player, serviceContext.getWorldGroupService()));

    serviceContext.getSchedulerService().runTaskTimer(this::updateAll, 20L, 20L);
  }
}