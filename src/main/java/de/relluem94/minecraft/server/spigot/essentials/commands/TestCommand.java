package de.relluem94.minecraft.server.spigot.essentials.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import org.bukkit.Bukkit;
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

import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.MobHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_CLOUDSAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_CUSTOMMOB;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_NOENCHANT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_RELLU;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_SMELT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_TELE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_WORLDS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND)) {
            if (isPlayer(sender)) {
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
                        }else if (args[0].equals("pick"))  {
                            p.getInventory().addItem(CustomItems.relluPickAxe.getCustomItem());
                        }
                        else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_CLOUDSAILOR)) {
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
                            CustomEnchants.autosmelt.addTo(p.getInventory().getItemInMainHand());
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_TELE)) {
                            CustomEnchants.telekinesis.addTo(p.getInventory().getItemInMainHand());
                        } else if (args[0].equals(PLUGIN_COMMAND_NAME_TEST_COMMAND_NOENCHANT)) {
                            CustomEnchants.autosmelt.removeFrom(p.getInventory().getItemInMainHand());
                            CustomEnchants.telekinesis.removeFrom(p.getInventory().getItemInMainHand());

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
                        else if (args[0].equals("bc")) {
                            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
                            RelluEssentials.dBH.insertBag(1, pe.getID());
                        }
                        else if (args[0].equals("bo")) {
                            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
                            p.openInventory(BagHelper.getBag(1, pe));
                        }
                        else if (args[0].equals("bc2")) {
                            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
                            RelluEssentials.dBH.insertBag(2, pe.getID());
                        }
                        else if (args[0].equals("bo2")) {
                            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
                            p.openInventory(BagHelper.getBag(2, pe)); 
                        }
                        else if(args[0].equals("di")){
                            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
                            if(pe.getPlayerState().equals(PlayerState.DEFAULT)){
                                pe.setPlayerState(PlayerState.DAMAGE_INFO);
                            }
                            else{
                                pe.setPlayerState(PlayerState.DEFAULT);
                            }
                        }
                        else if (args[0].equals("sk")) {
                            p.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.BAG_OF_COINS));
                            p.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.MONEY_BAG));
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
