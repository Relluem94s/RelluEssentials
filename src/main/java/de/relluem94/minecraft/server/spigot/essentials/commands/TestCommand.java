package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.MobHelper;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ZAQmNCRXEdwSGU7DvEcXTbBkp2qEaCSSNkQcMhL3m7KSDtmXWaxtbYCaQCFBR96fj")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.getName().equalsIgnoreCase("Relluem94")) {
                    if (args.length == 1) {
                        if (args[0].equals("cm")) {
                            MobHelper mh = new MobHelper(p.getLocation(), EntityType.ZOMBIE, "§aX Æ A-XII", true);
                            mh.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1));
                            mh.setCanPickupItems(true);

                            mh.spawn(
                                    new ItemStack(Material.WOODEN_SWORD, 1),
                                    new ItemStack(Material.SHIELD, 1),
                                    new ItemStack(Material.LEATHER_HELMET, 1),
                                    new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                                    new ItemStack(Material.LEATHER_LEGGINGS, 1),
                                    new ItemStack(Material.LEATHER_BOOTS, 1)
                            );
                        } else if (args[0].equals("cs")) {
                            p.getInventory().addItem(CustomItems.cloudSailor.getCustomItem());
                            p.getInventory().addItem(CustomItems.cloudBoots.getCustomItem());
                        } else if (args[0].equals("rellu")) {
                            p.getInventory().addItem(CustomItems.relluHelmet.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluChestPlate.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluLeggings.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluBoots.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluShield.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluSword.getCustomItem());
                        } else if (args[0].equals("smelt")) {
                            p.getInventory().getItemInMainHand().addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt"))), 1);

                        } else if (args[0].equals("tele")) {
                            p.getInventory().getItemInMainHand().addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis"))), 1);
                        }
                    }
                    return true;
                } else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10, 1));
                    return true;
                }
            }
        }
        return false;
    }
}
