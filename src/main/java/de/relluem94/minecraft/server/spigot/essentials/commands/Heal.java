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
import java.util.Objects;
import lombok.NonNull;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for healing a player's health and hunger.
 *
 * <p>When executed by a player, heals the executing player directly.
 * When executed by a command block or console, a target player name must be
 * provided as an argument.
 */
@CommandName("heal")
public class Heal implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for translation, group and plugin metadata services.
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
   * Handles the heal command execution.
   *
   * <p>Command blocks and consoles must provide a target player name as the first argument.
   * Players execute the command without arguments to heal themselves.
   * Requires the sender to have at least {@code mod} group authorization.
   *
   * @param sender  the entity that sent the command
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    the arguments provided with the command
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

      heal(sender, args[0]);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length > 0) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    heal(p);
    return true;
  }

  private void heal(@org.jspecify.annotations.NonNull Player p) {
    p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).getDefaultValue());
    p.setFoodLevel(20);
    p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_HEAL));
  }

  private void heal(CommandSender sender, String targetName) {
    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(targetName);

    if (target == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName));
      return;
    }

    heal(target);
  }

  /**
   * Provides tab completion for the heal command.
   *
   * <p>Returns a list of online player names for command blocks and consoles
   * that have {@code mod} group authorization. Returns an empty list for players
   * or when more than one argument has already been entered.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used to trigger the command
   * @param strings       the arguments currently entered
   * @return a list of matching online player names, or an empty list if not applicable
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
