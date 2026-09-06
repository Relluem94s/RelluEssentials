package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_BROADCAST;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for broadcasting messages to all online players. Supports two broadcast
 * modes: chat messages and title overlays. Requires the sender to have at least moderator-level
 * authorization.
 */
@CommandName("broadcast")
public class Broadcast implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for authorization, translation and plugin access.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Provides tab completion suggestions for the broadcast command. Returns an empty list if the
   * sender lacks moderator authorization or if more than one argument is present.
   *
   * @param commandSender the sender requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current command arguments
   * @return a list of valid tab completion suggestions
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

    tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));

    return tabList;
  }

  /**
   * Returns all available sub-commands for the broadcast command.
   *
   * @return an array of {@link CommandsEnum} values representing the available sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  /**
   * Executes the broadcast command. Sends a chat broadcast or a title overlay to all online players
   * depending on the provided arguments. Requires the sender to have at least moderator-level
   * authorization.
   *
   * @param sender  the entity executing the command
   * @param command the command being executed
   * @param label   the alias used to trigger the command
   * @param args    the arguments provided with the command
   * @return {@code true} in all cases to indicate the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (args.length < 1) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_BROADCAST_INFO));
      return true;
    }

    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "mod")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args[0].equalsIgnoreCase(Commands.TITLE.getName())) {
      broadcast(args, 1, false);
      return true;
    } else if (args[0].equalsIgnoreCase(Commands.CHAT.getName())) {
      broadcast(args, 1, true);
      return true;
    }

    broadcast(args, 0, true);
    return true;
  }

  private void broadcast(String[] args, int start, boolean chat) {
    String message = implode(start, args);
    message = replaceSymbols(replaceColor(message));

    if (chat) {
      serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
          PLUGIN_NAME_BROADCAST + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + message);
      return;
    }

    for (Player op : serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getOnlinePlayers()) {
      op.sendTitle(PLUGIN_NAME_BROADCAST, message, 5, 80, 5);
    }
  }

  /**
   * Defines the available sub-commands for the broadcast command.
   * Each entry represents a distinct broadcast mode.
   */
  @Getter
  public enum Commands implements CommandsEnum {

    TITLE("title"), CHAT("chat");

    private final String name;
    private final String[] subCommands;

    /**
     * Creates a new Commands entry with the given name and optional sub-commands.
     *
     * @param name the name of the sub-command
     * @param subCommands optional nested sub-commands
     */
    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}