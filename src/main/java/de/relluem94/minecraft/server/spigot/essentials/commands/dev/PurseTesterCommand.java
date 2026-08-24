package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand.Commands;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class PurseTesterCommand implements SubCommand {

  private final ServiceContext serviceContext;

  public PurseTesterCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(player);

    int amount = 0;

    if (args.length == 2) {
      amount = Integer.parseInt(args[1]);
    }

    pe.setPurse(amount);
    pe.setHasToBeUpdated(true);
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    if (args.length < 1 || !Commands.PURSE_TESTER.getName().equalsIgnoreCase(args[0])) {
      return false;
    }

    if (args.length == 1) {
      return true;
    }

    if (args.length == 2) {
      try {
        Integer.parseInt(args[1]);
        return true;
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return false;
  }
}