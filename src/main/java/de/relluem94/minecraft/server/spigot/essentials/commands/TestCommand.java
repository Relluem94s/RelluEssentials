package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.*;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
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
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluBoots;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluChestplate;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluHelmet;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluLeggings;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluPickaxe;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluShield;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluSword;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("ZAQmNCRXEdwSGU7DvEcXTbBkp2qEaCSSNkQcMhL3m7KSDtmXWaxtbYCaQCFBR96fj")
public class TestCommand implements CommandConstruct {


    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
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

        if (args[0].equalsIgnoreCase(Commands.CUSTOM_MOB.getName())) {
            cm(p.getLocation());
        } else if (args[0].equalsIgnoreCase(Commands.PICKAXE.getName())) {
            p.getInventory().addItem(new RelluPickaxe().getCustomItem());
        } else if (args[0].equalsIgnoreCase(Commands.CLOUD_SAILOR.getName())) {
            p.getInventory().addItem(CustomItems.cloudSailor.getCustomItem());
            p.getInventory().addItem(CustomItems.cloudBoots.getCustomItem());
        } else if (args[0].equalsIgnoreCase(Commands.RELLU.getName())) {
            rellu(p);
        } else if (args[0].equalsIgnoreCase(Commands.SMELT.getName())) {
            CustomEnchants.autosmelt.addTo(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(Commands.TELE.getName())) {
            CustomEnchants.telekinesis.addTo(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(Commands.NO_ENCHANT.getName())) {
            CustomEnchants.autosmelt.removeFrom(p.getInventory().getItemInMainHand());
            CustomEnchants.telekinesis.removeFrom(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(Commands.WORLDS.getName())) {
            worlds(p);
        } else if (args[0].equalsIgnoreCase(Commands.DAMAGE_INFO.getName())) {
            di(p);
        } else if (args[0].equalsIgnoreCase(Commands.PLAYER_STATS.getName())) {
            pl(p);
        } else if (args[0].equalsIgnoreCase(Commands.SKULL.getName())) {
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

    private void rellu(@NotNull Player p){
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

        World world2 = Bukkit.getWorld("world2");
        if(world2 == null){
            return;
        }

        p.teleport(world2.getSpawnLocation());
    }

    private void di(Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        if (pe.getPlayerState().equals(PlayerState.DEFAULT)) {
            pe.setPlayerState(PlayerState.DAMAGE_INFO);
        } else {
            pe.setPlayerState(PlayerState.DEFAULT);
        }
    }

    private void pl(@NotNull Player p){
        p.sendMessage("Health: " + p.getHealth());
        p.sendMessage("Food: " + p.getFoodLevel());
        p.sendMessage("Exp: " + p.getExp());
    }

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        CUSTOM_MOB("cm"),
        CLOUD_SAILOR("cs"),
        PICKAXE("pick"),
        RELLU("rellu"),
        SMELT("smelt"),
        TELE("tele"),
        NO_ENCHANT("noenchant"),
        WORLDS("worlds"),
        PLAYER_STATS("pl"),
        DAMAGE_INFO("di"),
        SKULL("sk");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        if(strings.length == 1){
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }

        return tabList;
    }
}