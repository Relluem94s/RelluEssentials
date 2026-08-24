package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class PingCommand implements SubCommand {

  private final ServiceContext serviceContext;

  public PingCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    if (args.length == 1) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_ADMIN_PING, player.getPing()));
      return;
    }

    Player target = Bukkit.getPlayer(args[1]);
    if (target == null) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_ADMIN_PING_OTHER_NOT_FOUND, args[1]));
      return;
    }

    player.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_ADMIN_PING_OTHER,
            target.getCustomName(), target.getPing()));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return (args.length == 1 || args.length == 2) && Admin.Commands.PING.getName()
        .equalsIgnoreCase(args[0]);
  }
}