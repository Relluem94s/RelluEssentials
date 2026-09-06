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
 * Command implementation for the /more command.
 * Fills the item in the main hand of the executing player or a specified target
 * player to a stack of 64.
 */
@CommandName("more")
public class More implements CommandConstruct {

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
   * Executes the /more command.
   *
   * <p>If no arguments are provided, fills the executing player's held item to 64.
   * If one argument is provided, fills the specified target player's held item to 64
   * and notifies both the sender and the target.</p>
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias used to execute the command
   * @param args    the arguments passed to the command; optionally one player name
   * @return {@code true} in all cases to indicate the command was handled
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

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.getInventory().getItemInMainHand().setAmount(64);
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_MORE, p.getInventory().getItemInMainHand().getType()));
      return true;
    }

    if (args.length > 1) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);
    if (target == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    target.getInventory().getItemInMainHand().setAmount(64);
    p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_MORE,
        target.getInventory().getItemInMainHand().getType()));
    target.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_MORE_PLAYER,
            target.getInventory().getItemInMainHand().getType()));
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
   * Provides tab completion suggestions for the /more command.
   *
   * <p>Returns a list of online player names if the sender is authorized and only one argument
   * is being completed. Returns an empty list if the sender lacks permission or more than
   * one argument has already been entered.</p>
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current arguments entered by the sender
   * @return a list of matching online player names, or an empty list
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return new ArrayList<>();
    }

    if (strings.length > 1) {
      return new ArrayList<>();
    }

    return TabCompleterHelper.getOnlinePlayers();
  }
}
