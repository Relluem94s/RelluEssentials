package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_MSG_SPACER_IN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_MSG_SPACER_OUT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SPACER_CHANNEL;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ReplyRegistry;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Service responsible for handling various types of chat communication,
 * including player messages, console output, channel messages, and private messaging.
 */
public class ChatService {

  private final ServiceContext serviceContext;
  private final ReplyRegistry replyRegistry;

  /**
   * Constructs a new ChatService.
   *
   * @param serviceContext the service context containing necessary services
   * @param replyRegistry  the registry managing player reply targets
   */
  public ChatService(ServiceContext serviceContext, ReplyRegistry replyRegistry) {
    this.serviceContext = serviceContext;
    this.replyRegistry = replyRegistry;
  }

  /**
   * Sends a message to the sender. If the sender is a player, it sends it to them;
   * otherwise, it sends it to the console.
   *
   * @param sender  the sender of the message
   * @param message the message to be sent
   */
  public void sendMessage(CommandSender sender, String message) {
    if (isPlayer(sender)) {
      Player p = (Player) sender;
      p.sendMessage(message);
    } else {
      consoleSendMessage("", message);
    }
  }

  /**
   * Sends a message to the server console with a specific type prefix.
   *
   * @param type    the prefix/type of the message
   * @param message the message content
   */
  public void consoleSendMessage(String type, String message) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    console.sendMessage(type + " " + message);
  }

  /**
   * Sends a message to the server console with a specific type prefix, repeated multiple times.
   *
   * @param type    the prefix/type of the message
   * @param message the message content
   * @param repeat  the number of times to repeat the message
   */
  public void consoleSendMessage(String type, String message, int repeat) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    for (int i = 0; i <= repeat; i++) {
      console.sendMessage(type + " " + message);
    }
  }

  /**
   * Sends a formatted message to all authorized players in a specific channel.
   *
   * @param message the raw message to send
   * @param sender  the player sending the message
   * @param channel the channel identifier to strip from the message
   * @param group   the group information for the sender
   */
  public void sendMessageInChannel(String message, Player sender, String channel,
      GroupEntry group) {
    String strippedMessage = message.replaceFirst(channel, "");
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (serviceContext.getGroupService().isSenderAuthorized(onlinePlayer, group.getName())) {
        sendMessage(onlinePlayer, sender.getCustomName() + group.getPrefix()
            + PLUGIN_FORMS_SPACER_CHANNEL + PLUGIN_COLOR_MESSAGE + replaceColor(strippedMessage));
      }
    }
  }

  /**
   * Sends a formatted message to all authorized players in a specific channel using a name string.
   *
   * @param message    the raw message to send
   * @param senderName the name of the sender
   * @param channel    the channel identifier to strip from the message
   * @param group      the group information for the sender
   */
  public void sendMessageInChannel(String message, String senderName, String channel,
      GroupEntry group) {
    String strippedMessage = message.replaceFirst(channel, "");
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (serviceContext.getGroupService().isSenderAuthorized(onlinePlayer, group.getName())) {
        sendMessage(onlinePlayer, senderName + group.getPrefix()
            + PLUGIN_FORMS_SPACER_CHANNEL + PLUGIN_COLOR_MESSAGE + replaceColor(strippedMessage));
      }
    }
  }

  /**
   * Sends a message to a player's action bar.
   *
   * @param p       the recipient player
   * @param message the message to display in the action bar
   */
  public void sendMessageInActionBar(@NonNull Player p, String message) {
    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
  }

  /**
   * Registers a reply target for a player.
   *
   * @param sender the player initiating the reply
   * @param target the player to be replied to
   */
  public void registerReply(Player sender, Player target) {
    replyRegistry.register(sender, target);
  }

  /**
   * Checks if a player has a registered reply target.
   *
   * @param sender the player to check
   * @return true if a reply target exists, false otherwise
   */
  public boolean hasReplyTarget(Player sender) {
    return replyRegistry.hasReplyTarget(sender);
  }

  /**
   * Finds the registered reply target for a player.
   *
   * @param sender the player to look up
   * @return the target player, or null if none exists
   */
  public Player findReplyTarget(Player sender) {
    return replyRegistry.findReplyTarget(sender);
  }

  /**
   * Sends a private message from one player to another.
   *
   * @param sender the sender of the private message
   * @param target the recipient of the private message
   * @param args   the message arguments
   * @param start  the starting index in the arguments array
   */
  public void sendPrivateMessage(CommandSender sender, Player target, String[] args, int start) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return;
    }

    Player senderPlayer = (Player) sender;
    if (serviceContext.getGroupService().isSenderAuthorized(senderPlayer, "user")) {
      String message = buildMessage(senderPlayer, args, start);
      target.sendMessage(senderPlayer.getCustomName() + PLUGIN_FORMS_MSG_SPACER_IN + message);
      senderPlayer.sendMessage(target.getCustomName() + PLUGIN_FORMS_MSG_SPACER_OUT + message);
    } else {
      senderPlayer.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
    }
  }

  private String buildMessage(Player sender, String[] args, int start) {
    String message = implode(start, args);
    if (serviceContext.getGroupService().isSenderAuthorized(sender, "vip")) {
      return replaceSymbols(replaceColor(message));
    }
    return message;
  }
}