package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command for developers that spawns a predefined {@link CustomMob} at the executing player's location.
 *
 * <p>The spawned mob is a named zombie equipped with full leather armor, a wooden sword and a shield.
 * It also has the glowing potion effect applied and is allowed to pick up items.</p>
 *
 * @author rellu
 */
@AllArgsConstructor
public class CustomMobCommand implements SubCommand {

  private final ServiceContext serviceContext;

  @Override
  public void execute(Player player, String[] args) {
    serviceContext
        .getMobService()
        .spawnMob(new RelluEssentialsNamespacedKey(serviceContext
            .getPluginMetadataService()
            .getName(), "XÆAXII"), player.getLocation());

    player.sendMessage("Alive: " + serviceContext
        .getMobService()
        .countAliveMobs() + " All: " + serviceContext
        .getMobService()
        .countSpawnedMobs());
    player.sendMessage("Definitions: " + serviceContext
        .getMobService()
        .findAllDefinitions()
        .size());
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.CUSTOM_MOB
        .getName()
        .equalsIgnoreCase(args[0]);
  }
}