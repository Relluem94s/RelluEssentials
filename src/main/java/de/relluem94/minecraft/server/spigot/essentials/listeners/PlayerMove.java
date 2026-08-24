package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 *
 * @author rellu
 */

@ListenerName("PlayerMove")
public class PlayerMove implements ListenerConstruct {


  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  @EventHandler
  public void onMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();

    if (p.getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)) {
      if (e.getTo() != null && e.getTo().clone().add(0, -1, 0).getBlock().getType()
          .equals(Material.MYCELIUM)) {
        if (p.isSneaking()) {
          p.getWorld().playEffect(p.getLocation(), Effect.BAT_TAKEOFF, 5);
          p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 1F, 0F);
        } else {
          Vector dir = p.getLocation().getDirection().multiply(0.50);
          Vector vec = new Vector(dir.getX(), 2.0D, dir.getZ());
          p.setVelocity(vec);
          p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 2F, 0F);
          p.getWorld().playEffect(p.getLocation(), Effect.BAT_TAKEOFF, 5);
          p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 1));
          p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120, 9));
        }
      }
    }

    if (serviceContext.getPlayerService().getPlayerEntry(p.getUniqueId()) != null) {
      e.setCancelled(
          serviceContext.getPlayerService().getPlayerEntry(p.getUniqueId())
              .isAfk());
    }
  }
}