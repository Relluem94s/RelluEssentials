package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_CLOUDSAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_CUSTOMMOB;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_NOENCHANT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_RELLU;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_SMELT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_TELE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND_WORLDS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.MobHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluBoots;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluChestplate;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluHelmet;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluLeggings;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluPickaxe;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluShield;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluSword;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND)) {
            return false;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!p.getName().equalsIgnoreCase("Relluem94")) {
            sender.sendMessage(PLUGIN_COMMAND_INVALID);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;   
        }

        if (args.length > 1) {
            sender.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;   
        }

        if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND_CUSTOMMOB)) {
            cm(p.getLocation());
        } else if (args[0].equalsIgnoreCase("pick")) {
            p.getInventory().addItem(new RelluPickaxe().getCustomItem());
        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND_CLOUDSAILOR)) {
            p.getInventory().addItem(CustomItems.cloudSailor.getCustomItem());
            p.getInventory().addItem(CustomItems.cloudBoots.getCustomItem());
        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND_RELLU)) {
            rellu(p);
        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND_SMELT)) {
            CustomEnchants.autosmelt.addTo(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND_TELE)) {
            CustomEnchants.telekinesis.addTo(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND_NOENCHANT)) {
            CustomEnchants.autosmelt.removeFrom(p.getInventory().getItemInMainHand());
            CustomEnchants.telekinesis.removeFrom(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TEST_COMMAND_WORLDS)) {
            worlds(p);
        } else if (args[0].equalsIgnoreCase("bc")) {
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            RelluEssentials.getInstance().getDatabaseHelper().insertBag(1, pe.getId());
        } else if (args[0].equalsIgnoreCase("bo")) {
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            p.openInventory(BagHelper.getBag(1, pe));
        } else if (args[0].equalsIgnoreCase("bc2")) {
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            RelluEssentials.getInstance().getDatabaseHelper().insertBag(2, pe.getId());
        } else if (args[0].equalsIgnoreCase("bo2")) {
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            p.openInventory(BagHelper.getBag(2, pe));
        } else if (args[0].equalsIgnoreCase("di")) {
            di(p);
        } else if (args[0].equalsIgnoreCase("pl")) {
            pl(p);
        } else if (args[0].equalsIgnoreCase("sk")) {
            p.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.BAG_OF_COINS));
            p.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.MONEY_BAG));
        }

        return true;
    }

    private void cm(Location location){
        MobHelper mh = new MobHelper(location, EntityType.ZOMBIE, "§aX Æ A-XII", true);
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
    }

    private void rellu(Player p){
        p.getInventory().addItem(new RelluHelmet().getCustomItem());
        p.getInventory().addItem(new RelluChestplate().getCustomItem());
        p.getInventory().addItem(new RelluLeggings().getCustomItem());
        p.getInventory().addItem(new RelluBoots().getCustomItem());
        p.getInventory().addItem(new RelluShield().getCustomItem());
        p.getInventory().addItem(new RelluSword().getCustomItem());
    }

    private void worlds(Player p){
        try {
            WorldHelper.cloneWorld("world2", "world");
        } catch (WorldNotFoundException ex) {
            Logger.getLogger(TestCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

        p.teleport(Bukkit.getWorld("world2").getSpawnLocation());
    }

    private void di(Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        if (pe.getPlayerState().equals(PlayerState.DEFAULT)) {
            pe.setPlayerState(PlayerState.DAMAGE_INFO);
        } else {
            pe.setPlayerState(PlayerState.DEFAULT);
        }
    }

    private void pl(Player p){
        p.sendMessage("Health: " + p.getHealth());
        p.sendMessage("Food: " + p.getFoodLevel());
        p.sendMessage("Exp: " + p.getExp());
    }
}