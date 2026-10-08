package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.annotations.Generated;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command for developers that spawns a predefined {@link CustomMob} at the executing player's location.
 *
 * <p>The spawned mob is a named zombie equipped with full leather armor, a wooden sword and a shield.
 * It also has the glowing potion effect applied and is allowed to pick up items.</p>
 *
 * @author rellu
 */
public class CustomMobCommand implements SubCommand {

  @Generated // Can't be tested Bukkit Architecture Problem
  @Override
  public void execute(Player player, String[] args) {
    CustomMob mh = new CustomMob(player.getLocation(), EntityType.ZOMBIE, "§aX Æ A-XII", true);
    mh.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1));
    mh.setCanPickupItems(true);
    mh.spawn(new ItemStack(Material.WOODEN_SWORD, 1), new ItemStack(Material.SHIELD, 1),
        new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1),
        new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.CUSTOM_MOB
        .getName()
        .equalsIgnoreCase(args[0]);
  }
}