package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class FakeAfkCommand implements SubCommand {

  private final PlayerService playerService;

  public FakeAfkCommand(PlayerService playerService) {
    this.playerService = playerService;
  }

  @Override
  public void execute(Player player, String[] args) {
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(player);

    if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)) {
      playerService.setAFK(player, false);
      pe.setPlayerState(PlayerState.DEFAULT);
    } else {
      pe.setPlayerState(PlayerState.FAKE_AFK_ON);
      playerService.setAFK(player, false);
      pe.setPlayerState(PlayerState.FAKE_AFK_ACTIVE);
    }
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.AFK.getName().equalsIgnoreCase(args[0]);
  }
}