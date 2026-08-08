package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class FakeAfkCommand implements SubCommand {

  private final ServiceContext serviceContext;

  public FakeAfkCommand(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  @Override
  public void execute(Player player, String[] args) {
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(player);

    if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)) {
      serviceContext.getPlayerService().setAFK(player, false);
      pe.setPlayerState(PlayerState.DEFAULT);
    } else {
      pe.setPlayerState(PlayerState.FAKE_AFK_ON);
      serviceContext.getPlayerService().setAFK(player, false);
      pe.setPlayerState(PlayerState.FAKE_AFK_ACTIVE);
    }
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.AFK.getName().equalsIgnoreCase(args[0]);
  }
}