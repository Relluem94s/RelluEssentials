package de.relluem94.minecraft.server.spigot.essentials.npcs;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mannequin;

public class NpcMannequinAttributeApplier {

  private NpcMannequinAttributeApplier() {}

  public static void applyAttributes(Mannequin mannequin) {
    Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
      mannequin.setInvulnerable(true);
      mannequin.setCollidable(false);
      mannequin.setCanPickupItems(false);
      mannequin.setImmovable(true);
    }, 20L);
  }
}
