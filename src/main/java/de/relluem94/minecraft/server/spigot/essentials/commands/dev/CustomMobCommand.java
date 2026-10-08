package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.annotations.Generated;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobEquipment;
import java.util.List;
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

  private final ServiceContext serviceContext;

  public CustomMobCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Generated // Can't be tested Bukkit Architecture Problem
  @Override
  public void execute(Player player, String[] args) {
    CustomMobEquipment customMobEquipment =
        new CustomMobEquipment(new ItemStack(Material.WOODEN_SWORD, 1), new ItemStack(Material.SHIELD, 1),
            new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1),
            new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1));

    CustomMobDefinition customMobDefinition = new CustomMobDefinition(
        EntityType.ZOMBIE,
        "§aX Æ A-XII",
        true,
        List.of(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1)),
        customMobEquipment,
        120,
        false, false);

    serviceContext.getMobService().spawnMob(customMobDefinition, player.getLocation());
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.CUSTOM_MOB
        .getName()
        .equalsIgnoreCase(args[0]);
  }
}