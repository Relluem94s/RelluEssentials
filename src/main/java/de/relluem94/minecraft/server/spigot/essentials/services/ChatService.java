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

public class ChatService {

  private final ServiceContext serviceContext;
  private final ReplyRegistry replyRegistry;

  public ChatService(ServiceContext serviceContext, ReplyRegistry replyRegistry) {
    this.serviceContext = serviceContext;
    this.replyRegistry = replyRegistry;
  }

  public void sendMessage(CommandSender sender, String message) {
    if (isPlayer(sender)) {
      Player p = (Player) sender;
      p.sendMessage(message);
    } else {
      consoleSendMessage("", message);
    }
  }

  public void consoleSendMessage(String type, String message) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    console.sendMessage(type + " " + message);
  }

  public void consoleSendMessage(String type, String message, int repeat) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    for (int i = 0; i <= repeat; i++) {
      console.sendMessage(type + " " + message);
    }
  }

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

  public void sendMessageInActionBar(@NonNull Player p, String message) {
    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
  }

  public void registerReply(Player sender, Player target) {
    replyRegistry.register(sender, target);
  }

  public boolean hasReplyTarget(Player sender) {
    return replyRegistry.hasReplyTarget(sender);
  }

  public Player findReplyTarget(Player sender) {
    return replyRegistry.findReplyTarget(sender);
  }

  public void sendPrivateMessage(CommandSender sender, Player target, String[] args, int start) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return;
    }

    Player senderPlayer = (Player) sender;
    String message = buildMessage(senderPlayer, args, start);

    if (serviceContext.getGroupService().isSenderAuthorized(senderPlayer, "user")) {
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