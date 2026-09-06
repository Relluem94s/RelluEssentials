package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.rellulib.utils.TypeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

/**
 * Command implementation for player teleportation.
 *
 * <p>Supports teleporting to players, to specific coordinates, and sending teleport requests
 * that require acceptance. Handles both direct teleportation for authorized users and request-based
 * teleportation for standard users.
 *
 * <p>Subcommands:
 * <ul>
 *   <li>{@code /teleport <player>} – Teleports the sender to the target player or
 *   sends a request.</li>
 *   <li>{@code /teleport accept} – Accepts a pending teleport request.</li>
 *   <li>{@code /teleport to <player>} – Requests the target player to teleport to
 *   the sender.</li>
 *   <li>{@code /teleport <x> <y> <z>} – Teleports the sender to the given
 *   coordinates (mod only).</li>
 * </ul>
 */
@CommandName("teleport")
public class Teleport implements CommandConstruct {

  private final HashMap<Player, Player> teleportAcceptList = new HashMap<>();
  private final HashMap<Player, Player> teleportToAcceptList = new HashMap<>();
  private ServiceContext serviceContext;

  /**
   * Injects the service context required for this command to function.
   *
   * @param context the {@link ServiceContext} providing access to all necessary services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  private void addTeleportEntry(Player p, Player t) {
    teleportAcceptList.put(t, p);
    t.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, p.getCustomName()));
    serviceContext.getSchedulerService().runTaskLater(() -> {
      if (hasTeleportEntry(t)) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TP_REQUEST_EXPIRED));
        t.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TP_REQUEST_EXPIRED));
        removeTeleportEntry(t);
      }
    }, 20 * 60 * 2L);
  }

  private void addTeleportToEntry(Player p, Player t) {
    teleportToAcceptList.put(t, p);
    t.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, p.getCustomName()));
    serviceContext.getSchedulerService().runTaskLater(() -> {
      if (hasToTeleportEntry(t)) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TP_REQUEST_EXPIRED));
        t.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TP_REQUEST_EXPIRED));
        removeToTeleportEntry(t);
      }
    }, 20 * 60 * 2L);
  }

  private void removeTeleportEntry(Player t) {
    teleportAcceptList.remove(t);
  }

  private void removeToTeleportEntry(Player t) {
    teleportToAcceptList.remove(t);
  }

  private boolean hasTeleportEntry(Player t) {
    return teleportAcceptList.containsKey(t);
  }

  private boolean hasToTeleportEntry(Player t) {
    return teleportToAcceptList.containsKey(t);
  }

  /**
   * Teleports the target player to the given player's location.
   *
   * <p>Saves a back point for the target player before teleporting and notifies the target.
   *
   * @param p the player whose location is the teleport destination
   * @param t the player being teleported
   */
  public void teleport(Player p, Player t) {
    serviceContext.getBackService().saveBackPoint(t);
    t.teleport(p);
    t.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TP, t.getCustomName()));
  }

  /**
   * Teleports the requesting player to the target player's location.
   *
   * <p>Saves a back point for the requesting player before teleporting
   * and notifies both players.
   *
   * @param p the player being teleported to the target
   * @param t the target player whose location is the destination
   */
  public void teleportTo(Player p, Player t) {
    serviceContext.getBackService().saveBackPoint(p);
    p.teleport(t);
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TP_TO, p.getCustomName()));
    t.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TP, t.getCustomName()));
  }

  /**
   * Handles execution of the teleport command.
   *
   * <p>Validates that the sender is a player and is authorized. Delegates to the appropriate
   * teleport behavior based on the provided arguments.
   *
   * @param sender  the entity executing the command
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    the arguments provided with the command
   * @return {@code true} if the command was handled, {@code false} otherwise
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "vip")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TP_INFO, command.getName(), command.getName(),
              Commands.ACCEPT.getName(), command.getName(), Commands.TO.getName(),
              command.getName()));
      return true;
    }

    if (args.length == 1) {
      if (args[0].equalsIgnoreCase(Commands.ACCEPT.getName())) {
        if (hasTeleportEntry(p)) {
          teleport(p, teleportAcceptList.get(p));
          removeTeleportEntry(p);
          return true;
        }

        if (hasToTeleportEntry(p)) {
          teleportTo(p, teleportToAcceptList.get(p));
          removeToTeleportEntry(p);
          return true;
        }

        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TP_ACCEPT_NO_REQUEST));
        return true;
      }

      Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(args[0]);
      if (target == null) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return false;
      }

      if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
        addTeleportEntry(p, target);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TP_SEND_REQUEST, target.getCustomName()));
        return true;
      }

      serviceContext.getBackService().saveBackPoint(p);
      p.teleport(target);
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TP, target.getCustomName()));
      return true;
    }

    if (args.length == 2) {
      Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(args[1]);
      if (target == null) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[1]));
        return true;
      }

      if (!args[0].equalsIgnoreCase(Commands.TO.getName())) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TP_INFO, command.getName(), command.getName(),
                Commands.ACCEPT.getName(), command.getName(), Commands.TO.getName(),
                command.getName()));
        return true;
      }

      if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
        addTeleportToEntry(p, target);
        return true;
      }
      teleportTo(p, target);
      return true;
    }

    if (args.length == 3) {
      teleportToLocation(p, args[0], args[1], args[2]);
      return true;
    }

    p.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
    return true;
  }

  private void teleportToLocation(Player p, String x, String y, String z) {
    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }

    if (!TypeUtils.isInt(x)) {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_INVALID));
      return;
    }

    if (!TypeUtils.isInt(y)) {
      return;
    }

    if (!TypeUtils.isInt(z)) {
      return;
    }

    Location l = p.getLocation().clone();
    l.setX(Integer.parseInt(x));
    l.setY(Integer.parseInt(y));
    l.setZ(Integer.parseInt(z));

    serviceContext.getBackService().saveBackPoint(p);
    p.teleport(l);
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TP, l.getX() + ", " + l.getY() + ", " + l.getZ()));
  }

  /**
   * Returns all subcommands supported by the teleport command.
   *
   * @return an array of {@link CommandsEnum} entries representing the available subcommands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  /**
   * Provides tab completion suggestions for the teleport command.
   *
   * <p>Returns available subcommands and online player names based on the current input.
   * Returns an empty list if the sender is not an authorized player.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being completed
   * @param s             the alias used
   * @param strings       the current argument tokens
   * @return a list of tab completion suggestions, or an empty list if unauthorized
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length == 1) {
      tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
      serviceContext.getPluginMetadataService().getPlugin().getServer().getOnlinePlayers().stream()
          .filter(player -> !player.equals(commandSender)).map(Player::getName)
          .forEach(tabList::add);
    }

    if (strings.length == 2 && strings[0].equalsIgnoreCase(Commands.TO.getName())) {
      serviceContext.getPluginMetadataService().getPlugin().getServer().getOnlinePlayers().stream()
          .filter(player -> !player.equals(commandSender)).map(Player::getName)
          .forEach(tabList::add);
    }

    return tabList;
  }

  /**
   * Defines the available sub-commands for the teleport command.
   * Each entry represents a distinct teleport mode.
   */
  @Getter
  public enum Commands implements CommandsEnum {

    ACCEPT("accept"), TO("to");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}