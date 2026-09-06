package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for assigning a permission group to a player.
 *
 * <p>Players with sufficient authority can assign groups up to their own rank.
 * Console and command block senders bypass rank restrictions and may assign any group.
 */
@CommandName("setGroup")
public class PermissionsGroup implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the {@link ServiceContext} into this command instance.
   *
   * @param context the service context providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles the group assignment command execution.
   *
   * <p>Expects exactly two arguments: the target player name and the group name.
   * Player senders are restricted to groups within their authorized rank.
   * Console and command block senders may assign any existing group.
   *
   * @param sender the entity executing the command
   * @param command the command being executed
   * @param label the alias used to trigger the command
   * @param args the command arguments where {@code args[0]} is the target player name
   *             and {@code args[1]} is the group name
   * @return {@code true} if the command was handled, {@code false} otherwise
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {

    if (args.length < 2) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
      return true;
    }

    if (args.length > 2) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    OfflinePlayer target = PlayerHelper.getOfflinePlayer(args[0]);

    if (target == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (serviceContext.getPlayerService().getPlayerEntry(target.getPlayer()) == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (isPlayer(sender)) {
      Player p = (Player) sender;
      Optional<GroupEntry> groupEntry = serviceContext.getGroupService()
          .resolveAuthorizedGroup(p, args[1]);

      if (groupEntry.isEmpty()) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_SETGROUP_GROUP_NOT_FOUND, args[1]));
        return true;
      }

      serviceContext.getPlayerService().updateGroup(target, groupEntry.get());
      notifySenderAndTarget(sender, groupEntry.get(), target);
      return true;
    } else if (isCMDBlock(sender) || isConsole(sender)) {
      GroupEntry g = serviceContext.getGroupService().resolveGroupWithFallback(args[1]);
      serviceContext.getPlayerService().updateGroup(target, g);
      notifySenderAndTarget(sender, g, target);
      return true;
    }
    return false;
  }

  /**
   * Returns the sub-commands associated with this command construct.
   *
   * @return an empty array as this command has no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Provides tab completion suggestions for the group assignment command.
   *
   * <p>Returns an empty list if the sender is not authorized at the {@code mod} rank or above,
   * or if more than two arguments have already been entered.
   * Suggests online player names for the first argument and available group names for the second.
   *
   * @param commandSender the entity requesting tab completion
   * @param command the command being tab-completed
   * @param s the alias used to trigger the command
   * @param strings the current command arguments
   * @return a list of matching completion suggestions, or an empty list if none apply
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return new ArrayList<>();
    }

    if (strings.length > 2) {
      return new ArrayList<>();
    }

    if (strings.length == 1) {
      return TabCompleterHelper.getOnlinePlayers();
    }

    return TabCompleterHelper.getGroups(serviceContext.getGroupService().findAllGroups());
  }

  private void notifySenderAndTarget(@NotNull CommandSender sender, @NotNull GroupEntry g,
      @NotNull OfflinePlayer target) {
    String groupDisplayName = g.getPrefix() + g.getName();
    sender.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SETGROUP, groupDisplayName, target.getName()));

    if (target.isOnline()) {
      Player onlineTarget = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(target.getUniqueId());
      if (onlineTarget != null) {
        onlineTarget.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_SETGROUP, groupDisplayName, target.getName()));
      }
    }
  }
}