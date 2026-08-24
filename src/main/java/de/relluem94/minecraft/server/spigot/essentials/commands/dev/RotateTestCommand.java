package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class RotateTestCommand implements SubCommand {

  private final ServiceContext serviceContext;

  public RotateTestCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    Location startLocation = player.getLocation().clone();

    serviceContext.getPositionService()
        .setFirstPosition(player, startLocation.clone().add(1, 0, 0));
    serviceContext.getPositionService()
        .setSecondPosition(player, startLocation.clone().add(4, 2, 8));
    serviceContext.getSchedulerService()
        .runTaskLater(() -> player.performCommand("modify set stone"), 0L);
    serviceContext.getSchedulerService()
        .runTaskLater(() -> player.performCommand("modify copy"), 10L);
    serviceContext.getSchedulerService()
        .runTaskLater(() -> player.performCommand("modify clipboard rotate"), 11L);
    serviceContext.getSchedulerService().runTaskLater(
        () -> player.teleport(player.getLocation().clone().add(10, 0, 20)), 12L);
    serviceContext.getSchedulerService()
        .runTaskLater(() -> player.performCommand("modify paste"), 13L);
    serviceContext.getSchedulerService()
        .runTaskLater(() -> player.sendMessage("Clean Up in 400 ticks."), 140L);
    serviceContext.getSchedulerService()
        .runTaskLater(() -> player.sendMessage("Clean Up in 200 ticks.."), 340L);
    serviceContext.getSchedulerService().runTaskLater(() -> {
      player.sendMessage("Clean Up...");
      player.teleport(startLocation);
      player.performCommand("modify undo");
      player.performCommand("modify undo");
    }, 540L);
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.ROTATE_TEST.getName().equalsIgnoreCase(args[0]);
  }
}