package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobEquipment;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Manages the registration and lifecycle of custom mobs.
 *
 * @author rellu
 */
public class MobManager implements Enable, Disable {

  private ServiceContext serviceContext;

  @Override
  public void enable(Plugin plugin) {
    this.serviceContext = ((RelluEssentials) plugin).getServiceContext();

    CustomMobEquipment xaeaxiiCustomMobEquipment =
        new CustomMobEquipment(new ItemStack(Material.WOODEN_SWORD, 1), new ItemStack(Material.SHIELD, 1),
            new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1),
            new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1));

    this.serviceContext
        .getMobService()
        .registerDefinition(new CustomMobDefinition(new RelluEssentialsNamespacedKey(serviceContext
            .getPluginMetadataService()
            .getName(), "XÆAXII"), EntityType.ZOMBIE, "§aX Æ A-XII", true,
            List.of(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1)),
            xaeaxiiCustomMobEquipment, 20, false, false));
  }

  @Override
  public void disable(Plugin plugin) {
    serviceContext.getMobService().despawnAll();
  }
}
