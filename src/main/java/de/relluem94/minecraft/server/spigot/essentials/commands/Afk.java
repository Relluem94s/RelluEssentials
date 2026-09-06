package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for the /afk command.
 *
 * <p>Allows players to toggle their own AFK status or, if they have moderator permissions,
 * toggle the AFK status of another online player.
 */
@CommandName("afk")
public class Afk implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for command execution.
   *
   * @param context the {@link ServiceContext} providing access to all necessary services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Returns the sub-commands associated with this command.
   *
   * @return an empty array, as this command has no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Provides tab-completion suggestions for the /afk command.
   *
   * <p>Returns a list of online player names if the sender is a player with moderator permissions
   * and has not yet provided a player argument. Returns an empty list otherwise.
   *
   * @param commandSender the entity that is tab-completing the command
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current command arguments
   * @return a list of suggested completions, or an empty list if no suggestions are applicable
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
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

  /**
   * Executes the /afk command.
   *
   * <p>If no arguments are provided, toggles the AFK status of the executing player.
   * If a player name is provided as an argument, toggles the AFK status of the specified target,
   * requiring the sender to have moderator permissions.
   *
   * @param commandSender the entity that executed the command
   * @param command       the command that was executed
   * @param label         the alias used to execute the command
   * @param args          the arguments passed to the command
   * @return {@code true} if the command was handled successfully, {@code false} otherwise
   */
  @Override
  public boolean onCommand(@NonNull CommandSender commandSender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!isPlayer(commandSender)) {
      commandSender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) commandSender;

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      serviceContext.getPlayerService().setAfk(p, false);
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);

    if (target == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER));
      return true;
    }

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 1) {
      serviceContext.getPlayerService().setAfk(target, false);
      return true;
    }

    return false;
  }
}