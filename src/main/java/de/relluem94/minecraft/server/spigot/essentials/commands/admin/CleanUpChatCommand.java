package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CleanUpChatCommand implements SubCommand {

  private final TranslationService translationService;

  public CleanUpChatCommand(ServiceContext context) {
    this.translationService = context.getTranslationService();
  }

  @Override
  public void execute(Player player, String[] args) {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      for (int i = 0; i < 100; i++) {
        onlinePlayer.sendMessage("");
      }
    }
    player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CHAT_CLEARED));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.CHAT.getName().equalsIgnoreCase(args[0]);
  }
}