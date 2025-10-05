package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("repair")
public class Repair implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta im = item.getItemMeta();

            if (im instanceof Damageable dmg && dmg.hasDamage()) {
                dmg.setDamage(0);
                item.setItemMeta(im);
                p.sendMessage(String.format(PLUGIN_COMMAND_REPAIR, p.getInventory().getItemInMainHand().getType().name()));
            }
            else{
                p.sendMessage(String.format(PLUGIN_COMMAND_CANNOT_REPAIR, p.getInventory().getItemInMainHand().getType().name()));
            }

            return true;
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
                return true;
            }

            ItemStack item = target.getInventory().getItemInMainHand();
            ItemMeta im = item.getItemMeta();

            if (im instanceof Damageable dmg && dmg.hasDamage()) {
                dmg.setDamage(0);
                item.setItemMeta(im);
                p.sendMessage(String.format(PLUGIN_COMMAND_REPAIR, target.getInventory().getItemInMainHand().getType().name()));
                target.sendMessage(String.format(PLUGIN_COMMAND_REPAIR_PLAYER, target.getInventory().getItemInMainHand().getType().name()));
            }
            else{
                p.sendMessage(String.format(PLUGIN_COMMAND_CANNOT_REPAIR, p.getInventory().getItemInMainHand().getType().name()));
            }

            return true;

        }
    }

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
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

        if(strings.length > 1){
            return tabList;
        }

        tabList.addAll(TabCompleterHelper.getOnlinePlayers());

        return tabList;
    }
}
