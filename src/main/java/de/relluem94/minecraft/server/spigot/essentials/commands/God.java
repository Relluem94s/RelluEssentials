package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
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
 * Command implementation for toggling invulnerability (god mode) on players.
 *
 * <p>When executed by a console or command block, a target player name must be provided as an
 * argument.
 * When executed by a player with the required group authorization, god mode is toggled for
 * themselves.
 */
@CommandName("god")
public class God implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for translation, group authorization and plugin metadata
   * access.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
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
   * Handles the execution of the god command.
   *
   * <p>Console and command block senders must provide a target player name as the first argument.
   * Player senders with the required group authorization toggle their own god mode without
   * arguments.
   *
   * @param sender  the source of the command
   * @param command the command that was executed
   * @param label   the alias used to execute the command
   * @param args    the arguments passed to the command
   * @return {@code true} in all cases to indicate the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {

    if (isCMDBlock(sender) || isConsole(sender)) {
      if (args.length < 1) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
        return true;
      }

      toggleGodMode(sender, args[0]);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length > 0) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    toggleGodMode(p);
    return true;
  }

  private void toggleGodMode(@NotNull Player p) {
    p.sendMessage(!p.isInvulnerable() ? serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_GOD_ON)
        : serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_GOD_OFF));
    p.setInvulnerable(!p.isInvulnerable());
  }

  private void toggleGodMode(CommandSender sender, String targetName) {
    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(targetName);

    if (target == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName));
      return;
    }

    toggleGodMode(target);
  }

  /**
   * Provides tab completion suggestions for the god command.
   *
   * <p>Returns a list of online player names when the sender is not a player and is authorized
   * with the required group. Returns an empty list otherwise or when more than one argument is
   * present.
   *
   * @param commandSender the source requesting tab completion
   * @param command       the command being tab completed
   * @param s             the alias used
   * @param strings       the current arguments
   * @return a list of suggested completions, or an empty list if none apply
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return tabList;
    }

    if (isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(TabCompleterHelper.getOnlinePlayers());

    return tabList;
  }
}