package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ToggleDamageInfoCommand implements SubCommand {

  private ServiceContext serviceContext;

  public ToggleDamageInfoCommand(ServiceContext context) {
    serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    PlayerEntry playerEntry = serviceContext.getPlayerService().getPlayerEntry(player);
    if (playerEntry.getPlayerState().equals(PlayerState.DEFAULT)) {
      playerEntry.setPlayerState(PlayerState.DAMAGE_INFO);
    } else {
      playerEntry.setPlayerState(PlayerState.DEFAULT);
    }
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.DAMAGE_INFO.getName().equalsIgnoreCase(args[0]);
  }
}