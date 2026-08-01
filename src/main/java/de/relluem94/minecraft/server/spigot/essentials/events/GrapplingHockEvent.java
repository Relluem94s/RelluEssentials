package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper.isInWorld;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;

/**
 * Handles grappling hook mechanics for players in the lobby world.
 */
public class GrapplingHockEvent implements Listener {

  protected static final List<Player> COOL_DOWN = new ArrayList<>();

  /**
   * Launches the player toward the hook's location when the hook hits the ground or is reeled in.
   * Applies a cooldown to prevent spamming.
   */
  @EventHandler
  public void grapple(@NonNull PlayerFishEvent e) {
    if (!isInWorld(e.getPlayer(), PLUGIN_WORLD_LOBBY)) {
      return;
    }

    RegistryKey grapplingHookKey = RegistryKey.of(RelluEssentials.getInstance(), "grappling_hook");
    ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();

    if (ItemRegistry.identifyFromItemStack(itemInMainHand)
        .filter(grapplingHookKey::equals)
        .isEmpty()) {
      return;
    }

    if (e.getState().equals(State.IN_GROUND) || e.getState().equals(State.REEL_IN)) {
      if (!COOL_DOWN.contains(e.getPlayer())) {
        Location hookLocation = e.getHook().getLocation();
        Location playerLocation = e.getPlayer().getLocation();

        Vector playerVelocity = new Vector(
            hookLocation.getX() - playerLocation.getX(),
            (hookLocation.getY() - playerLocation.getY()) + 0.001,
            hookLocation.getZ() - playerLocation.getZ()
        );

        e.getPlayer().setVelocity(playerVelocity);
        COOL_DOWN.add(e.getPlayer());

        new BukkitRunnable() {
          @Override
          public void run() {
            COOL_DOWN.remove(e.getPlayer());
          }
        }.runTaskLater(RelluEssentials.getInstance(), 50L);
      } else {
        e.getPlayer()
            .sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_GRAPPLING_HOOK_COOLDOWN));
      }
    }
  }
}