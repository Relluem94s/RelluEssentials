package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for changing the display name (nickname) of a player.
 * Requires the sender to have admin group authorization.
 * Expects exactly two arguments: the target player's name and the desired nickname.
 */
@CommandName("nick")
public class Nick implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for accessing plugin services.
   *
   * @param context the {@link ServiceContext} providing access to all plugin services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles the nick command execution.
   * Validates argument count, sender authorization, and target player existence
   * before applying the nickname to the target player's display name and tab list name.
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias used to execute the command
   * @param args    the arguments provided with the command, expecting target player name and nickname
   * @return {@code true} in all cases to indicate the command was handled
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

    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "admin")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);

    if (target == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(target);
    pe.setCustomName(args[1]);
    pe.setUpdatedBy(
        isPlayer(sender) ? serviceContext.getPlayerService().getPlayerEntry(((Player) sender))
            .getId() : 1);
    pe.setHasToBeUpdated(true);
    target.setCustomName(pe.getGroup().getPrefix() + args[1]);
    target.setPlayerListName(pe.getGroup().getPrefix() + args[1]);
    sender.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_NICK, pe.getGroup().getPrefix() + target.getName()));
    return true;
  }

  /**
   * Returns the sub-commands associated with this command.
   *
   * @return an empty array as this command has no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Provides tab completion suggestions for the nick command.
   * Returns a list of online player names as suggestions for the first argument.
   * Returns an empty list if the sender lacks admin authorization,
   * is not a player, or has already provided more than one argument.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current arguments provided by the sender
   * @return a list of online player names, or an empty list if completion is not applicable
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "admin")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(TabCompleterHelper.getOnlinePlayers());

    return tabList;
  }
}