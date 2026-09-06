package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command that clones an existing world and teleports the executing player to its spawn
 * location.
 */
public class CloneWorldCommand implements SubCommand {

  private final ServiceContext serviceContext;

  /**
   * Creates a new CloneWorldCommand with the given service context.
   *
   * @param context the service context used to access plugin and server services
   */
  public CloneWorldCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    try {
      WorldHelper.cloneWorld("world2", "world");
    } catch (WorldNotFoundException ex) {
      Logger.getLogger(CloneWorldCommand.class.getName()).log(Level.SEVERE, null, ex);
    }

    World world2 = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getWorld("world2");
    if (world2 == null) {
      return;
    }

    player.teleport(world2.getSpawnLocation());
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.WORLDS.getName().equalsIgnoreCase(args[0]);
  }
}