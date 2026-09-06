package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command that clears the chat for all online players by sending 100 empty messages,
 * then notifies the executing player that the chat has been cleared.
 */
public class CleanUpChatCommand implements SubCommand {

  private final ServiceContext serviceContext;

  /**
   * Creates a new CleanUpChatCommand with the given service context.
   *
   * @param context the service context providing access to plugin metadata and translation services
   */
  public CleanUpChatCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    for (Player onlinePlayer : serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getOnlinePlayers()) {
      for (int i = 0; i < 100; i++) {
        onlinePlayer.sendMessage("");
      }
    }
    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_ADMIN_CHAT_CLEARED));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.CHAT.getName().equalsIgnoreCase(args[0]);
  }
}