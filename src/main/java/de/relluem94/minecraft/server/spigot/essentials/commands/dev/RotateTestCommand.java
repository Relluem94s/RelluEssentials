package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class RotateTestCommand implements SubCommand {

  private final SchedulerService schedulerService;

  public RotateTestCommand(ServiceContext context) {
    this.schedulerService = context.getSchedulerService();
  }

  @Override
  public void execute(Player player, String[] args) {
    Location startLocation = player.getLocation().clone();

    RelluEssentials.getInstance().position.put(player, new DoubleStore<>(
        startLocation.clone().add(1, 0, 0),
        startLocation.clone().add(4, 2, 8)
    ));

    schedulerService.runTaskLater(() -> player.performCommand("modify set stone"), 0L);
    schedulerService.runTaskLater(() -> player.performCommand("modify copy"), 10L);
    schedulerService.runTaskLater(() -> player.performCommand("modify clipboard rotate"), 11L);
    schedulerService.runTaskLater(
        () -> player.teleport(player.getLocation().clone().add(10, 0, 20)), 12L);
    schedulerService.runTaskLater(() -> player.performCommand("modify paste"), 13L);
    schedulerService.runTaskLater(() -> player.sendMessage("Clean Up in 400 ticks."), 140L);
    schedulerService.runTaskLater(() -> player.sendMessage("Clean Up in 200 ticks.."), 340L);
    schedulerService.runTaskLater(() -> {
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