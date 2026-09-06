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
 * Command implementation for the suicide mechanic.
 *
 * <p>Allows players to kill themselves or, with elevated permissions, kill a target player.
 * Console and command block senders may also trigger the suicide of a specified target player.
 */
@CommandName("suicide")
public class Suicide implements CommandConstruct {

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

  /**
   * Handles the execution of the suicide command.
   *
   * <p>Behavior varies based on the sender type and provided arguments:
   * <ul>
   *   <li>Console and command block senders require a target player argument.</li>
   *   <li>Players with {@code user} permission can commit suicide on themselves.</li>
   *   <li>Players with {@code mod} permission can commit suicide on a specified target player.</li>
   * </ul>
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias of the command that was used
   * @param args    the arguments passed to the command
   * @return {@code true} in all cases to indicate the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (isCMDBlock(sender) || isConsole(sender)) {
      if (args.length == 0) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
        return true;
      }

      Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(args[0]);
      if (target == null) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      suicide(target);
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;
    if (!serviceContext.getGroupService().isSenderAuthorized(p, "user")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      suicide(p);
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);
    if (target == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    suicide(target);
    return true;
  }

  private void suicide(@NotNull Player p) {
    p.setHealth(0);
    serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_SUICIDE, p.getCustomName()));
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
   * Provides tab completion suggestions for the suicide command.
   *
   * <p>Returns a list of online player names as suggestions for the first argument,
   * but only if the sender has {@code mod} permission. Returns an empty list otherwise or when more
   * than one argument has already been provided.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias of the command that was used
   * @param strings       the current arguments provided by the sender
   * @return a list of player name suggestions, or an empty list if not applicable
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(TabCompleterHelper.getOnlinePlayers());
    return tabList;
  }
}