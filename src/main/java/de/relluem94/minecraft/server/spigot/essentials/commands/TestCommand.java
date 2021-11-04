package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.autosmelt;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.telekinesis;
import de.relluem94.minecraft.server.spigot.essentials.helpers.MobHelper;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND_CLOUDSAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND_CUSTOMMOB;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND_NOENCHANT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND_RELLU;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND_SMELT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND_TELE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_TEST_COMMAND_WORLDS;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND)) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.getName().equalsIgnoreCase("Relluem94")) {
                    if (args.length == 1) {
                        if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_CUSTOMMOB)) {
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
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_CLOUDSAILOR)) {
                            p.getInventory().addItem(CustomItems.cloudSailor.getCustomItem());
                            p.getInventory().addItem(CustomItems.cloudBoots.getCustomItem());
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_RELLU)) {
                            p.getInventory().addItem(CustomItems.relluHelmet.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluChestPlate.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluLeggings.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluBoots.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluShield.getCustomItem());
                            p.getInventory().addItem(CustomItems.relluSword.getCustomItem());
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_SMELT)) {
                            autosmelt.addTo(p.getInventory().getItemInMainHand());
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_TELE)) {
                            telekinesis.addTo(p.getInventory().getItemInMainHand());
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_NOENCHANT)) {
                            autosmelt.removeFrom(p.getInventory().getItemInMainHand());
                            telekinesis.removeFrom(p.getInventory().getItemInMainHand());

                            //////////////////////////////////////////////////////////////
                            //FOR TEST WITH LORE ACTIVATE THIS BLOCK                    //
                            //                                                          //
                            boolean isDebugged = false;                                 //
                            //                                                          //
                            if (isDebugged) {                                             //
                                ItemStack i = p.getInventory().getItemInMainHand();     //
                                ItemMeta im = i.getItemMeta();                          //
                                List<String> lore = im.getLore();                       //
                                String rarity = lore.get(lore.size() - 1);                //
                                lore.clear();                                           //
                                lore.add(rarity);                                       //
                                im.setLore(lore);                                       //
                                i.setItemMeta(im);                                      //
                            }                                                           //
                            //                                                          //
                            //FOR TEST FINISHED                                         //
                            //////////////////////////////////////////////////////////////
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_WORLDS)) {
                            try {
                                WorldHelper.cloneWorld("world2", "world");
                            } catch (WorldNotFoundException ex) {
                                Logger.getLogger(TestCommand.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            p.teleport(Bukkit.getWorld("world2").getSpawnLocation());
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
