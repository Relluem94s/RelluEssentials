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
 * Command implementation for sending private messages between players. Handles the /msg command,
 * registers reply targets and dispatches private messages.
 */
@CommandName("msg")
public class Message implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for accessing plugin services.
   *
   * @param context the {@link ServiceContext} providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Executes the /msg command, sending a private message from the sender to a specified target
   * player. Validates that the sender is a player, that sufficient arguments are provided, and that
   * the target player is online before dispatching the message.
   *
   * @param sender  the entity executing the command
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    the arguments provided; args[0] is the target player name, args[1+] form the
   *                message
   * @return {@code true} in all cases to indicate the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;
    if (args.length <= 1) {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_MSG_INFO));
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);
    if (target == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_MSG_PLAYER_OFFLINE));
      return true;
    }

    serviceContext.getChatService().registerReply(p, target);
    serviceContext.getChatService().sendPrivateMessage(sender, target, args, 1);
    return true;
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
   * Provides tab completion suggestions for the /msg command. Returns a list of online player names
   * when completing the first argument. Returns an empty list if the sender is not an authorized
   * player or more than one argument has been typed.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used to trigger the command
   * @param strings       the current arguments provided by the sender
   * @return a list of online player name suggestions, or an empty list if completion is not
   *     applicable
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

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(TabCompleterHelper.getOnlinePlayers());

    return tabList;
  }
}
