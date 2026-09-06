package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND_BLOCK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for broadcasting formatted messages to all players on the server.
 *
 * <p>Supports execution by players (requires mod permission), the console, and command blocks.
 * When executed from a command block, the {@code @p} selector is resolved to the nearest player.
 */
@CommandName("print")
public class Print implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for translation, group, and plugin metadata services.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Executes the print command, broadcasting a formatted message to all players.
   *
   * <p>Resolves the sender's display name based on their type (player, console, or command block).
   * Players require the {@code mod} group permission. Command blocks support the {@code @p}
   * selector, which is replaced with the custom name of the nearest player.
   *
   * @param sender  the command sender executing the command
   * @param command the command being executed
   * @param label   the alias used to execute the command
   * @param args    the arguments provided with the command; the first argument onwards forms the
   *                message
   * @return {@code true} in all cases, indicating the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    Player targetedPlayerBySelector = null;

    if (args.length < 1) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PRINT_INFO));
      return true;
    }

    String name = null;

    if (isCMDBlock(sender)) {
      BlockCommandSender bcs = (BlockCommandSender) sender;
      name = PLUGIN_COLOR_COMMAND_BLOCK + bcs.getName();

      List<String> argsList = Arrays.asList(args);
      if (argsList.contains("@p")) {
        CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
        targetedPlayerBySelector = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
        if (targetedPlayerBySelector == null) {
          sender.sendMessage(serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER,
                  serviceContext.getTranslationService()
                      .get(MessageKey.COMMAND_NO_PLAYER_IN_REACH)));
          return true;
        }
      }
    }

    if (isConsole(sender)) {
      ConsoleCommandSender ccs = (ConsoleCommandSender) sender;
      name = PLUGIN_COLOR_CONSOLE + StringHelper.firstCharToUpper(ccs.getName().toLowerCase());
    }

    if (isPlayer(sender)) {
      Player p = (Player) sender;

      if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      name = p.getCustomName();
    }

    if (name == null) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_INVALID));
      return true;
    }

    String message = implode(0, args);
    message = replaceSymbols(replaceColor(message));

    if (targetedPlayerBySelector != null) {
      message = message.replace("@p",
          Objects.requireNonNull(targetedPlayerBySelector.getCustomName()));
    }

    serviceContext.getPluginMetadataService().getPlugin().getServer()
        .broadcastMessage(name + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + message);
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
   * Provides tab-completion suggestions for the print command.
   *
   * @param commandSender the sender requesting tab-completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current arguments
   * @return an empty list, as no tab-completion suggestions are provided
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return new ArrayList<>();
  }
}